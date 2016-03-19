package com.task.webchallengetask;

import android.app.Application;
import android.content.Context;

import com.task.webchallengetask.utils.SharedPrefManager;


public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        FlowManager.init(this);
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getAppContext() {
        return mContext;
    }

}
