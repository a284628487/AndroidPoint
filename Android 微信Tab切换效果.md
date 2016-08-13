# Android 微信Tab切换效果

#### Activity

```
public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageView imageView;
    private LinearLayout append;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_viewpager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imageView = (ImageView) findViewById(R.id.indicator);
        LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        viewPager.setAdapter(new MyAdapter());
        imageView.setLayoutParams(lps);
        append = (LinearLayout) findViewById(R.id.append);
        //
        ViewPager.OnPageChangeListener opcl = new MyOnPageChangeListener(imageView, append
                , Color.parseColor("#369369"), Color.parseColor("#e5e5e5"));
        viewPager.setOnPageChangeListener(opcl);
    }

    private static class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        final String TAG = "MyOnPageChangeListener";
        private int pageIndex = 0;
        private float lastOffset = -1;
        private int currIndex = 0;
        private int width = 0;
        private View mIndicator;
        private ViewGroup mTabGroup;
        private int highLight;
        private int normal;
        ArgbEvaluator ae = new ArgbEvaluator();

        private MyOnPageChangeListener(View mIndicator, ViewGroup tabGroup, int hl, int nml) {
            this.mIndicator = mIndicator;
            this.mTabGroup = tabGroup;
            this.highLight = hl;
            this.normal = nml;
            Context context = mIndicator.getContext();
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
            width = lps.width = (context.getResources().getDisplayMetrics().widthPixels + 1) / 3;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if ((position == 0 || position == 2) && positionOffset == 0) return;
            if (positionOffset == 0) return;

            if (lastOffset == -1) {
                lastOffset = positionOffset;
                if (lastOffset > 0.75) {
                    lastOffset = 1;
                } else {
                    lastOffset = 0;
                }
            }

            boolean left = true;

            if (lastOffset == 1) { // scroll -> left
                if (position == currIndex - 1) {

                } else { // scroll -> right
                    left = false;
                }
            } else {
                if (position == currIndex - 1) { // scroll -> left

                } else { // scroll -> right
                    left = false;

                }
            }

            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
            lps.leftMargin = currIndex * width;
            mIndicator.setLayoutParams(lps);

            if (currIndex > position) { // 向左
                if (positionOffset > 1) position = 1;
                lps.leftMargin -= (1 - positionOffset) * width;
            } else { // 向右
                if (positionOffset > 1) position = 1;
                lps.leftMargin += positionOffset * width;
            }

            Log.e(TAG, currIndex + " -> " + position);

            if (!left) { // ->
                Integer color1 = (Integer) ae.evaluate(positionOffset, highLight, normal);
                Integer color2 = (Integer) ae.evaluate(1 - positionOffset, highLight, normal);
                mTabGroup.getChildAt(currIndex).setBackgroundColor(color1);
                mTabGroup.getChildAt(currIndex + 1).setBackgroundColor(color2);
            } else { // <-
                Integer color1 = (Integer) ae.evaluate(positionOffset, highLight, normal);
                Integer color2 = (Integer) ae.evaluate(1 - positionOffset, highLight, normal);
                mTabGroup.getChildAt(currIndex - 1).setBackgroundColor(color1);
                mTabGroup.getChildAt(currIndex).setBackgroundColor(color2);
            }
        }

        // 在非第一页与最后一页时，滑动到下一页，position为当前页位置；滑动到上一页：position为当前页-1

        // positionOffset 滑动到下一页，[0,1)区间上 `+` 变化；滑动到上一页：(1,0]区间上 `-` 变化
        // positionOffsetPixels这个和positionOffset很像：滑动到下一页，[0,宽度)区间上变化；滑动到上一页：(宽度,0]区间上变化

        // 第一页时：滑动到上一页position=0 ，其他基本为0 ；最后一页滑动到下一页 position为当前页位置，其他两个参数为0
        @Override
        public void onPageSelected(int position) {
            pageIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING: // begin
                    break;
                case ViewPager.SCROLL_STATE_SETTLING: // change
                    break;
                case ViewPager.SCROLL_STATE_IDLE: // over
                    lastOffset = -1;
                    currIndex = pageIndex;
                    break;
            }
        }
    }


    private class MyAdapter extends PagerAdapter {
        List<View> list = new ArrayList<>();

        public MyAdapter() {
            ImageView iv1 = new ImageView(ViewPagerActivity.this);
            iv1.setBackgroundColor(0xffff0000);
            list.add(iv1);

            ImageView iv2 = new ImageView(ViewPagerActivity.this);
            iv2.setBackgroundColor(0xff00ff00);
            list.add(iv2);

            ImageView iv3 = new ImageView(ViewPagerActivity.this);
            iv3.setBackgroundColor(0xff0000ff);
            list.add(iv3);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
```

#### xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <android.support.v4.view.ViewPager
        android:id="@+id/viewpager"
        android:layout_width="match_parent"
        android:layout_height="400dp" />

    <ImageView
        android:id="@+id/indicator"
        android:layout_width="wrap_content"
        android:layout_height="4px"
        android:background="#36adcf" />

    <LinearLayout
        android:id="@+id/append"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:orientation="horizontal">

        <View
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:background="#ff369369" />

        <View
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:background="#ffe5e5e5" />

        <View
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:background="#ffe5e5e5" />
    </LinearLayout>

</LinearLayout>
```
