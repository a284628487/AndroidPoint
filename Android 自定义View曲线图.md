
### 使用 cubic 画平滑曲线

```
public class MyView extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<Point> points = new ArrayList<>();
    private Path path;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        if (points.size() == 0) {
            path = new Path();
            int w = getWidth();
            int h = getHeight();
            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                int y = r.nextInt(h);
                points.add(new Point(w / 10 * i, y));
            }
            mPaint.setStrokeWidth(4);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.RED);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0) {
            return;
        }
        init();
        Point start, end;
        Point p3 = new Point(), p4 = new Point();
        for (int i = 1; i < points.size(); i++) {
            start = points.get(i - 1);
            end = points.get(i);
            int centerX = (start.x + end.x) / 2;
            p3.y = start.y;
            p3.x = centerX;
            p4.y = end.y;
            p4.x = centerX;

            path.reset();
            path.moveTo(start.x, start.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, end.x, end.y);
            canvas.drawPath(path, mPaint);
        }
    }
}
```

### 两个第三方经典库

[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)

[hellocharts-android](https://github.com/lecho/hellocharts-android)
