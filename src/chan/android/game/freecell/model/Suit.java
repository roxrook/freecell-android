package chan.android.game.freecell.model;

public enum Suit {

    HEARTS("hearts") {
        public boolean isOpposite(Suit other) {
            return other == SPADES || other == CLUBS;
        }
    },

    DIAMONDS("diamonds") {
        @Override
        public boolean isOpposite(Suit other) {
            return other == SPADES || other == CLUBS;
        }
    },

    SPADES("spades") {
        @Override
        public boolean isOpposite(Suit other) {
            return other == HEARTS || other == DIAMONDS;
        }
    },

    CLUBS("clubs") {
        @Override
        public boolean isOpposite(Suit other) {
            return other == HEARTS || other == DIAMONDS;
        }
    };

    final String name;

    public String getName() {
        return name;
    }

    Suit(String name) {
        this.name = name;
    }

    public boolean isOpposite(Suit other) {
        return false;
    }
}
