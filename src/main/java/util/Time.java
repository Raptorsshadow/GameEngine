package util;

/**
 * Class: Time
 * Author: rapto
 * CreatedDate: 1/19/2025 : 1:46 AM
 * Project: GameEngine
 * Description: Singleton used to track game time clock.
 */
public class Time {
    public static final float TIME_STARTED = System.nanoTime();

    private Time() {
    }

    /**
     * Return elapsed time in seconds since start.
     *
     * @return current system time
     */
    public static float getTime() {
        return (float) ((System.nanoTime() - TIME_STARTED) * 1E-9);
    }
}
