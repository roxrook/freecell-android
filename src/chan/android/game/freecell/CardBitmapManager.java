package chan.android.game.freecell;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import chan.android.game.freecell.model.Card;
import chan.android.game.freecell.model.Rank;
import chan.android.game.freecell.model.Suit;

import java.util.HashMap;

public class CardBitmapManager {

    private HashMap<Card, Bitmap> cardBitmapMapper;

    private Bitmap placeHolder;

    private Bitmap acePlaceHolder;

    private static CardBitmapManager instance;

    public static CardBitmapManager getBitmapManager(Context context, int width, int height) {
        if (instance == null) {
            instance = new CardBitmapManager(context, width, height);
        }
        return instance;
    }

    private CardBitmapManager(Context context, int width, int height) {
        Rank[] rankEnums = Rank.values();
        Suit[] suitEnums = Suit.values();
        int ranks = rankEnums.length;
        int suits = suitEnums.length;
        cardBitmapMapper = new HashMap<Card, Bitmap>();
        for (int i = 0; i < ranks; ++i) {
            for (int j = 0; j < suits; ++j) {
                Bitmap src = BitmapFactory.decodeResource(context.getResources(), Card.CARD_DRAWABLE_IDS[i][j]);
                cardBitmapMapper.put(new Card(rankEnums[i], suitEnums[j]), Bitmap.createScaledBitmap(src, width, height, true));
                src.recycle();
            }
        }

        Bitmap src = BitmapFactory.decodeResource(context.getResources(), Card.PLACE_HOLDER_ID);
        placeHolder = Bitmap.createScaledBitmap(src, width, height, true);
        src.recycle();

        src = BitmapFactory.decodeResource(context.getResources(), Card.ACE_PLACE_HOLDER_ID);
        acePlaceHolder = Bitmap.createScaledBitmap(src, width, height, true);
        src.recycle();
    }

    public Bitmap getBitmap(Card c) {
        return cardBitmapMapper.get(c);
    }

    public Bitmap getPlaceHolderBitmap() {
        return placeHolder;
    }

    public Bitmap getAcePlaceHolderBitmap() {
        return acePlaceHolder;
    }
}
