package com.task.webchallengetask;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.raizlabs.android.dbflow.config.FlowManager;


public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        FlowManager.init(this);
//        LeakCanary.install(this);
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }

    public static Context getAppContext() {
        return sApp;
    }

}
