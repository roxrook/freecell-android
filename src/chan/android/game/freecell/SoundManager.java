package chan.android.game.freecell;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public final class SoundManager {

    private int snap;

    private int shuffle;

    private SoundPool soundPool;

    private AudioManager audioManager;

    private float volume;

    private boolean off = false;

    public SoundManager(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        snap = soundPool.load(context, R.raw.snap, 1);
        shuffle = soundPool.load(context, R.raw.shuffle, 1);
    }

    public void turnOff() {
        off = true;
    }

    public void turnOn() {
        off = false;
    }

    public void playCardSnap() {
        if (!off) {
            soundPool.play(snap, volume, volume, 1, 0, 1.0f);
        }
    }

    public void playDeckShuffle() {
        if (!off) {
            soundPool.play(shuffle, volume, volume, 1, 0, 1.0f);
        }
    }
}
