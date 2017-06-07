package com.jw.android.huddroid;

/**
 * Created by Jon Weissenburger on 5/11/16.
 */
public class TimeSpan {

    int millis;

    private TimeSpan(int millis) {
        this.millis = millis;
    }

    public static TimeSpan fromSeconds(int seconds) {
        return new TimeSpan(seconds * 1000);
    }

    public static TimeSpan fromMillis(int millis) {
        return new TimeSpan(millis);
    }

    public static TimeSpan fromMinutes(int minutes) {
        return new TimeSpan(minutes * 60 * 1000);
    }
}
