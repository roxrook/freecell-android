package chan.android.game.freecell.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import chan.android.game.freecell.R;

@SuppressWarnings("NewApi")
public class CardPileLayout extends ViewGroup implements View.OnDragListener, View.OnTouchListener {

    private static final int VERTICAL_SHIFT = 50;

    private static final int CARD_WIDTH = CardView.WIDTH;

    private static final int CARD_HEIGHT = CardView.HEIGHT;

    private Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);

    private int x;

    private int y;

    private Paint paint;

    public CardPileLayout(Context context) {
        super(context);
        init();
    }

    public CardPileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardPileLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void addCardView(CardView view) {
        final int count = getChildCount();
        super.addView(view);
    }

    private void init() {
        setOnDragListener(this);
        setOnTouchListener(this);
        x = 0;
        y = 0;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec, CARD_WIDTH), measureHeight(heightMeasureSpec, CARD_HEIGHT));
    }

    private int measureWidth(int measureSpec, int cardWidth) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getPaddingLeft() + getPaddingRight() + cardWidth;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec, int cardHeight) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            final int count = getChildCount();
            result = getPaddingTop() + getPaddingBottom() + (count - 1) * VERTICAL_SHIFT + cardHeight;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(x, y, x + 200, y + 200, paint);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        final int count = getChildCount();
        int top = 0;
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.layout(0, top, r, b);
            top += VERTICAL_SHIFT;
        }
    }

    /**
     * Get left margin from the root layout
     */
    private int getLeftMargin(View v) {
        if (v.getParent() == v.getRootView()) {
            return v.getLeft();
        } else {
            return v.getLeft() + getLeftMargin((View) v.getParent());
        }
    }

    /**
     * Get top margin from the root layout
     */
    private int getTopMargin(View v) {
        if (v.getParent() == v.getRootView()) {
            return v.getTop();
        } else {
            return v.getTop() + getTopMargin((View) v.getParent());
        }
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackground(enterShape);
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackground(normalShape);
                break;

            case DragEvent.ACTION_DROP:
                View view = (View) event.getLocalState();
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
                CardPileLayout container = (CardPileLayout) v;
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackground(normalShape);
                break;

            default:
                break;
        }
        return true;
    }

    private int getCardIndex(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
            return -1;
        }
        int idx = y / VERTICAL_SHIFT;
        return Math.min(idx, getChildCount() - 1);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int idx = getCardIndex(event);
            x = getLeft() + (int) event.getX();
            y = getTop() + (int) event.getY();
            if (idx != -1) {
                Toast.makeText(getContext(), "idx= " + idx, Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }
}
