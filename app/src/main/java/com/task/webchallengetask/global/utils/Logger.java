package com.task.webchallengetask.global.utils;

import android.util.Log;

public class Logger {

    public static void e (Throwable _throwable) {
        Log.e("logger", _throwable.getClass().getName() + ", cause - " + _throwable.getCause() + ", message - " + _throwable.getMessage());
    }

    public static void d (String _message) {
        Log.d("logger", _message);
    }

}
