package chan.android.game.freecell.model.highscore;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class HighScoreDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "highscore.db";

    private static final int DATABASE_VERSION = 1;

    public HighScoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        HighScoreDbTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        HighScoreDbTable.onUpgrade(db, oldVersion, newVersion);
    }
}
