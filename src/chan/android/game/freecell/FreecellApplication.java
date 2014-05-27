package chan.android.game.freecell;

import android.app.Application;

public class FreecellApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GameSettings.initialize(this);
    }
}
