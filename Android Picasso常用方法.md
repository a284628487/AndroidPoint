# Picasso

最简单的开始：
```
Picasso.with(context).load(url).into(imageView);
```
或者

下载一张图片并显示到一个imageView，并且设定tag；
```
Picasso.with(context) //
        .load(url) //
        .placeholder(R.drawable.placeholder) //
        .error(R.drawable.error) //
        .fit() // ？？？？
        .tag(context) //
        .into(view);
```

### RequestCreator

`Picasso.with(context).load(url)`返回的就是一个`RequestCreator`对象，拿到这个对象之后，可以给它设置一些参数，当图片请求回来之后，会先执行我们给定的设置，然后再显示出来；

常用的方法比如 `rotate`, `resize`, `transform`等；


- 指定Tag
```
Picasso.with(this).load(url).tag();
```

- 列表上下滚动时暂停和恢复tag

```
final Picasso picasso = Picasso.with(context);
if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
   picasso.resumeTag(context);
} else {
   picasso.pauseTag(context);
}
```

- 取消请求
```
Picasso.with(this).cancelRequest(imageView);
Picasso.with(this).cancelTag(context);
```

- 获取图片Bitmap对象
```
picasso.load(R.drawable.noise).get();
```

### 显示通知(AppWidgetProvider)

```
RemoteViews remoteViews =
        new RemoteViews(activity.getPackageName(), R.layout.notification_view);

Intent intent = new Intent(activity, SampleGridViewActivity.class);

NotificationCompat.Builder builder =
        new NotificationCompat.Builder(activity).setSmallIcon(R.drawable.icon)
                .setContentIntent(PendingIntent.getActivity(activity, -1, intent, 0))
                .setContent(remoteViews);

Notification notification = builder.getNotification();
// Bug in NotificationCompat that does not set the content.
if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
    notification.contentView = remoteViews;
}

NotificationManager notificationManager =
(NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.notify(NOTIFICATION_ID, notification);

// Now load an image for this notification.
Picasso.with(activity) //
    .load(Data.URLS[new Random().nextInt(Data.URLS.length)]) //
    .resizeDimen(R.dimen.notification_icon_width_height,
    R.dimen.notification_icon_width_height) //
    .into(remoteViews, R.id.photo, NOTIFICATION_ID, notification);
```

### Transform

```
public class GrayscaleTransformation implements Transformation {

    private final Picasso picasso;

    public GrayscaleTransformation(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap result = createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Bitmap noise;
        try {
            noise = picasso.load(R.drawable.noise).get();
        } catch (IOException e) {
            throw new RuntimeException("Failed to apply transformation! Missing resource.");
        }

        BitmapShader shader = new BitmapShader(noise, REPEAT, REPEAT);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(source, 0, 0, paint);

        paint.setColorFilter(null);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        source.recycle();
        noise.recycle();

        return result;
    }

    @Override
    public String key() {
        return "grayscaleTransformation()";
    }
}
```
