package chan.android.game.freecell.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    public static final int MAX_CARD = 52;

    public List<Card> cards;

    private int cardLeft;

    public Deck() {
        cards = new ArrayList<Card>(MAX_CARD);
        Rank[] rankEnums = Rank.values();
        Suit[] suitEnums = Suit.values();
        int ranks = rankEnums.length;
        int suits = suitEnums.length;
        for (int i = 0; i < ranks; ++i) {
            for (int j = 0; j < suits; ++j) {
                cards.add(new Card(rankEnums[i], suitEnums[j]));
            }
        }

        cardLeft = MAX_CARD;
    }

    public ArrayList<Card> getCards() {
        return new ArrayList<Card>(cards);
    }

    public int getCardLeft() {
        return cardLeft;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public ArrayList<Card> deal(int howMany) {
        if (cardLeft == 0) {
            return new ArrayList<Card>();
        }
        ArrayList<Card> dealCards = new ArrayList<Card>();
        int noDeal = Math.min(howMany, cardLeft);
        for (int i = 0; i < noDeal; ++i) {
            dealCards.add(cards.remove(0));
        }
        cardLeft -= noDeal;
        return dealCards;
    }
}
