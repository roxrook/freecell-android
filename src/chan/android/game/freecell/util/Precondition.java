package chan.android.game.freecell.util;

import java.util.Collection;

public final class Precondition {

    public static void checkValidRange(int begin, int end) {
        if (begin > end || begin < 0 || end < 0) {
            throw new RuntimeException("Argument range(" + begin + "," + end + ") is invalid.");
        }
    }

    public static void checkCondition(boolean shouldBe, String condition) {
        if (!shouldBe) {
            throw new RuntimeException("Condition did not meet " + condition);
        }
    }

    public static void checkNotNull(Object o, String name) {
        if (o == null) {
            throw new RuntimeException("Argument '" + name + "' cannot be null.");
        }
    }

    public static <T> void checkNotNullOrEmpty(Collection<T> c, String name) {
        if (isNullOrEmpty(c)) {
            throw new RuntimeException("Argument '" + name + "' cannot be null or empty.");
        }
    }

    public static void checkNotNullOrEmpty(Object[] c, String name) {
        if (isNullOrEmpty(c)) {
            throw new RuntimeException("Argument '" + name + "' cannot be null or empty.");
        }
    }

    public static void checkNotNullOrEmpty(String s, String name) {
        if (isNullOrEmpty(s)) {
            throw new RuntimeException("Argument '" + name + "' cannot be null or empty.");
        }
    }

    public static void checkOneOf(Object o, String name, Object... all) {
        for (Object other : all) {
            if (other != null) {
                if (other.equals(o)) {
                    return;
                }
            } else {
                if (o == null) {
                    return;
                }
            }
        }
        throw new RuntimeException("Argument " + name + " is not one of the following values " + all.toString());
    }

    /**
     * Helper
     *
     * @param s
     * @param <T>
     * @return
     */
    public static <T> boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0);
    }

    /**
     * Helper
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> boolean isNullOrEmpty(Object[] c) {
        return (c == null) || (c.length == 0);
    }

    /**
     * Helper
     *
     * @param c
     * @param <T>
     * @return
     */
    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || (c.size() == 0);
    }
}

