package chan.android.game.freecell.model;

import android.content.Context;
import android.graphics.Canvas;
import chan.android.game.freecell.CardBitmapManager;

public class FreePileCard extends PileCard {

    public FreePileCard() {
        super();
    }

    @Override
    public boolean canAdd(Card c) {
        return isEmpty();
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
            if (cutIndex == 1) {
                canvas.drawBitmap(cbm.getBitmap(getTopCard()), x, y, null);
            } else {
                canvas.drawBitmap(cbm.getPlaceHolderBitmap(), x, y, null);
            }
        }
    }
}
