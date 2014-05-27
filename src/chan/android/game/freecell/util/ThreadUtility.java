package chan.android.game.freecell.util;

import android.os.Looper;

public class ThreadUtility {

    public static void print(String msg) {
        Logger.e("Thread " + Thread.currentThread().getName() + " : " + msg);
    }

    public static long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    public static String getCurrentThreadSignature() {
        Thread t = Thread.currentThread();
        long id = t.getId();
        String name = t.getName();
        long priority = t.getPriority();
        String group = t.getThreadGroup().getName();
        return name + ", id=" + id + ", priority=" + priority + ", group=" + group;
    }

    public static int getPriority() {
        return Thread.currentThread().getPriority();
    }

    public static boolean isMainThread() {
        return (Looper.getMainLooper().getThread() == Thread.currentThread());
    }
}
