package chan.android.game.freecell.model.scoring;

public interface ComputeScoreStrategy {

    public int compute(int moves, long millis);
}
