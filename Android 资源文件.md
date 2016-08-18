# Android 资源文件

- 运行时改变`Shape`的`color`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/shape_id">
        <shape android:shape="rectangle">
            <solid android:color="#36adcf"></solid>
        </shape>
    </item>
</layer-list>
```

```xml
<Button
    android:id="@+id/countdown"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/bg" />
```

```java
LayerDrawable ld = (LayerDrawable) v.getBackground();
GradientDrawable gd = (GradientDrawable) ld.findDrawableByLayerId(R.id.shape_id);
gd.setColor(0xffff4400);
```

- `Drawable`嵌套

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android" >
    <item android:id="@+id/shape_id">
        <rotate
            android:fromDegrees="45"
            android:toDegrees="45"
            android:pivotX="-40%"
            android:pivotY="87%" >
            <shape
                android:shape="rectangle"  >
                <stroke android:color="@android:color/transparent" android:width="10dp"/>
                <solid
                    android:color="#000000"  />
            </shape>
        </rotate>
    </item>
</layer-list> 
```

获取`RotateDrawable`和`GradientDrawable`

```java
LayerDrawable layerDrawable = (LayerDrawable) iv.getBackground(); 
RotateDrawable rotateDrawable = (RotateDrawable) layerDrawable.findDrawableByLayerId(R.id.shape_id); 
GradientDrawable shapeTriangle = (GradientDrawable) rotateDrawable.getDrawable();
```

- 代码编写`ColorStateList`

```
int[][] states = new int[][]{
        new int[]{android.R.attr.state_enabled}, // enabled
        new int[]{-android.R.attr.state_enabled}, // disabled
        new int[]{-android.R.attr.state_checked}, // unchecked
        new int[]{android.R.attr.state_pressed}  // pressed
};
int[] colors = new int[]{
        Color.YELLOW,
        Color.RED,
        Color.GREEN,
        Color.BLUE
};
ColorStateList myList = new ColorStateList(states, colors);
```
可用状态参考[ref](http://developer.android.com/reference/android/R.attr.html#state_above_anchor)

与`xml`的对应
```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:color="@color/white"/>
    <item android:color="@color/black"/>
</selector>
```

```
ColorStateList myColorStateList = new ColorStateList(
        new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        },
        new int[] {
                context.getResources().getColor(R.color.white),
                context.getResources().getColor(R.color.black),
        }
);
```

- triangle_top

```
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/shape_id">
        <!-- 倒三角 -->
        <rotate
            android:fromDegrees="45"
            android:pivotX="135%"
            android:pivotY="15%"
            android:toDegrees="45">
            <shape android:shape="rectangle">
                <solid android:color="@color/background_color_black" />
            </shape>
        </rotate>
    </item>
</layer-list>
```

- triangle_bottom

```
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/shape_id">
        <!-- 正三角 -->
        <rotate
            android:fromDegrees="45"
            android:pivotX="-40%"
            android:pivotY="87%"
            android:toDegrees="45">
            <shape android:shape="rectangle">
                <solid android:color="@color/background_color_black" />
            </shape>
        </rotate>
    </item>
</layer-list>
```
