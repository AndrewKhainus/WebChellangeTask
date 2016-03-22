package com.task.webchallengetask;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class App extends Application {
    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        MultiDex.install(this);
/*
        FlowManager.init(this);
        LeakCanary.install(this);
*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getAppContext() {
        return mApp;
    }



}
