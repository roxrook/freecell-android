package chan.android.game.freecell.model;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import chan.android.game.freecell.CardBitmapManager;

import java.util.List;

public class HomePileCard extends PileCard {

    private static final Paint PAINT = new Paint();

    static {
        PAINT.setAntiAlias(true);
        PAINT.setStyle(Paint.Style.STROKE);
        PAINT.setStrokeWidth(2.0f);
        PAINT.setColor(Color.BLUE);
    }

    public HomePileCard() {
        super();
    }

    public HomePileCard(Card card) {
        super();
        cardList.add(card);
    }

    public HomePileCard(List<Card> cards) {
        super(cards);
    }

    public boolean addPileCard(PileCard pile) {
        if (!pile.isEmpty() && pile.count() == 1 && canAdd(pile.getTopCard())) {
            cardList.add(pile.getTopCard());
            return true;
        }
        return false;
    }

    @Override
    public boolean canAdd(Card c) {
        if (isEmpty()) {
            return c.getRank() == Rank.ACE;
        }
        Card top = getBottomCard();
        return (top.getSuit() == c.getSuit() && top.getRank().getValue() + 1 == c.getRank().getValue());
    }

    public boolean isDone() {
        return (!isEmpty() && getBottomCard().getRank().equals(Rank.KING));
    }

    public void clear() {
        cardList.clear();
    }

    @Override
    public void draw(Context context, Canvas canvas, int x, int y) {
        CardBitmapManager cbm = CardBitmapManager.getBitmapManager(context, cardWidth, cardHeight);
        if (isEmpty()) {
            canvas.drawBitmap(cbm.getPlaceHolderBitmap(), x, y, null);
        } else {
            canvas.drawBitmap(cbm.getBitmap(getBottomCard()), x, y, null);
        }
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, PAINT);
    }
}



