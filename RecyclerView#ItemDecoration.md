# RecyclerView.ItemDecoration

主要包含下面3个方法
```
    onDraw(Canvas c, RecyclerView parent, State state)
    onDrawOver(Canvas c, RecyclerView parent, State state)
    getItemOffsets(Rect outRect, View view, RecyclerView parent, State state)
```

- `getItemOffsets` 中为 `outRect` 设置的4个方向的值，将被计算进所有 `decoration` 的尺寸中，而这个尺寸，被计入了 `RecyclerView` 每个 `itemView` 的 `padding` 中
- 在 `onDraw` 为 `divider` 设置绘制范围，并绘制到 `canvas` 上，而这个绘制范围可以超出在 `getItemOffsets` 中设置的范围，但由于 `decoration` 是绘制在 `childView` 的底下，所以并不可见，但是会存在 `overdraw`
- `decoration` 的 `onDraw`，`childView` 的 `onDraw`，`decoration` 的 `onDrawOver`，这三者是依次发生的，`onDrawOver` 是绘制在最上层的，所以它的绘制位置并不受限制

#### getItemOffsets

官方样例的 DividerItemDecoration 里面是这样实现的：
```
if (mOrientation == VERTICAL_LIST) {
    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
} else {
    outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
}
```
这个outRect设置的四个值是什么意思呢？先来看看它是在哪里调用的，它在`RecyclerView`中唯一被调用的地方就是 `getItemDecorInsetsForChild(View child)` 函数。
```
Rect getItemDecorInsetsForChild(View child) {
    final LayoutParams lp = (LayoutParams) child.getLayoutParams();
    if (!lp.mInsetsDirty) {
        return lp.mDecorInsets;
    }
 
    final Rect insets = lp.mDecorInsets;
    insets.set(0, 0, 0, 0);
    final int decorCount = mItemDecorations.size();
    for (int i = 0; i < decorCount; i++) {
        mTempRect.set(0, 0, 0, 0);
        mItemDecorations.get(i).getItemOffsets(mTempRect, child, this, mState);
        insets.left += mTempRect.left;
        insets.top += mTempRect.top;
        insets.right += mTempRect.right;
        insets.bottom += mTempRect.bottom;
    }
    lp.mInsetsDirty = false;
    return insets;
}
```
可以看到，`getItemOffsets` 函数中设置的值被加到了 `insets` 变量中，并被该函数返回，那么 `insets` 又是啥呢？

根据`Inset Drawable`文档，它的使用场景是：当一个`view`需要的背景小于它的边界时。例如按钮图标较小，但是我们希望按钮有较大的点击热区，一种做法是使用`ImageButton`，设置`background="@null"`，把图标资源设置给src属性，这样`ImageButton`可以大于图标，而不会导致图标也跟着拉伸到`ImageButton`那么大。那么使用`Inset drawable`也能达到这样的目的。但是相比之下有什么优势呢？src属性也能设置`selector drawable`，所以点击态也不是问题。

回到正题，`getItemDecorInsetsForChild` 函数中会重置 `insets` 的值，并重新计算，计算方式就是把所有 `ItemDecoration` 的 `getItemOffsets` 中设置的值累加起来，而这个 `insets` 实际上是 `RecyclerView` 的 `child` 的 `LayoutParams` 中的一个属性，它会在 `getTopDecorationHeight`, `getBottomDecorationHeight` 等函数中被返回，那么这个 `insets` 的意义就很明显了，它记录的是所有 `ItemDecoration`所需要的尺寸的总和。

而在 `RecyclerView` 的 `measureChild(View child, int widthUsed, int heightUsed)` 函数中，调用了 `getItemDecorInsetsForChild`，并把它算在了 `childView` 的 `padding` 中。

```
public void measureChild(View child, int widthUsed, int heightUsed) {
    final LayoutParams lp = (LayoutParams) child.getLayoutParams();
 
    final Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
    widthUsed += insets.left + insets.right;
    heightUsed += insets.top + insets.bottom;
    final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(),
            getPaddingLeft() + getPaddingRight() + widthUsed, lp.width,
            canScrollHorizontally());
    final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(),
            getPaddingTop() + getPaddingBottom() + heightUsed, lp.height,
            canScrollVertically());
    if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
        child.measure(widthSpec, heightSpec);
    }
}
```
上面这段代码中调用 getChildMeasureSpec 函数的第三个参数就是 childView 的 padding，而这个参数就把 insets 的值算进去了。那么现在就可以确认了，getItemOffsets 中为 outRect 设置的4个方向的值，将被计算进所有 decoration 的尺寸中，而这个尺寸，被计入了 RecyclerView 每个 item view 的 padding 中。

#### onDraw
先来看看官方样例的 DividerItemDecoration 实现：
```
public void drawVertical(Canvas c, RecyclerView parent) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
        final View child = parent.getChildAt(i);
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        final int top = child.getBottom() + params.bottomMargin +
                Math.round(ViewCompat.getTranslationY(child));
        final int bottom = top + mDivider.getIntrinsicHeight();
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }
}
```
drawVertical 是为纵向的 RecyclerView 绘制 divider，遍历每个 child view ，把 divider 绘制到 canvas 上，而 mDivider.setBounds 则设置了 divider 的绘制范围。其中，left 设置为 parent.getPaddingLeft()，也就是左边是 parent 也就是 RecyclerView 的左边界加上 paddingLeft 之后的位置，而 right 则设置为了 RecyclerView 的右边界减去 paddingRight 之后的位置，那这里左右边界就是 RecyclerView 的内容区域了。top 设置为了 child 的 bottom 加上 marginBottom 再加上 translationY，这其实就是 childView 的下边界，bottom 就是 divider 绘制的下边界了，它就是简单地 top 加上 divider 的高度。

#### onDrawOver
有一点需要注意：decoration 的 onDraw，child view 的 onDraw，decoration 的 onDrawOver，这三者是依次发生的。而由于 onDrawOver 是绘制在最上层的，所以它的绘制位置并不受限制（当然，decoration 的 onDraw 绘制范围也不受限制，只不过不可见），所以利用 onDrawOver 可以做很多事情，例如为 RecyclerView 整体顶部绘制一个蒙层，或者为特定的 item view 绘制蒙层。

#### 小结
- getItemOffsets 中为 outRect 设置的4个方向的值，将被计算进所有 decoration 的尺寸中，而这个尺寸，被计入了 RecyclerView 每个 item view 的 padding 中
- 在 onDraw 为 divider 设置绘制范围，并绘制到 canvas 上，而这个绘制范围可以超出在 getItemOffsets 中设置的范围，但由于 decoration 是绘制在 child view 的底下，所以并不可见，但是会存在 overdraw
- decoration 的 onDraw，child view 的 onDraw，decoration 的 onDrawOver，这三者是依次发生的
- onDrawOver 是绘制在最上层的，所以它的绘制位置并不受限制

```
public class DividerDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;

    private int mOrientation;

    public DividerDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setDivider(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mDivider.getIntrinsicHeight() + 20;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }
}
```
