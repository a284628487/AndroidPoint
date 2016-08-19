# Android 小知识点

- getParent().requestDisallowInterceptTouchEvent(true);剥夺父view 对touch 事件的处理权，谁用谁知道。

- ArgbEvaluator.evaluate(float fraction, Object startValue, Object endValue); 用于根据一个起始颜色值和一个结束颜色值以及一个偏移量生成一个新的颜色，分分钟实现类似于微信底部栏滑动颜色渐变。

- Canvas中clipRect、clipPath和clipRegion 剪切区域的API。

- Bitmap.extractAlpha ();返回一个新的Bitmap，capture原始图片的alpha 值。有的时候我们需要动态的修改一个元素的背景图片又不希望使用多张图片的时候，通过这个方法，结合Canvas 和Paint 可以动态的修改一个纯色Bitmap的颜色。

- HandlerThread，代替不停new Thread 开子线程的重复体力写法。

- IntentService,一个可以干完活后自己去死且不需要我们去管理子线程的Service。

- Palette，5.0加入的可以提取一个Bitmap 中突出颜色的类，结合上面的Bitmap.extractAlpha，你懂的。

- Executors. newSingleThreadExecutor() 单线程顺序执行的任务队列。

- android:animateLayoutChanges=”true”，LinearLayout中添加View 的动画的办法，支持通过setLayoutTransition()自定义动画。

- ViewDragHelper，自定义一个子View可拖拽的ViewGroup 时，处理各种事件很累吧，嗯? what the fuck!!

- GradientDrawable，之前接手公司的项目，发现有个阴影效果还不错，以为是切的图片，一看代码，什么鬼= =！

- AsyncQueryHandler，如果做系统工具类的开发，比如联系人短信辅助工具等，肯定免不了和ContentProvider打交道，如果数据量不是很大的情况下，随便搞，如果数据量大的情况下，了解下这个类是很有必要的，需要注意的是，这玩意儿吃异常..

- ViewFlipper，实现多个view的切换(循环)，可自定义动画效果，且可针对单个切换指定动画。

- 有朋友提到了在自定义View时有些方法在开启硬件加速的时候没有效果的问题，在API16之后确实有很多方法不支持硬件加速，通常我们关闭硬件加速都是在清单文件中通过，其实android也提供了针对特定View关闭硬件加速的方法,调用View.setLayerType(View.LAYER_TYPE_SOFTWARE, null);即可。

- android util包中的Pair类，可以方便的用来存储一”组”数据。注意不是key value。

- StateListDrawable，定义Selector通常的办法都是xml文件，但是有的时候我们的图片资源可能是从服务器动态获取的，比如很多app所谓的皮肤，这种时候就只能通StateListDrawable来完成了，各种addState即可。

- android:descendantFocusability，ListView的item中CheckBox等元素抢焦点导致item点击事件无法响应时，除了给对应的元素设置 focusable,更简单的是在item根布局加上android:descendantFocusability=”blocksDescendants”

- android:duplicateParentState=”true”，让子View跟随其Parent的状态，如pressed等。常见的使用场景是某些时候一个按钮很小，我们想要扩大其点击区域的时候通常会再给其包裹一层布局，将点击事件写到Parent上，这时候如果希望被包裹按钮的点击效果对应的Selector继续生效的话，这时候duplicateParentState就派上用场了。

- includeFontPadding=”false”，TextView默认上下是有一定的padding的，有时候我们可能不需要上下这部分留白，加上它即可。

- Messenger，面试的时候通常都会被问到进程间通信，一般情况下大家都是开始背书，AIDL巴拉巴拉。。有一天在鸿神的博客看到这个，嗯，如他所说，又可以装一下了。

- TextView.setError();用于验证用户输入。

- ViewConfiguration.getScaledTouchSlop()触发移动事件的最小距离，自定义View处理touch事件的时候，有的时候需要判断用户是否真的存在movie，系统提供了这样的方法。

- ValueAnimator.reverse(); 顺畅的取消动画效果。

- ViewStub，有的时候一块区域需要根据情况显示不同的布局，通常我们都会通过setVisibility的方法来显示和隐藏不同的布局，但是这样默认是全部加载的，用ViewStub可以更好的提升性能。

- onTrimMemory，在Activity中重写此方法，会在内存紧张的时候回调（支持多个级别），便于我们主动的进行资源释放，避免OOM。

- EditTxt.setImeOptions， 使用EditText弹出软键盘时，修改回车键的显示内容(一直很讨厌用回车键来交互，所以之前一直不知道这玩意儿)

- TextView.setCompoundDrawablePadding，代码设置TextView的drawable padding。

- ImageSwitcher，可以用来做图片切换的一个类，类似于幻灯片。

- WeakHashMap，直接使用HashMap有时候会带来内存溢出的风险，使用WaekHashMap实例化Map。当使用者不再有对象引用的时候，WeakHashMap将自动被移除对应Key值的对象。

- ActivityLifecycleCallbacks接口，用于在Application类中监听各Activity的状态变化；

- android.media.ThumbnailUtils类，用来获取媒体（图片、视频）缩略图；

- 获取`StatusBar`高度
```
int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
if (resId > 0) {
    int statusBarHeight = getResources().getDimensionPixelSize(resId);
}
```

- 获取`StatusBar`高度2
```
Rect r = new Rect();
getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
// 1280 * 720
System.out.println(r.top); // 50
System.out.println(r.bottom); // 1184
System.out.println(r.left); // 0
System.out.println(r.right); // 720
```

- 判断`Activity`是否全屏
```
public boolean isFullScreen() { 
    int flags = ((Activity) context).getWindow().getAttributes().flags; 
    boolean flag = false; 
    if ((flags & 1024) == 1024) { 
	    flag = true; 
	} 
	return flag; 
}
```

- 获取`Activity`的`ContentView`
```
getWindow().findViewById(Window.ID_ANDROID_CONTENT);
```


