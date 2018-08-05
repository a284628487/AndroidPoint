---
title: Android MediaCodec 视频解码
date: 2018-03-27 20:34:11
tags:
	- Android
	- Media
---

结合**MediaExtractor**提取元数据，使用**MediaCodec**视频编解码。
- 使用MediaCoder来解码video，也可以使用来进行视频编码。(decoding/encoding)
- 使用TimeAnimator来用来同步动画帧。
- 使用TextureView来进行render显示。
<!--more-->

# Main Step

1. 创建**TextureView**，创建**MediaExtractor**`new MediaExtractor()`和**TimeAnimator**`new TimeAnimator()`.
2. 调用`MediaExtractor#setDataSource(this, videoUri, null)`启动播放.
3. 调用`MediaExtractor#getTrackCount()`获取track数量，它们不是全部都是video track，调用`MediaExtractor#unselectTrack(i)`取消选中track.
4. 使用`MediaExtractor#getTrackFormat(i).getString(MediaFormat.KEY_MIME)`获取track类型，如果类型包括"video/"，则表示是video，调用`MediaExtractor#selectTrack(i)`选择它.
5. 创建**MediaCodec**: `MediaCodec.createDecoderByType(mimeType)`,配置MediaCodec，通过`configure(trackFormat, textureView, null, 0)`, `trackFormat`通过`调用MediaExtractor#getTrackFormat(i)`获取.
6. 给**TimeAnimation**设置TimeListener重写`onTimeUpdate(final TimeAnimator animation, final long totalTime, final long deltaTime)`.在**onTimeUpdate**中，检查media track是否已经到达尾部, 使用`MediaExtractor#getSampleFlags()`确保flag不是**MediaCodec.BUFFER_FLAG_END_OF_STREAM**.
7. 在**onTimeUpdate**中，如果不是end，则使用`queueInputBuffer(index, 0, size, presentationTimeUs, flags)`让MediaDecoder开始解码。如何设置buffer参考[MediaCodec](http://developer.android.com/reference/android/media/MediaCodec.html).
8. 在media sample解码之后, media sample需要前进, 调用`MediaExtractor#advance()`(阻塞操作，需要放到后台线程).
9. 获取解码好的帧：`dequeueOutputBuffer(info, timeout)` 并且释放 `releaseOutputBuffer(i, true)`, 参考[MediaCodec](http://developer.android.com/reference/android/media/MediaCodec.html).
10. 在`onPause()`或者达到视频尾部，调用`TimeAnimation#end()`方法，之后调用`MediaCodec#stop()`和`MediaCodec#release()`，最后调用`MediaExtractor#release()`.
> From [MediaCodec](http://developer.android.com/reference/android/media/MediaCodec.html)，流程太繁琐，通过官方的Demo有个更好的认知。


## MediaCodecWrapper.java

```Java
public class MediaCodecWrapper {

    private final static String TAG = "MediaCodecWrapper";

    static MediaCodec.CryptoInfo cryptoInfo = new MediaCodec.CryptoInfo();

    // MediaFormat 发生改变
    public interface OutputFormatChangedListener {
        void outputFormatChanged(MediaCodecWrapper sender, MediaFormat newFormat);
    }

    private OutputFormatChangedListener mOutputFormatChangedListener = null;

    public interface OutputSampleListener {
        void outputSample(MediaCodecWrapper sender, MediaCodec.BufferInfo info, ByteBuffer buffer);
    }

    private OutputSampleListener mOutputSampleListener = null;

    private MediaCodec mDecoder;
    // MediaCodec内部的buffers。codec通过index来管理使用这些buffer，而不是直接通过引用。
    private ByteBuffer[] mInputBuffers;
    private ByteBuffer[] mOutputBuffers;

    // 当前可用(writing)input buffers的索引，按它们从codec中dequeue的顺序来消费他。
    private Queue<Integer> mAvailableInputBuffers;

    // 已经成功解码完成的frame，按它们从codec中produced的顺序排列。
    private Queue<Integer> mAvailableOutputBuffers;

    // 每一帧的输出buffer的BufferInfo，通过index来索引。
    private MediaCodec.BufferInfo[] mOutputBufferInfo;

    private MediaCodecWrapper(MediaCodec codec) {
        mDecoder = codec;
        mDecoder.start();
        mInputBuffers = mDecoder.getInputBuffers();
        mOutputBuffers = mDecoder.getOutputBuffers();
        Log.e(TAG, "MediaCodecWrapper: " + mInputBuffers.length + " <-> " + mOutputBuffers.length);
        mOutputBufferInfo = new MediaCodec.BufferInfo[mOutputBuffers.length];
        mAvailableInputBuffers = new ArrayDeque<Integer>(mOutputBuffers.length);
        mAvailableOutputBuffers = new ArrayDeque<Integer>(mInputBuffers.length);
    }

    // Releases resources and ends the encoding/decoding session.
    public void stopAndRelease() {
        mDecoder.stop();
        mDecoder.release();
        mDecoder = null;
    }

    public static MediaCodecWrapper findVideoMediaCodecWrapper(final MediaFormat trackFormat,
                                                               Surface surface) throws IOException {
        MediaCodecWrapper result = null;
        final String mimeType = trackFormat.getString(MediaFormat.KEY_MIME);
        // 查找video trace，并根据mimeType创建MediaCodec
        if (mimeType.contains("video/")) {
            MediaCodec videoCodec = MediaCodec.createDecoderByType(mimeType);
            // 配置videoCodec，设置解码输出到surface。
            videoCodec.configure(trackFormat, surface, null, 0);
            // 创建Wrapper实例
            result = new MediaCodecWrapper(videoCodec);
        }
        return result;
    }

    // 提交解码buffer index
    public boolean writeSample(final MediaExtractor extractor,
                               final boolean isSecure,
                               final long presentationTimeUs,
                               int flags) {
        boolean result = false;
        if (!mAvailableInputBuffers.isEmpty()) {
            int index = mAvailableInputBuffers.remove();
            ByteBuffer buffer = mInputBuffers[index];
            // extractor将视频帧的raw data提取出来并存放到传递的参数buffer中，并返回sampleSize。
            // 之后交由MediaCodec来将raw data解码图像。
            int size = extractor.readSampleData(buffer, 0);
            if (size <= 0) {
                flags |= MediaCodec.BUFFER_FLAG_END_OF_STREAM;
            }
            // 提交buffer index开始解码。presentationTimeUs即当前帧的position(play time)
            if (!isSecure) {
                Log.e(TAG, "writeSample: " + index + " -> " + size);
                mDecoder.queueInputBuffer(index, 0, size, presentationTimeUs, flags);
            } else {
                extractor.getSampleCryptoInfo(cryptoInfo);
                mDecoder.queueSecureInputBuffer(index, 0, cryptoInfo, presentationTimeUs, flags);
            }
            result = true;
        }
        return result;
    }

    // 获取可以release的buffer，并根据buffer提取出media info。
    public boolean peekSample(MediaCodec.BufferInfo out_bufferInfo) {
        update();
        boolean result = false;
        if (!mAvailableOutputBuffers.isEmpty()) {
            int index = mAvailableOutputBuffers.peek(); // peekFirst()
            MediaCodec.BufferInfo info = mOutputBufferInfo[index];
            // metadata of the sample
            out_bufferInfo.set(
                    info.offset,
                    info.size,
                    info.presentationTimeUs,
                    info.flags);
            result = true;
        }
        return result;
    }

    /**
     * Processes, releases and optionally renders the output buffer available at the head of the queue.
     */
    public void popSample(boolean render) {
        update();
        if (!mAvailableOutputBuffers.isEmpty()) {
            int index = mAvailableOutputBuffers.remove(); // removeFirst
            Log.e(TAG, "popSample: " + index );
            if (render && mOutputSampleListener != null) {
                ByteBuffer buffer = mOutputBuffers[index];
                MediaCodec.BufferInfo info = mOutputBufferInfo[index];
                mOutputSampleListener.outputSample(this, info, buffer);
            }
            // release buffer，并且将帧渲染到Surface。
            mDecoder.releaseOutputBuffer(index, render);
        }
    }

    private void update() {
        int index;
        // 从coder中获取可用的input buffer的索引，之后使用同样的顺序来填充buffer，
        // 如果没有可用的有效的buffer，返回-1。
        while ((index = mDecoder.dequeueInputBuffer(0)) != MediaCodec.INFO_TRY_AGAIN_LATER) {
            Log.e(TAG, "update#dequeueInputBuffer: " + index);
            mAvailableInputBuffers.add(index);
        }
        // 如果output buffers发生改变，则使用新的output buffer集合。
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        // 获取已经被成功解码的帧Buffer，或者是 INFO_* 常量，如果是buffer，则将信息保存到info中。
        while ((index = mDecoder.dequeueOutputBuffer(info, 0)) != MediaCodec.INFO_TRY_AGAIN_LATER) {
            Log.e(TAG, "update#dequeueOutputBuffer: " + index);
            switch (index) {
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED: // output buffers changed
                    mOutputBuffers = mDecoder.getOutputBuffers();
                    mOutputBufferInfo = new MediaCodec.BufferInfo[mOutputBuffers.length];
                    mAvailableOutputBuffers.clear();
                    break;
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED: // format changed
                    outputFormatChanged();
                    break;
                default:
                    // 确保Index是有效的然后再添加到buffers中。
                    if (index >= 0) {
                        mOutputBufferInfo[index] = info;
                        mAvailableOutputBuffers.add(index);
                    }
                    break;
            }
        }
    }

    // FormatChanged
    private void outputFormatChanged() {
        if (mOutputFormatChangedListener != null) {
            mOutputFormatChangedListener.outputFormatChanged(MediaCodecWrapper.this,
                    mDecoder.getOutputFormat());
        }
    }

    private class WriteException extends Throwable {
        private WriteException(final String detailMessage) {
            super(detailMessage);
        }
    }

    /**
     * Write a media sample to the decoder.
     * <p>
     * A "sample" here refers to a single atomic access unit in the media stream. The definition
     * of "access unit" is dependent on the type of encoding used, but it typically refers to
     * a single frame of video or a few seconds of audio. {@link MediaExtractor}
     * extracts data from a stream one sample at a time.
     *
     * @param input              A ByteBuffer containing the input data for one sample. The buffer must be set
     *                           up for reading, with its position set to the beginning of the sample data and its limit
     *                           set to the end of the sample data.
     * @param presentationTimeUs The time, relative to the beginning of the media stream,
     *                           at which this buffer should be rendered.
     * @param flags              Flags to pass to the decoder. See {@link MediaCodec#queueInputBuffer(int,
     *                           int, int, long, int)}
     */
    public boolean writeSample(final ByteBuffer input,
                               final MediaCodec.CryptoInfo crypto,
                               final long presentationTimeUs,
                               final int flags) throws MediaCodec.CryptoException, WriteException {
        boolean result = false;
        int size = input.remaining();

        // check if we have dequed input buffers available from the codec
        if (size > 0 && !mAvailableInputBuffers.isEmpty()) {
            int index = mAvailableInputBuffers.remove();
            ByteBuffer buffer = mInputBuffers[index];

            // we can't write our sample to a lesser capacity input buffer.
            if (size > buffer.capacity()) {
                throw new WriteException(String.format(
                        "Insufficient capacity in MediaCodec buffer: "
                                + "tried to write %d, buffer capacity is %d.",
                        input.remaining(),
                        buffer.capacity()));
            }

            buffer.clear();
            buffer.put(input);

            // 提交buffer index到codec開始解碼，presentationTimeUs表示当前sample的position(play time)
            if (crypto == null) {
                mDecoder.queueInputBuffer(index, 0, size, presentationTimeUs, flags);
            } else {
                mDecoder.queueSecureInputBuffer(index, 0, crypto, presentationTimeUs, flags);
            }
            result = true;
        }
        return result;
    }
}
```

## MainActivity.java

```Java
public class MainActivity extends AppCompatActivity {

    Handler h = new Handler();

    private TextureView mPlaybackView;
    private TimeAnimator mTimeAnimator = new TimeAnimator();

    private MediaCodecWrapper mCodecWrapper;
    private MediaExtractor mExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mPlaybackView = findViewById(R.id.textureView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mPlaybackView.isAvailable()) {
            startPlayback();
        } else {
            mPlaybackView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    startPlayback();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimeAnimator != null && mTimeAnimator.isRunning()) {
            mTimeAnimator.end();
        }

        if (mCodecWrapper != null) {
            mCodecWrapper.stopAndRelease();
            mExtractor.release();
            mExtractor = null;
        }
    }

    public void startPlayback() {
//        Uri videoUri = Uri.parse("android.resource://"
//                + getPackageName() + "/"
//                + R.raw.vid_bigbuckbunny);
        mExtractor = new MediaExtractor();
        Uri videoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/video.mp4"));
        try {
            // BEGIN_INCLUDE(initialize_extractor)
            mExtractor.setDataSource(this, videoUri, null);
            int nTracks = mExtractor.getTrackCount();

            for (int i = 0; i < nTracks; ++i) {
                mExtractor.unselectTrack(i);
            }

            for (int i = 0; i < nTracks; ++i) {
                mCodecWrapper = MediaCodecWrapper.findVideoMediaCodecWrapper(mExtractor.getTrackFormat(i),
                        new Surface(mPlaybackView.getSurfaceTexture()));
                if (mCodecWrapper != null) {
                    mExtractor.selectTrack(i);
                    break;
                }
            }
            // END_INCLUDE(initialize_extractor)
            mTimeAnimator.setTimeListener(new TimeAnimator.TimeListener() {
                @Override
                public void onTimeUpdate(final TimeAnimator animation,
                                         final long totalTime,
                                         final long deltaTime) {
                    boolean isEos = ((mExtractor.getSampleFlags() & MediaCodec
                            .BUFFER_FLAG_END_OF_STREAM) == MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    // BEGIN_INCLUDE(write_sample)
                    if (!isEos) {
                        // 提交sample到codec，如果成功，前进(advance)extractor到一下帧sample
                        boolean result = mCodecWrapper.writeSample(mExtractor, false,
                                mExtractor.getSampleTime(), mExtractor.getSampleFlags());
                        if (result) { // blocking operation => worker thread
                            mExtractor.advance();
                        }
                    }
                    // END_INCLUDE(write_sample)

                    // 检查queue中的第一个sample是否解码完成并且可以release，如果是，则render到surface上。
                    MediaCodec.BufferInfo out_bufferInfo = new MediaCodec.BufferInfo();
                    mCodecWrapper.peekSample(out_bufferInfo);
                    // BEGIN_INCLUDE(render_sample)
                    if (out_bufferInfo.size <= 0 && isEos) {
                        mTimeAnimator.end();
                        mCodecWrapper.stopAndRelease();
                        mExtractor.release();
                    } else if (out_bufferInfo.presentationTimeUs / 1000 < totalTime) {
                        // 取出sample并且发送到surface进行渲染。
                        mCodecWrapper.popSample(true);
                    }
                    // END_INCLUDE(render_sample)
                }
            });
            //
            mTimeAnimator.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## 核心流程

**MediaCodec**解码核心流程：

1. `MediaCodec#dequeueInputBuffer()` 获取空闲可用的buffers的索引。
2. `MediaExtractor#readSampleData()` 传入1中根据索引提到的ByteBuffer使用MediaExtractor提取Sample。
3. `MediaExtractor#advance()` 向前前进一帧。
4. `MediaCodec#queueInputBuffer()` 提交buffer index将MediaExtractor提取到的Sample解码。
5. `MediaCodec#dequeueOutputBuffer()` 获取已经被成功解码的帧的buffer索引。
6. `MediaCodec#releaseOutputBuffer()` 释放buffer，并且渲染。

### SourcDir
`/developers/samples/android/media/BasicMediaDecoder/`
