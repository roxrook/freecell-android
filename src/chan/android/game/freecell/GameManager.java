package chan.android.game.freecell;


import android.content.Context;

import java.util.Date;

public class GameManager {

    private static final Object lock = new Object();

    private static GameManager gameManager;

    private Context appContext;

    private Date newestScoreDate;

    private GameManager(Context context) {
        appContext = context.getApplicationContext();
    }

    public static GameManager getGameManager(Context context) {
        if (gameManager == null) {
            synchronized (lock) {
                if (gameManager == null) {
                    gameManager = new GameManager(context);
                }
            }
        }
        return gameManager;
    }

    public synchronized void setNewestScoreDate(final Date date) {
        synchronized (lock) {
            newestScoreDate = date;
        }
    }

    public synchronized Date getNewestScoreDate() {
        return newestScoreDate;
    }
}
