# Android 锁屏状态下弹出Activity

- 在接收消息广播的onReceive里，跳转到你要显示的界面。
```
Intent intent = new Intent(context, MessageActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);        
context.startActivity(intent);
```

- 为该activity的添加Flag：
```
super.onCreate(savedInstanceState);
getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
setContentView(R.layout.activity_message);
```

- 设置activity的theme属性：
```
android:theme="@android:style/Theme.Wallpaper.NoTitleBar"
```

- 添加事件，进入app，突破锁屏：
KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
keyguardLock.disableKeyguard();

- 加入权限：
```
<uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
```
