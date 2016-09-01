
# 无限轮播

```
private class BannerAdapter extends PagerAdapter {
    private List<View> list;

    private BannerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return 10000;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % list.size();
        View view = list.get(position);
        if (null != view.getParent()) {
            ViewPager vg = (ViewPager) view.getParent();
            vg.removeView(view);
        }
        container.addView(view, 0);
        return view;
    }
}
```

注意：list 里面的条目数必须大于2；

### 普通的ViewPagerAdapter

```
public class ViewPagerViewAdapter extends PagerAdapter {
    private List<View> list;

    public ViewPagerViewAdapter(List<View> list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }
}
```