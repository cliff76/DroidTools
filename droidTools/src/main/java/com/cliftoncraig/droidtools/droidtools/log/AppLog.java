package com.cliftoncraig.droidtools.droidtools.log;

import android.util.Log;

/**
 * Created by clifton
 * Copyright 7/26/15.
 */
public class AppLog {
    public static final String TAG = AppLog.class.getName();

    public static void log(String msg) {
        android.util.Log.d(TAG, msg);
    }

    public static void logError(String msg) {
        logError(msg, null);
    }

    public static void logError(String msg, Throwable e) {
        if (e!=null) { Log.e(TAG, msg, e); }
        else { Log.e(TAG, msg); }
    }

    public static void d(String tag, String msg) {
        log(tag + "> " + msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        logError(tag + "> " + msg, throwable);
    }
}
