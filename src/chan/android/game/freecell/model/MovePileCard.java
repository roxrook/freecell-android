package chan.android.game.freecell.model;


import android.content.Context;
import android.graphics.Canvas;
import chan.android.game.freecell.CardBitmapManager;

import java.util.List;

public class MovePileCard extends PileCard {

    public MovePileCard(List<Card> cards) {
        super(cards);
    }

    public MovePileCard(Card card) {
        super(card);
    }

    public MovePileCard() {
        super();
    }

    @Override
    public void draw(Context context, Canvas canvas, int x, int y) {
        if (!isEmpty()) {
            CardBitmapManager cbm = CardBitmapManager.getBitmapManager(context, cardWidth, cardHeight);
            int top = y;
            for (int i = 0; i < cutIndex; ++i) {
                canvas.drawBitmap(cbm.getBitmap(cardList.get(i)), x, top, null);
                top += verticalShift;
            }
        }
    }
}
