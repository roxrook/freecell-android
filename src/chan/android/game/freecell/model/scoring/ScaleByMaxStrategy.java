package chan.android.game.freecell.model.scoring;


import java.util.concurrent.TimeUnit;

public class ScaleByMaxStrategy implements ComputeScoreStrategy {

    private static final int MAX_SCORE = 10000;
    private static final int MIN_MOVE = 52;
    private static final int PENALTY_THRESHOLD_TIME = 10;

    @Override
    public int compute(int moves, long millis) {
        int score = (MAX_SCORE * MIN_MOVE) / moves;
        int minutes = (int) TimeUnit.MINUTES.MILLISECONDS.toMinutes(millis);
        if (minutes > PENALTY_THRESHOLD_TIME) {
            // Five points penalty for each minute
            score -= (minutes - PENALTY_THRESHOLD_TIME) * 5;
        }
        return score;
    }
}
