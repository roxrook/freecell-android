package chan.android.game.freecell.model.highscore;


import android.content.ContentValues;
import android.content.Context;
import chan.android.game.freecell.GameManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HighScoreManager implements HighScoreDataSource {

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Context appContext;

    public HighScoreManager(Context context) {
        appContext = context.getApplicationContext();
    }

    @Override
    public void insertNewScore(int score) {
        ContentValues cv = new ContentValues();
        cv.put(HighScoreDbTable.COLUMN_SCORE, score);
        Date now = new Date();
        updateGameManager(now);
        cv.put(HighScoreDbTable.COLUMN_DATE, DATE_FORMATTER.format(now));
        appContext.getContentResolver().insert(HighScoreContentProvider.CONTENT_URI, cv);
    }

    private void updateGameManager(final Date date) {
        GameManager gameManager = GameManager.getGameManager(appContext);
        gameManager.setNewestScoreDate(date);
    }
}
