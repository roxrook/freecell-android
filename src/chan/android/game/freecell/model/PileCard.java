package chan.android.game.freecell.model;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import chan.android.game.freecell.CardBitmapManager;
import chan.android.game.freecell.util.Precondition;

import java.util.ArrayList;
import java.util.List;

public class PileCard implements Drawable, Parcelable {

    protected static int cardWidth = 0;

    protected static int cardHeight = 0;

    protected static int verticalShift = 0;

    public static void setDrawingInfo(int width, int height, int shift) {
        cardWidth = width;
        cardHeight = height;
        verticalShift = shift;
    }

    protected List<Card> cardList;

    protected volatile int cutIndex;

    public PileCard(List<Card> cards) {
        cardList = cards;
        cutIndex = cards.size();
    }

    public PileCard(Card c) {
        this();
        cardList.add(c);
    }

    public PileCard() {
        cardList = new ArrayList<Card>();
    }

    public boolean isEmpty() {
        return cardList.isEmpty();
    }

    public boolean isPartialPileMovable(int index) {
        if (index < 0 || index >= cardList.size()) {
            return false;
        }
        return hasAllCardsInOrdered(index);
    }

    public PileCard getPartialPile(int index) {
        Precondition.checkCondition(index >= 0 && index < count(), "getPartialPile(" + index + ") is out of range");
        List<Card> cards = new ArrayList<Card>();
        final int count = cardList.size();
        for (int i = index; i < count; ++i) {
            cards.add(cardList.get(i));
        }
        return new MovePileCard(cards);
    }

    public void removeCardsFromEnd(int howMany) {
        if (howMany > cardList.size()) {
            return;
        }
        cardList.subList(cardList.size() - howMany, cardList.size()).clear();
        setCutIndex(cardList.size());
    }

    private boolean hasAllCardsInOrdered(int index) {
        final int count = cardList.size();
        // If we have only 1 card, return true
        if (index == count - 1) {
            return true;
        }
        for (int i = index; i < count - 1; ++i) {
            Card before = cardList.get(i);
            Card after = cardList.get(i + 1);
            if (!(before.isLessThanOne(after) && before.isOppositeSuit(after))) {
                return false;
            }
        }
        return true;
    }

    public boolean addCard(Card card) {
        if (canAdd(card)) {
            cardList.add(card);
            return true;
        }
        return false;
    }

    public int count() {
        return cardList.size();
    }

    public boolean addPileCard(PileCard pile) {
        if (!pile.isEmpty() && canAdd(pile.getTopCard())) {
            ArrayList<Card> cards = pile.getCards();
            for (Card c : cards) {
                cardList.add(c);
            }
            resetCutIndex();
            return true;
        }
        return false;
    }

    public void clear() {
        cardList.clear();
    }

    public Card getBottomCard() {
        return cardList.get(cardList.size() - 1);
    }

    public ArrayList getCards() {
        return new ArrayList(cardList);
    }

    public Card getTopCard() {
        return cardList.get(0);
    }

    public boolean canAdd(Card card) {
        if (isEmpty()) {
            return true;
        }
        Card top = getBottomCard();
        return (top.isLessThanOne(card) && top.isOppositeSuit(card));
    }

    /**
     * Force add card and ignore rule. Only use this method for
     * undo
     *
     * @param pile
     */
    public void forceAdd(PileCard pile) {
        ArrayList<Card> cards = pile.getCards();
        for (Card c : cards) {
            cardList.add(c);
        }
        resetCutIndex();
    }

    public void resetCutIndex() {
        setCutIndex(cardList.size());
    }

    public void setCutIndex(int index) {
        Precondition.checkCondition(index >= 0 && index <= count(), "setCutIndex(" + index + ") is out of range");
        cutIndex = index;
    }

    @Override
    public void draw(Context context, Canvas canvas, int x, int y) {
        CardBitmapManager cbm = CardBitmapManager.getBitmapManager(context, cardWidth, cardHeight);
        if (!isEmpty()) {
            int top = y;
            for (int i = 0; i < cutIndex; ++i) {
                canvas.drawBitmap(cbm.getBitmap(cardList.get(i)), x, top, null);
                top += verticalShift;
            }
        } else {
            canvas.drawBitmap(cbm.getPlaceHolderBitmap(), x, y, null);
        }
    }

    public PileCard(Parcel in) {
        cardList = new ArrayList<Card>();
        in.readList(cardList, ((Object) this).getClass().getClassLoader());
    }

    public static final Creator<PileCard> CREATOR = new Creator<PileCard>() {
        public PileCard createFromParcel(Parcel in) {
            return new PileCard(in);
        }

        public PileCard[] newArray(int size) {
            return new PileCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(cardList);
    }
}
