package chan.android.game.freecell.model;


import android.os.Parcel;
import android.os.Parcelable;
import chan.android.game.freecell.R;

import java.util.Comparator;

public class Card implements Comparator<Card>, Parcelable {

    public static final int[][] CARD_DRAWABLE_IDS = new int[13][4];

    static {
        // ACE
        CARD_DRAWABLE_IDS[Rank.ACE.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c1;
        CARD_DRAWABLE_IDS[Rank.ACE.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h1;
        CARD_DRAWABLE_IDS[Rank.ACE.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s1;
        CARD_DRAWABLE_IDS[Rank.ACE.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d1;

        // TWO
        CARD_DRAWABLE_IDS[Rank.TWO.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c2;
        CARD_DRAWABLE_IDS[Rank.TWO.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h2;
        CARD_DRAWABLE_IDS[Rank.TWO.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s2;
        CARD_DRAWABLE_IDS[Rank.TWO.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d2;

        // THREE
        CARD_DRAWABLE_IDS[Rank.THREE.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c3;
        CARD_DRAWABLE_IDS[Rank.THREE.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h3;
        CARD_DRAWABLE_IDS[Rank.THREE.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s3;
        CARD_DRAWABLE_IDS[Rank.THREE.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d3;

        // FOUR
        CARD_DRAWABLE_IDS[Rank.FOUR.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c4;
        CARD_DRAWABLE_IDS[Rank.FOUR.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h4;
        CARD_DRAWABLE_IDS[Rank.FOUR.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s4;
        CARD_DRAWABLE_IDS[Rank.FOUR.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d4;

        // FIVE
        CARD_DRAWABLE_IDS[Rank.FIVE.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c5;
        CARD_DRAWABLE_IDS[Rank.FIVE.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h5;
        CARD_DRAWABLE_IDS[Rank.FIVE.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s5;
        CARD_DRAWABLE_IDS[Rank.FIVE.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d5;

        // SIX
        CARD_DRAWABLE_IDS[Rank.SIX.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c6;
        CARD_DRAWABLE_IDS[Rank.SIX.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h6;
        CARD_DRAWABLE_IDS[Rank.SIX.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s6;
        CARD_DRAWABLE_IDS[Rank.SIX.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d6;

        // SEVEN
        CARD_DRAWABLE_IDS[Rank.SEVEN.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c7;
        CARD_DRAWABLE_IDS[Rank.SEVEN.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h7;
        CARD_DRAWABLE_IDS[Rank.SEVEN.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s7;
        CARD_DRAWABLE_IDS[Rank.SEVEN.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d7;

        // EIGHT
        CARD_DRAWABLE_IDS[Rank.EIGHT.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c8;
        CARD_DRAWABLE_IDS[Rank.EIGHT.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h8;
        CARD_DRAWABLE_IDS[Rank.EIGHT.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s8;
        CARD_DRAWABLE_IDS[Rank.EIGHT.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d8;

        // NINE
        CARD_DRAWABLE_IDS[Rank.NINE.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c9;
        CARD_DRAWABLE_IDS[Rank.NINE.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h9;
        CARD_DRAWABLE_IDS[Rank.NINE.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s9;
        CARD_DRAWABLE_IDS[Rank.NINE.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d9;

        // TEN
        CARD_DRAWABLE_IDS[Rank.TEN.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.c10;
        CARD_DRAWABLE_IDS[Rank.TEN.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.h10;
        CARD_DRAWABLE_IDS[Rank.TEN.ordinal()][Suit.SPADES.ordinal()] = R.drawable.s10;
        CARD_DRAWABLE_IDS[Rank.TEN.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.d10;

        // JACK
        CARD_DRAWABLE_IDS[Rank.JACK.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.cj;
        CARD_DRAWABLE_IDS[Rank.JACK.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.hj;
        CARD_DRAWABLE_IDS[Rank.JACK.ordinal()][Suit.SPADES.ordinal()] = R.drawable.sj;
        CARD_DRAWABLE_IDS[Rank.JACK.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.dj;

        // QUEEN
        CARD_DRAWABLE_IDS[Rank.QUEEN.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.cq;
        CARD_DRAWABLE_IDS[Rank.QUEEN.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.hq;
        CARD_DRAWABLE_IDS[Rank.QUEEN.ordinal()][Suit.SPADES.ordinal()] = R.drawable.sq;
        CARD_DRAWABLE_IDS[Rank.QUEEN.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.dq;

        // KING
        CARD_DRAWABLE_IDS[Rank.KING.ordinal()][Suit.CLUBS.ordinal()] = R.drawable.ck;
        CARD_DRAWABLE_IDS[Rank.KING.ordinal()][Suit.HEARTS.ordinal()] = R.drawable.hk;
        CARD_DRAWABLE_IDS[Rank.KING.ordinal()][Suit.SPADES.ordinal()] = R.drawable.sk;
        CARD_DRAWABLE_IDS[Rank.KING.ordinal()][Suit.DIAMONDS.ordinal()] = R.drawable.dk;
    }

    public static final int BLACK_JOKER_ID = R.drawable.jb;

    public static final int RED_JOKER_ID = R.drawable.jr;

    public static final int PLACE_HOLDER_ID = R.drawable.placeholder;

    public static final int ACE_PLACE_HOLDER_ID = R.drawable.ace_place_holder;

    private Rank rank;

    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public boolean isOppositeSuit(Card other) {
        return suit.isOpposite(other.getSuit());
    }

    public boolean isLessThanOne(Card other) {
        int value = rank.getValue();
        // There is no card that less than 2
        if (value == 2) {
            return false;
        }
        // This is an ACE, so 'other' must be a KING
        else if (value == 1) {
            return other.getRank() == Rank.KING;
        }

        // The rest is just subtraction
        return (value - other.getRank().getValue()) == 1;
    }

    public int getResourceId() {
        return CARD_DRAWABLE_IDS[rank.ordinal()][suit.ordinal()];
    }

    @Override
    public String toString() {
        return rank.name() + " of " + suit.name();
    }

    @Override
    public int compare(Card lhs, Card rhs) {
        if (lhs.rank.getValue() < rhs.rank.getValue()) {
            return (lhs.rank.getValue() == 1) ? 1 : -1;
        } else if (lhs.rank.getValue() > rhs.rank.getValue()) {
            return (rhs.rank.getValue() == 1) ? -1 : 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (rank != card.rank) return false;
        if (suit != card.suit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rank.hashCode();
        result = 31 * result + suit.hashCode();
        return result;
    }

    /**
     * Constructor for parcelable
     *
     * @param in
     */
    public Card(Parcel in) {
        rank = Rank.valueOf(in.readString());
        suit = Suit.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(rank.name());
        out.writeString(suit.name());
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
