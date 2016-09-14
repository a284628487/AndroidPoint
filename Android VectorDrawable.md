# VectorDrawable

### SVG 和 VectorDrawable 的基本知识

`VectorDrawable` 并没有支持所有的 SVG 规范，目前只支持 [PathData](http://www.w3.org/TR/SVG/paths.html#PathData) 和有限的 Group 功能。另外还有一个 clip-path 属性来支持后面绘图的区域。 所以对于使用 VectorDrawable 而言，我们只需要了解 SVG 的 PathData 规范即可。通过查看 PathData 文档，可以看到 path 数据包含了一些绘图命令，比如 ：

- M：move to 移动绘制点
- L：line to 直线
- Z：close 闭合
- C：cubic bezier 三次贝塞尔曲线
- Q：quatratic bezier 二次贝塞尔曲线
- A：ellipse 圆弧

### 绘制三角形示例

svg:
```
<svg width="4cm" height="4cm" viewBox="0 0 400 400"
     xmlns="http://www.w3.org/2000/svg" version="1.1">
  <rect x="1" y="1" width="398" height="398"
        fill="none" stroke="blue" />
  <path d="M 100 100 L 300 100 L 200 300 z"
        fill="red" stroke="blue" stroke-width="3" />
</svg>
```
示例的画布大小为 400 X 400像素（左上角坐标为0,0； 右下角坐标为400,400）， path 路径为： 移动到 100、100 位置，然后画一条线到绝对坐标 300、100位置，然后画一条线到绝对坐标 200、300位置，然后画一条线到该路径的起始位置。这样一个倒三角形就绘制出来了。
只需要把上面的 path 路径中的数据放到 VectorDrawable 定义xml文件中，就可以实现一个 Android 版本的 矢量图了。

```
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="400dp"
    android:height="400dp"
    android:viewportHeight="400"
    android:viewportWidth="400">
    <path
        android:pathData="M 100 100 L 300 100 L 200 300 z"
        android:strokeColor="#000000"
        android:strokeWidth="5"
        android:fillColor="#FF0000"
        />
</vector>

```
vector 上有两个宽高设置，其中 `android:viewportWidth/Height` 是指矢量图里面的画布大小，而 `android:width/height` 是指该矢量图所对应的 VectorDrawable 的大小。

### path

```
android:name 定义该 path 的名字，这样在其他地方可以通过名字来引用这个路径
android:pathData 和 SVG 中 d 元素一样的路径信息。
android:fillColor 定义填充路径的颜色，如果没有定义则不填充路径
android:strokeColor 定义如何绘制路径边框，如果没有定义则不显示边框
android:strokeWidth 定义路径边框的粗细尺寸
android:strokeAlpha 定义路径边框的透明度
android:fillAlpha 定义填充路径颜色的透明度
android:trimPathStart 从路径起始位置截断路径的比率，取值范围从 0 到1
android:trimPathEnd 从路径结束位置截断路径的比率，取值范围从 0 到1
android:trimPathOffset 设置路径截取的范围 Shift trim region (allows showed region to include the start and end), in the range from 0 to 1.
android:strokeLineCap 设置路径线帽的形状，取值为 butt, round, square.
android:strokeLineJoin 设置路径交界处的连接方式，取值为 miter,round,bevel.
android:strokeMiterLimit 设置斜角的上限，Sets the Miter limit for a stroked path. 注：当strokeLineJoin设置为 “miter” 的时候， 绘制两条线段以锐角相交的时候，所得的斜面可能相当长。当斜面太长，就会变得不协调。strokeMiterLimit 属性为斜面的长度设置一个上限。这个属性表示斜面长度和线条长度的比值。默认是 10，意味着一个斜面的长度不应该超过线条宽度的 10 倍。如果斜面达到这个长度，它就变成斜角了。当 strokeLineJoin 为 “round” 或 “bevel” 的时候，这个属性无效。
```

### vector

```
android:name 定义该drawable的名字
android:width 定义该 drawable 的内部(intrinsic)宽度,支持所有 Android 系统支持的尺寸，通常使用 dp
android:height 定义该 drawable 的内部(intrinsic)高度,支持所有 Android 系统支持的尺寸，通常使用 dp
android:viewportWidth 定义矢量图视图的宽度，视图就是矢量图 path 路径数据所绘制的虚拟画布
android:viewportHeight 定义矢量图视图的高度，视图就是矢量图 path 路径数据所绘制的虚拟画布
android:tint 定义该 drawable 的 tint 颜色。默认是没有 tint 颜色的
android:tintMode 定义 tint 颜色的 Porter-Duff blending 模式，默认值为 src_in
android:autoMirrored 设置当系统为 RTL (right-to-left) 布局的时候，是否自动镜像该图片。比如 阿拉伯语。
android:alpha 该图片的透明度属性
```

### group

有时候我们需要对几个路径一起处理，这样就可以使用 group 元素来把多个 path 放到一起。
```
android:name 定义 group 的名字
android:rotation 定义该 group 的路径旋转多少度
android:pivotX 定义缩放和旋转该 group 时候的 X 参考点。该值相对于 vector 的 viewport 值来指定的。
android:pivotY 定义缩放和旋转该 group 时候的 Y 参考点。该值相对于 vector 的 viewport 值来指定的。
android:scaleX 定义 X 轴的缩放倍数
android:scaleY 定义 Y 轴的缩放倍数
android:translateX 定义移动 X 轴的位移。相对于 vector 的 viewport 值来指定的。
android:translateY 定义移动 Y 轴的位移。相对于 vector 的 viewport 值来指定的。
通过上面的属性可以看出， group 主要是用来设置路径做动画的关键属性的。
```
example
```
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="400dp"
    android:height="400dp"
    android:viewportHeight="400"
    android:viewportWidth="400">
    <group
        android:name="name"
        android:pivotX="200"
        android:pivotY="200"
        android:rotation="90">
        <path
            android:fillColor="#FF0000"
            android:pathData="M 100 100 L 300 100 L 200 300 z"
            android:strokeColor="#000000"
            android:strokeWidth="5" />
    </group>
</vector>


[Read more](http://blog.chengyunfeng.com/?p=834#ixzz4JLKvO0cz)
```

`vector`还支持`clip-path`元素。定义当前绘制的剪切路径。注意，clip-path 只对当前的 group 和子 group 有效。

```
android:name 定义 clip path 的名字
android:pathData 和 android:pathData 的取值一样。
```

### 修改Drawable填充颜色

```
VectorDrawable vd = (VectorDrawable) getResources().getDrawable(R.drawable.heart);
vd.setColorFilter(0xff00ff00, PorterDuff.Mode.SRC_IN);
view.setBackground(vd);
```

参考文档
- [PathData](http://www.w3.org/TR/SVG/paths.html#PathData)
- [Google 官方文档](https://developer.android.com/reference/android/graphics/drawable/VectorDrawable.html)

# AnimatedVectorDrawable

`VectorDrawable` 是一张静态图, `AnimatedVectorDrawable` 则可以实现动画图片;
定义矢量动画图,一般需要三个步骤:

- 定义VectorDrawable

```
<vector>...</vector>
```

- 定义动画文件

```
<objectAnimator
     android:duration="6000"
     android:propertyName="rotation"
     android:valueFrom="0"
     android:valueTo="360" />
```

```
<set xmlns:android="http://schemas.android.com/apk/res/android">
     <objectAnimator
            android:duration="3000"
            android:propertyName="pathData"
            android:valueFrom="M300,70 l 0,-70 70,70 0,0   -70,70z"
            android:valueTo="M300,70 l 0,-70 70,0  0,140 -70,0 z"
            android:valueType="pathType"/>
</set>

```

- 定义AnimatedVectorDrawable

```
<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
   android:drawable="@drawable/vectordrawable" >
     <target
         android:name="rotationGroup"
         android:animation="@anim/rotation" />
     <target
         android:name="v"
         android:animation="@anim/path_morph" />
 </animated-vector>
```

由于矢量图的特点，`AnimatedVectorDawable` 可以实现一些很特别的效果，对 `VectorDrawable` 里的pathData做动画，可以从一个图形渐变到另一个图形，比如Material Design里的toolbar icon；对trimPathStart、trimPathEnd做动画，可以得到图形的绘制轨迹。


参考文档

- [Link](https://developer.android.com/training/material/drawables.html)
- [Google 官方文档](https://developer.android.com/reference/android/graphics/drawable/AnimatedVectorDrawable.html)
- [AnimatedVectorDrawable Demo](https://gist.github.com/nickbutcher/b3962f0d14913e9746f2)
- [Demo](https://github.com/liuchenx/DribSearch)
- [JS](http://www.jianshu.com/p/709994b08683)
