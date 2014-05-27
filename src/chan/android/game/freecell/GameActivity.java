package chan.android.game.freecell;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import chan.android.game.freecell.model.Move;
import chan.android.game.freecell.model.highscore.HighScoreActivity;
import chan.android.game.freecell.model.highscore.HighScoreDataSource;
import chan.android.game.freecell.model.highscore.HighScoreManager;
import chan.android.game.freecell.model.scoring.ComputeScoreStrategy;
import chan.android.game.freecell.model.scoring.ScaleByMaxStrategy;
import chan.android.game.freecell.view.FreecellView;

import java.lang.ref.WeakReference;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GameActivity extends Activity {

    private static final String TEXT_MOVES = "Moves:  ";

    private static final String TEXT_SCORE = "Scores:  ";

    private static final String TEXT_TIME = "Time elapsed:  ";

    private Button buttonUndo;

    private FreecellView freecellView;

    private TextView textViewTimeElapsed;

    private TextView textViewMoveCount;

    private TextView textViewScore;

    private Timer timer;

    private SoundManager soundManager;

    private Stack<Move> moveStack;

    private long gameStartTime = 0;

    private long gameEndTime = 0;

    private HighScoreDataSource highscoreManager;

    private ComputeScoreStrategy scoreComputer;

    private ViewFlipper viewFlipper;

    private TextView textViewResume;

    private TextView textViewNewGame;

    private TextView textViewHighscore;

    private TextView textViewSettings;

    private LinearLayout linearLayoutBackground;

    private boolean isFirstTime = true;

    @Override
    public void onPause() {
        super.onPause();
        freecellView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSettings();
    }

    private void updateSettings() {
        if (GameSettings.isSoundEnabled()) {
            soundManager.turnOn();
        } else {
            soundManager.turnOff();
        }

        if (GameSettings.isClockEnabled()) {
            textViewTimeElapsed.setVisibility(View.VISIBLE);
        } else {
            textViewTimeElapsed.setVisibility(View.GONE);
        }

        linearLayoutBackground.setBackgroundResource(GameSettings.getBackgroundDrawableId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        viewFlipper = (ViewFlipper) findViewById(R.id.game_$_viewflipper);
        setUpGameView();
        setUpHomeView();
    }

    private void setUpGameView() {

        // For sound
        soundManager = new SoundManager(this);

        // For High score
        highscoreManager = new HighScoreManager(this);

        scoreComputer = new ScaleByMaxStrategy();

        // For keep track moves
        moveStack = new Stack<Move>();

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        linearLayoutBackground = (LinearLayout) findViewById(R.id.game_$_linearlayout_background);

        freecellView = (FreecellView) findViewById(R.id.main_$_gameview);
        freecellView.setMoveListener(new FreecellView.OnMoveListener() {
            @Override
            public void onMove(Move move) {
                if (isGameOver()) {
                    gameEndTime = System.currentTimeMillis();
                    int moves = moveStack.size();
                    long totalTime = gameEndTime - gameStartTime;
                    int score = scoreComputer.compute(moves, totalTime);
                    insertNewScore(score);
                    launch(HighScoreActivity.class);
                } else {
                    moveStack.push(move);
                    if (GameSettings.isAutoPlayEnabled()) {
                        freecellView.autoMove();
                    }
                    textViewMoveCount.setText(TEXT_MOVES + moveStack.size());
                    soundManager.playCardSnap();
                }
            }
        });

        buttonUndo = (Button) findViewById(R.id.main_$_button_undo);
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUndoClick();
            }
        });

        textViewTimeElapsed = (TextView) findViewById(R.id.main_$_textview_time_elapsed);
        textViewMoveCount = (TextView) findViewById(R.id.main_$_textview_moves);
        textViewMoveCount.setText(TEXT_MOVES + moveStack.size());

        textViewScore = (TextView) findViewById(R.id.main_$_textview_score);

        // Prepare ad

    }

    private void setUpHomeView() {
        textViewNewGame = (TextView) findViewById(R.id.home_$_textview_newgame);
        textViewNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirstTime) {
                    NewGameTask task = new NewGameTask(GameActivity.this);
                    task.execute();
                } else {
                    startNewGame();
                    viewFlipper.setDisplayedChild(1);
                }
            }
        });

        textViewResume = (TextView) findViewById(R.id.home_$_textview_continue);
        textViewResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
            }
        });
        shouldDisplayResume();

        textViewHighscore = (TextView) findViewById(R.id.home_$_textview_highscore);
        textViewHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch(HighScoreActivity.class);
            }
        });

        textViewSettings = (TextView) findViewById(R.id.home_$_textview_settings);
        textViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch(SettingsActivity.class);
            }
        });
    }

    private void shouldDisplayResume() {
        if (isFirstTime) {
            textViewResume.setVisibility(View.GONE);
        } else {
            textViewResume.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewFlipper.getDisplayedChild() == 1) {
            viewFlipper.setDisplayedChild(0);
            shouldDisplayResume();
        } else {
            super.onBackPressed();
        }
    }

    private void launch(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }

    public void startNewGame() {
        isFirstTime = false;
        moveStack.clear();
        textViewMoveCount.setText(TEXT_MOVES + moveStack.size());
        textViewScore.setText(TEXT_SCORE + "0");
        freecellView.drawNewDeck();
        freecellView.invalidate();
        startNewTimer();
        soundManager.playDeckShuffle();
    }

    private void onUndoClick() {
        if (!moveStack.isEmpty()) {
            Move move = moveStack.pop();
            textViewMoveCount.setText(TEXT_MOVES + moveStack.size());
            freecellView.undoMove(move);
            freecellView.invalidate();
            soundManager.playCardSnap();
        }
    }

    private void insertNewScore(int score) {
        highscoreManager.insertNewScore(score);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isGameOver() {
        return freecellView.hasHomeCellFinished();
    }

    private void startNewTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new UpdateTimeTask(System.currentTimeMillis()), 10, 1000);
        gameStartTime = System.currentTimeMillis();
    }

    class UpdateTimeTask extends TimerTask {

        private long startTime;

        private int counter;

        public UpdateTimeTask(long startTime) {
            this.startTime = startTime;
            this.counter = 0;
        }

        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            final int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
            counter++;

            if (counter > 60) {
                counter = 0;
            }

            post(new Runnable() {
                @Override
                public void run() {
                    textViewTimeElapsed.setText(TEXT_TIME + String.format("%02d:%02d", minutes, counter));
                }
            });
        }
    }

    private void post(Runnable runnable) {
        runOnUiThread(runnable);
    }

    private static class NewGameTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<GameActivity> gameActivity;

        private ProgressDialog pd;

        public NewGameTask(GameActivity a) {
            gameActivity = new WeakReference<GameActivity>(a);
            pd = new ProgressDialog(gameActivity.get());
            pd.setMessage("Loading new game...");
            pd.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            gameActivity.get().startNewGame();
            gameActivity.get().isFirstTime = false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            gameActivity.get().viewFlipper.setDisplayedChild(1);
        }
    }
}
