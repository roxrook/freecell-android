package chan.android.game.freecell.model;


public class Move {

    private int fromPile;

    private int toPile;

    private int noCard;

    public Move(int fromPile, int toPile, int noCard) {
        this.fromPile = fromPile;
        this.toPile = toPile;
        this.noCard = noCard;
    }

    public int getNoCard() {
        return noCard;
    }

    public void setNoCard(int noCard) {
        this.noCard = noCard;
    }

    public int getFromPile() {
        return fromPile;
    }

    public void setFromPile(int fromPile) {
        this.fromPile = fromPile;
    }

    public int getToPile() {
        return toPile;
    }

    public void setToPile(int toPile) {
        this.toPile = toPile;
    }

}
