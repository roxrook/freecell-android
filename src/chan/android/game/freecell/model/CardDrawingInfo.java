package chan.android.game.freecell.model;


public class CardDrawingInfo {

    private int width = 0;

    private int height = 0;

    private int verticalShift = 0;

    public CardDrawingInfo(int width, int height, int verticalShift) {
        this.width = width;
        this.height = height;
        this.verticalShift = verticalShift;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVerticalShift() {
        return verticalShift;
    }
}
