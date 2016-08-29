# Android 启动优化

在一个`Activity`打开时，如果该`Activity`所属的`Application`还没有启动，那么系统会为这个`Activity`创建一个进程（每创建一个进程都会调用一次`Application`，所以`Application`的`onCreate()`方法可能会被调用多次），在进程的创建和初始化中，势必会消耗一些时间，在这个时间里，`WindowManager`会先加载`APP`里的主题样式里的窗口背景（`windowBackground`）作为预览元素，然后才去真正的加载布局，如果这个时间过长，而默认的背景又是黑色或者白色，这样会给用户造成一种错觉，这个APP很卡，很不流畅，自然也影响了用户体验。

### 消除启动时的白屏/黑屏

在用户点击手机桌面`APP`的时候，看到的黑屏或者白屏其实是界面渲染前的第一帧，如上一段所述，要解决这个问题无非就是将`Theme`里的`windowBackground`设置成我们想要让用户看到的画面就可以了，这里有2种做法：

- 将背景图设置成我们APP的Welcome图，作为APP启动的引导，现在市面上大部分的APP也是这么做的。

```
<style name="AppWelcome" parent="AppTheme">
    <item name="android:windowBackground">@mipmap/bg_welcome_start</item>
</style>
```

- 将背景颜色设置为透明色，这样当用户点击桌面APP图片的时候，并不会"立即"进入APP，而且在桌面上停留一会，其实这时候APP已经是启动的了，只是我们心机的把`Theme`里的`windowBackground`的颜色设置成透明的，强行把锅甩给了手机应用厂商（手机反应太慢了啦，哈哈），其实现在微信也是这样做的，不信你可以试试。

```
<style name="Appwelcome" parent="android:Theme.Translucent.NoTitleBar.Fullscreen"/>
```

## 关于启动优化
上面的做法其实可以达到"秒开"APP的效果，不过确不是真实的速度，在Activity创建过程中其实是会经过一系列framework层的操作，在日常开发中，我们都会去重写Application类，然后在Application里进行一些初始化操作，比如存放用户标识的静态化TOKEN，第三方SDK的初始化等。
这里给出几点建议：
- 不要让Application参与业务的操作
- 不要在APPlication进行耗时操作，比如有些开发者会在自己的APP里一系列文件夹或文件（比如我自己），这些I/O操作应该放到"确实该使用的时候再去创建"亦或者是数据库的一些操作。
- 不要以静态变量的方式在Application中保存数据等。

当然这是绝对的理想主义，把上面的"不要"2字之前添上"尽量"2字吧，毕竟在实际开发中，这样做确实会让我们方便许多。另外，布局也是很重要的，尽量的去减少布局的复杂性，布局深度，因为在View绘制的过程中，测量也是很耗费性能的。
