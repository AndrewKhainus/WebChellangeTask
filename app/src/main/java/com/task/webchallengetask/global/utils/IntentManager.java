package com.task.webchallengetask.global.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.task.webchallengetask.App;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.services.ActivityTrackerService;

import java.util.Calendar;

public class IntentManager {

    public static Intent getActivityTrackerServiceIntent(String action, String _activityName) {
        Intent intent = new Intent(App.getAppContext(), ActivityTrackerService.class);
        intent.setAction(action);
        intent.putExtra(Constants.ACTIVITY_NAME_KEY, _activityName);
        return intent;
    }

    public static Intent sendTimerUpdateIntent(Calendar _time) {
        Intent intent = new Intent();
        intent.setAction(Constants.SEND_TIMER_UPDATE_ACTION);
        intent.putExtra(Constants.SEND_TIMER_UPDATE_KEY, _time == null ? -1 : _time.getTimeInMillis());
        return intent;
    }

    public static boolean isServiceRunning(Context _context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) _context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
