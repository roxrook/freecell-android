package chan.android.game.freecell.model.highscore;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class HighScoreContentProvider extends ContentProvider {

    private static final int HIGH_SCORES = 10;

    private static final int HIGH_SCORES_ID = 20;

    private static final String AUTHORITY = "chan.android.game.freecell";

    private static final String BASE_PATH = "highscores";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/highscores";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/highscore";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private HighScoreDbHelper dbHelper;

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, HIGH_SCORES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", HIGH_SCORES_ID);
    }

    static final String[] COLUMNS = {HighScoreDbTable.COLUMN_ID, HighScoreDbTable.COLUMN_SCORE, HighScoreDbTable.COLUMN_DATE};

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> request = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> available = new HashSet<String>(Arrays.asList(COLUMNS));
            if (!available.containsAll(request)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new HighScoreDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(HighScoreDbTable.TABLE_NAME);
        int type = URI_MATCHER.match(uri);
        switch (type) {
            case HIGH_SCORES:
                break;

            case HIGH_SCORES_ID:
                queryBuilder.appendWhere(HighScoreDbTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (type) {
            case HIGH_SCORES:
                id = db.insert(HighScoreDbTable.TABLE_NAME, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case HIGH_SCORES:
                affectedRows = db.delete(HighScoreDbTable.TABLE_NAME, selection, selectionArgs);
                break;

            case HIGH_SCORES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.delete(HighScoreDbTable.TABLE_NAME, HighScoreDbTable.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.delete(HighScoreDbTable.TABLE_NAME, HighScoreDbTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type) {
            case HIGH_SCORES:
                affectedRows = db.update(HighScoreDbTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case HIGH_SCORES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(HighScoreDbTable.TABLE_NAME, values, HighScoreDbTable.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.update(HighScoreDbTable.TABLE_NAME, values, HighScoreDbTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }
}


