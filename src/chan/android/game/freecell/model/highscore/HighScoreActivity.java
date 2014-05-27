package chan.android.game.freecell.model.highscore;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import chan.android.game.freecell.GameManager;
import chan.android.game.freecell.R;
import chan.android.game.freecell.model.CongratsDialogFragment;
import chan.android.game.freecell.view.RoundedRectListView;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HighScoreActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private HighScoreCursorAdapter cursorAdapter;

    private String newestScore;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);

        // Set up list view
        RoundedRectListView listview = (RoundedRectListView) findViewById(R.id.highscore_$_listview);
        View header = getLayoutInflater().inflate(R.layout.highscore_header, null);
        listview.addHeaderView(header);

        // Prepare loader
        getLoaderManager().initLoader(0, null, this);
        cursorAdapter = new HighScoreCursorAdapter(this);
        listview.setAdapter(cursorAdapter);
        newestScore = getNewestScore();

        handler = new Handler(Looper.getMainLooper());
    }

    private String getNewestScore() {
        GameManager gameManager = GameManager.getGameManager(this);
        Date newestDate = gameManager.getNewestScoreDate();
        String newestScore = "";
        if (newestDate != null) {
            newestScore = DATE_FORMATTER.format(newestDate);
        }
        return newestScore;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] projection = {
                HighScoreDbTable.COLUMN_ID,
                HighScoreDbTable.COLUMN_SCORE,
                HighScoreDbTable.COLUMN_DATE
        };
        CursorLoader loader = new CursorLoader(this, HighScoreContentProvider.CONTENT_URI, projection, null, null, "score DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        checkIfHighestScore(data);
        cursorAdapter.swapCursor(data);
    }

    private void checkIfHighestScore(Cursor cursor) {
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            final String date = cursor.getString(cursor.getColumnIndexOrThrow(HighScoreDbTable.COLUMN_DATE));
            final int score = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreDbTable.COLUMN_SCORE));
            if (date.compareTo(newestScore) == 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showCongratsDialog(score);
                    }
                });
            }
        }
    }

    public void showCongratsDialog(final int score) {
        final CongratsDialogFragment d = new CongratsDialogFragment();
        d.setOnClickListener(new CongratsDialogFragment.OnDialogClickListener() {
            @Override
            public void onLeftButtonClick() {
                d.dismiss();
            }

            @Override
            public void onRightButtonClick() {
                shareHighScore(score);
                d.dismiss();
            }
        });
        d.show(getFragmentManager(), "congrats_dialog");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    static class ViewHolder {
        TextView score;
        TextView date;

        public ViewHolder(View v) {
            score = (TextView) v.findViewById(R.id.highscore_row_$_score);
            date = (TextView) v.findViewById(R.id.highscore_row_$_date);
        }
    }

    class HighScoreCursorAdapter extends CursorAdapter {

        private LayoutInflater inflater;
        private String newestScore = "";

        public HighScoreCursorAdapter(Context context) {
            super(context, null, false);
            inflater = LayoutInflater.from(context);
            GameManager gameManager = GameManager.getGameManager(context);
            Date newest = gameManager.getNewestScoreDate();
            if (newest != null) {
                newestScore = DATE_FORMATTER.format(newest);
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = inflater.inflate(R.layout.highscore_row, parent, false);
            ViewHolder vh = new ViewHolder(view);
            view.setTag(vh);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder vh = (ViewHolder) view.getTag();
            final int score = cursor.getInt(cursor.getColumnIndexOrThrow(HighScoreDbTable.COLUMN_SCORE));
            final String date = cursor.getString(cursor.getColumnIndexOrThrow(HighScoreDbTable.COLUMN_DATE));
            if (date.compareTo(newestScore) == 0) {
                view.setBackgroundColor(Color.YELLOW);
            } else {
                view.setBackgroundColor(Color.WHITE);
            }
            vh.score.setText(Integer.toString(score));
            vh.date.setText(readableTimeStamp(date));
        }
    }

    private String readableTimeStamp(String date) {
        try {
            Date before = DATE_FORMATTER.parse(date);
            Date now = new Date();
            long duration = now.getTime() - before.getTime();

            long days = TimeUnit.MILLISECONDS.toDays(duration);
            if (days > 0) {
                return days + " days ago";
            }

            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            if (hours > 0) {
                return hours + " hours ago";
            }

            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if (minutes > 0) {
                return minutes + " minutes ago";
            }

            return "just now";
        } catch (ParseException e) {
            // Should never happen
        }
        return "";
    }

    public void shareHighScore(int score) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Your new score " + Integer.toString(score) + "!");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Freecell New Highscore");
        startActivity(Intent.createChooser(shareIntent, "Share..."));
    }
}
