# Android-ABI-setting

[ref](https://developer.android.com/ndk/guides/abis.html)


理想情况下,`App`应当同时提供`armeabi`,`armeabi-v7a`,`arm64-v8a`,`x86`,`x86_64`,`mips`,`mips64`的`so`文件，一些比较小的类库提供全部的`so`以保证在全平台能以最优性能运行，是没有问题的，但是有时候会有一些较大的类库,比如`ffmpeg`,`opencv`之类,一个`so`文件就有3~5mb,全部提供会让APK体积过于膨胀,这个时候就需要进行适当的权衡.

其中 x86(x86_32) 的真实设备实际上并不存在,只有模拟器可以调成`x86_32`,实际存在的CPU都是`x86_64`的，并且所有`x86_64`的设备都支持`Secondary ABI`, 大部分配置都是`armeabi/armeabi-v7a`, 不考虑性能最优的话，是可以不提供的。`mips`略过，只有早期起一些`android laptop`是这种架构。

这样我们需要实际考虑的ABI只有`armeabi`, `armeabi-v7a`, `arm64-v8a`。所以通常哪需要提供`armeabi`, `armeabi-v7a`, `arm64-v8a`的`so`即可；
