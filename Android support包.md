# Android support包使用记录

## NotificationCompat.Builder

```java
Notification notification = new NotificationCompat.Builder(this)
        .setContentTitle("Message from: ")
        .setContentText("message")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(null)
        .setAutoCancel(false)
        .build();
notification.flags = Notification.FLAG_NO_CLEAR;
NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.notify(0, notification);
```
`NotificationCompat.Builder`没有提供`setFlags()`方法，如果要设置`Notification`的`flags`，可以先使用`build()`方法生成一个`Notification`然后再去设置它的`flags`；



