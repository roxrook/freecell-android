package chan.android.game.freecell.view;


import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import chan.android.game.freecell.model.Card;
import chan.android.game.freecell.model.Rank;
import chan.android.game.freecell.model.Suit;

@SuppressWarnings("NewApi")
public class CardView extends View implements View.OnTouchListener {

    public static final int WIDTH = 200;

    public static final int HEIGHT = 300;

    private static final Paint PAINT = new Paint();

    static {
        PAINT.setAntiAlias(true);
        PAINT.setFilterBitmap(true);
        PAINT.setDither(true);
    }

    private Card card = new Card(Rank.ACE, Suit.SPADES);

    private Bitmap bitmap;

    public CardView(Context context, Card card) {
        super(context);
        this.card = card;
        init();
    }

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyles) {
        super(context, attrs, defStyles);
        init();
    }

    private void init() {
        Bitmap original = BitmapFactory.decodeResource(getResources(), card.getResourceId());
        bitmap = Bitmap.createScaledBitmap(original, WIDTH, HEIGHT, true);
        original.recycle();
        // setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, PAINT);
    }

    public Card getCard() {
        return card;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder builder = new DragShadowBuilder(view);
            view.startDrag(data, builder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
