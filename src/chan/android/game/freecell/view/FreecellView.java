package chan.android.game.freecell.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import chan.android.game.freecell.model.*;
import chan.android.game.freecell.util.Precondition;
import chan.android.game.freecell.util.Logger;

public class FreecellView extends SurfaceView implements View.OnTouchListener, SurfaceHolder.Callback {

    /**
     * Listener for gameview
     */
    public interface OnMoveListener {

        public void onMove(Move move);
    }

    private static final int TOTAL_PILE = 16;

    private static final int DISTANCE_FROM_FINGER_X = 50;

    private static final int DISTANCE_FROM_FINGER_Y = 50;

    private static final int FREE_PILE_BEGIN = 0;

    private static final int FREE_PILE_END = 4;

    private static final int HOME_PILE_BEGIN = 4;

    private static final int HOME_PILE_END = 8;

    private static final int REGULAR_PILE_BEGIN = 8;

    private static final int REGULAR_PILE_END = 16;

    private int cardWidth = 80;

    private int cardHeight = 120;

    private int slotVerticalShift = 0;

    private int verticalShift = 0;

    private PileCard[] pileCards;

    private PileCard movingPile;

    private int dragPileIndex = -1;

    private int dragCardIndex = -1;

    private Deck deck;

    private static final Paint PAINT = new Paint();

    private int dragX = 0;

    private int dragY = 0;

    private OnMoveListener moveListener;

    private GameThread gameThread;

    private Paint borderPaint;

    static {
        PAINT.setAntiAlias(true);
        PAINT.setFilterBitmap(true);
        PAINT.setDither(true);
    }

    public FreecellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
        init();
    }

    public FreecellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreecellView(Context context) {
        this(context, null, 0);
    }

    public void setMoveListener(OnMoveListener listener) {
        moveListener = listener;
    }

    private void init() {
        pileCards = new PileCard[TOTAL_PILE];

        borderPaint = new Paint();
        borderPaint.setColor(Color.RED);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5.0f);
        borderPaint.setColor(Color.RED);

        // Set call back for surface view
        getHolder().addCallback(this);
        setFocusable(true);
    }


    public void drawNewDeck() {
        deck = new Deck();
        deck.shuffle();
        deal(deck);
        invalidate();
    }

    private void deal(Deck deck) {

        // First 8 are slot card, single card pile
        for (int i = FREE_PILE_BEGIN; i < FREE_PILE_END; ++i) {
            pileCards[i] = new FreePileCard();
        }

        for (int i = HOME_PILE_BEGIN; i < HOME_PILE_END; ++i) {
            pileCards[i] = new HomePileCard();
        }

        // Last 8 are pile card
        for (int i = REGULAR_PILE_BEGIN; i < REGULAR_PILE_END; ++i) {
            pileCards[i] = new PileCard(deck.deal(7));
        }

        movingPile = new MovePileCard();
    }

    private void autoMove(int begin, int end) {
        Precondition.checkValidRange(begin, end);
        for (int from = begin; from < end; ++from) {
            PileCard src = pileCards[from];
            if (!src.isEmpty()) {
                Card bottom = src.getBottomCard();
                int to = canMoveToHome(bottom);
                if (to != -1) {
                    PileCard dest = pileCards[to];
                    src.removeCardsFromEnd(1);
                    dest.addCard(bottom);
                    moveListener.onMove(new Move(from, to, 1));
                    invalidate();
                    autoMove();
                }
            }
        }
    }

    public void autoMove() {
        if (!anyHomeAvailable()) {
            return;
        }
        autoMove(FREE_PILE_BEGIN, FREE_PILE_END);
        autoMove(REGULAR_PILE_BEGIN, REGULAR_PILE_END);
    }

    private boolean anyHomeAvailable(int begin, int end) {
        Precondition.checkValidRange(begin, end);
        for (int i = begin; i < end; ++i) {
            if (!pileCards[i].isEmpty()) {
                Card bottom = pileCards[i].getBottomCard();
                if (canMoveToHome(bottom) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean anyHomeAvailable() {
        return anyHomeAvailable(FREE_PILE_BEGIN, FREE_PILE_END) || anyHomeAvailable(REGULAR_PILE_BEGIN, REGULAR_PILE_END);
    }

    private int canMoveToHome(Card card) {
        for (int i = HOME_PILE_BEGIN; i < HOME_PILE_END; ++i) {
            if (pileCards[i].canAdd(card)) {
                return i;
            }
        }
        return -1;
    }

    public int countFreeSpot() {
        int count = 0;
        for (int i = FREE_PILE_BEGIN; i < FREE_PILE_END; ++i) {
            count += pileCards[i].isEmpty() ? 1 : 0;
        }
        return count + 1;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        cardWidth = getWidth() / 8;
        cardHeight = (cardWidth * 4) / 3;
        verticalShift = cardHeight / 4;
        slotVerticalShift = cardHeight * 2;

        PileCard.setDrawingInfo(cardWidth, cardHeight, verticalShift);
    }

    /**
     * Return the pile index when the user touch the screen
     *
     * @param event Touch event
     */
    public int getTouchingPileIndex(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (0 <= y && y <= cardHeight) {
            return x / cardWidth;
        } else {
            int index = REGULAR_PILE_BEGIN + x / cardWidth;
            if (index < 0) {
                index = 0;
            } else if (index > REGULAR_PILE_END - 1) {
                index = REGULAR_PILE_END - 1;
            }
            return index;
        }
    }

    public int getTouchingCardIndexOfPile(PileCard pile, MotionEvent event) {
        if (pile instanceof FreePileCard) {
            return pile.isEmpty() ? -1 : 0;
        }

        int y = (int) event.getY();
        int count = pile.count() - 1;
        int currentTotalHeight = slotVerticalShift + cardHeight + (count - 1) * verticalShift;
        if (y > currentTotalHeight) {
            return -1;
        }
        int index = (y - slotVerticalShift) / verticalShift;
        if (index < -1) {
            index = -1;
        }
        return Math.min(index, count);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas == null) {
            return;
        }

        draw(canvas);
    }

    public void draw(Canvas canvas) {
        int x = 0;
        int y = 0;

        for (int i = FREE_PILE_BEGIN; i < HOME_PILE_END; ++i) {
            // drawFreePile(canvas, x, y, pileCards[i]);
            pileCards[i].draw(getContext(), canvas, x, y);
            x += cardWidth;
        }

        x = 0;
        y = 2 * cardHeight;
        for (int i = REGULAR_PILE_BEGIN; i < REGULAR_PILE_END; ++i) {
            pileCards[i].draw(getContext(), canvas, x, y);
            x += cardWidth;
        }

        movingPile.draw(getContext(), canvas, dragX - DISTANCE_FROM_FINGER_X, dragY - DISTANCE_FROM_FINGER_Y);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onTouchActionDown(event);
                break;

            case MotionEvent.ACTION_UP:
                onTouchActionUp(event);
                break;

            case MotionEvent.ACTION_MOVE:
                onTouchActionMove(event);
                break;

            // Fall through
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void onTouchActionDown(MotionEvent event) {
        dragX = (int) event.getX();
        dragY = (int) event.getY();

        int pileIndex = getTouchingPileIndex(event);
        PileCard touchingPile = pileCards[pileIndex];
        if (touchingPile instanceof HomePileCard) {
            return;
        }

        int cardIndex = getTouchingCardIndexOfPile(touchingPile, event);

        // Update dragging info
        dragPileIndex = pileIndex;
        dragCardIndex = cardIndex;

        if (cardIndex != -1) {
            if (touchingPile.isPartialPileMovable(cardIndex)) {
                movingPile = touchingPile.getPartialPile(cardIndex);
                touchingPile.setCutIndex(cardIndex);
            } else {
                resetDragging();
            }
        }
    }

    private void onTouchActionMove(MotionEvent event) {
        dragX = (int) event.getX();
        dragY = (int) event.getY();
    }

    private void onTouchActionUp(MotionEvent event) {
        if (movingPile.isEmpty()) {
            return;
        }

        int dropPileIndex = getTouchingPileIndex(event);
        PileCard fromPile = pileCards[dragPileIndex];
        PileCard toPile = pileCards[dropPileIndex];
        if (dragPileIndex != dropPileIndex && dragCardIndex != -1) {
            // Just add for now
            if (hasEnoughSpot(movingPile) && toPile.addPileCard(movingPile)) {
                fromPile.removeCardsFromEnd(movingPile.count());
                fireMoveListener(new Move(dragPileIndex, dropPileIndex, movingPile.count()));
            }
        }

        fromPile.resetCutIndex();
        resetDragging();
    }

    public void undoMove(Move move) {
        int from = move.getFromPile();
        int to = move.getToPile();
        PileCard fromPile = pileCards[from];
        PileCard toPile = pileCards[to];
        PileCard transfer = toPile.getPartialPile(toPile.count() - move.getNoCard());
        toPile.removeCardsFromEnd(move.getNoCard());
        fromPile.forceAdd(transfer);
    }

    private void fireMoveListener(final Move move) {
        if (moveListener != null) {
            moveListener.onMove(move);
        }
    }

    private void resetDragging() {
        movingPile.clear();
        dragCardIndex = -1;
        dragPileIndex = -1;
    }

    private boolean hasEnoughSpot(PileCard draggingPile) {
        return draggingPile.count() <= countFreeSpot();
    }

    private void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean hasHomeCellFinished() {
        for (int i = 4; i < 8; ++i) {
            HomePileCard pile = (HomePileCard) pileCards[i];
            if (!pile.isDone()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.d("GameView.surfaceCreated()");
        gameThread = new GameThread(getHolder(), FreecellView.this);
        gameThread.onResume();
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logger.d("GameView.surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logger.d("GameView.surfaceDestroyed()");
        boolean retry = false;
        gameThread.onPause();
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void onPause() {
        getHolder().removeCallback(this);
        if (gameThread != null) {
            gameThread.onPause();
        }
    }

    public void onResume() {
        if (gameThread != null) {
            gameThread.onResume();
        }
    }

    private class GameThread extends Thread {

        private SurfaceHolder surfaceHolder;

        private FreecellView freecellView;

        private volatile boolean running = false;

        public GameThread(SurfaceHolder surfaceHolder, FreecellView freecellView) {
            this.surfaceHolder = surfaceHolder;
            this.freecellView = freecellView;
        }

        public SurfaceHolder getSurfaceHolder() {
            return surfaceHolder;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (running) {
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    canvas.drawColor(Color.BLACK);
                    synchronized (surfaceHolder) {
                        freecellView.onDraw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public synchronized void onPause() {
            running = false;
        }

        public synchronized void onResume() {
            running = true;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        GameState state = new GameState(superState);
        state.pileCards = this.pileCards;
        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof GameState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        GameState gameState = (GameState) state;
        super.onRestoreInstanceState(gameState.getSuperState());
        this.pileCards = gameState.pileCards;
    }

    private static class GameState extends BaseSavedState {

        private PileCard[] pileCards;

        public GameState(Parcelable source) {
            super(source);
        }

        private GameState(Parcel in) {
            super(in);
            pileCards = (PileCard[]) in.readParcelableArray(PileCard.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelableArray(pileCards, flags);
        }
    }

    public static final Parcelable.Creator<GameState> CREATOR = new Parcelable.Creator<GameState>() {
        public GameState createFromParcel(Parcel in) {
            return new GameState(in);
        }

        public GameState[] newArray(int size) {
            return new GameState[size];
        }
    };
}
