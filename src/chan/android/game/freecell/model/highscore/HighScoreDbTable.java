package chan.android.game.freecell.model.highscore;

import android.database.sqlite.SQLiteDatabase;

class HighScoreDbTable {

    public static final String TABLE_NAME = "highscore";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_DATE = "date";

    private static final String QUERY_CREATE = "create table "
            + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SCORE + " integer not null, "
            + COLUMN_DATE + " text not null"
            + ");";

    private static final String QUERY_DROP = "drop table if exists " + TABLE_NAME;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(QUERY_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(QUERY_DROP);
        onCreate(database);
    }
}
