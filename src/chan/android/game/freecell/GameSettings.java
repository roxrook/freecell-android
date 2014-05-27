package chan.android.game.freecell;

import android.content.Context;
import android.content.SharedPreferences;

public class GameSettings {

    enum Key {
        ENABLE_SOUND,
        ENABLE_AUTO_PLAY,
        ENABLE_CLOCK,
        BACKGROUND_DRAWABLE,
    }

    private static SharedPreferences prefs;

    public static void initialize(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("chan.android.game.freecell.prefs_", Context.MODE_PRIVATE);
        }
    }

    public static void setBackgroundDrawableId(int resourceId) {
        prefs.edit().putInt(Key.BACKGROUND_DRAWABLE.name(), resourceId).commit();
    }

    public static int getBackgroundDrawableId() {
        return prefs.getInt(Key.BACKGROUND_DRAWABLE.name(), R.drawable.shadow_main_screen);
    }

    public static void enableSound(boolean yesOrNo) {
        prefs.edit().putBoolean(Key.ENABLE_SOUND.name(), yesOrNo).commit();
    }

    public static void enableClock(boolean yesOrNo) {
        prefs.edit().putBoolean(Key.ENABLE_CLOCK.name(), yesOrNo).commit();
    }

    public static void enableAutoPlay(boolean yesOrNo) {
        prefs.edit().putBoolean(Key.ENABLE_AUTO_PLAY.name(), yesOrNo).commit();
    }

    public static boolean isSoundEnabled() {
        return prefs.getBoolean(Key.ENABLE_SOUND.name(), false);
    }

    public static boolean isClockEnabled() {
        return prefs.getBoolean(Key.ENABLE_CLOCK.name(), false);
    }

    public static boolean isAutoPlayEnabled() {
        return prefs.getBoolean(Key.ENABLE_AUTO_PLAY.name(), false);
    }
}
