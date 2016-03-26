package com.task.webchallengetask.ui.activities.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.util.Log;

import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.GoogleApiUtils;
import com.task.webchallengetask.global.utils.IntentManager;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivityView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by andri on 22.03.2016.
 */
public class StartActivityPresenter extends BaseActivityPresenter<StartActivityPresenter.StartActivityView> {

    private boolean isStarted;
    private boolean isPaused;
    private List<String> activitiesList;
    private TimerReceiver mTimerReceiver;
    private ActivityTrackerReceiver activityTrackerReceiver;
    private String currentActivity = "";

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        activitiesList = Arrays.asList(App.getAppContext().getResources()
                .getStringArray(R.array.activities_list));
        getView().setSpinnerData(activitiesList);
        mTimerReceiver = new TimerReceiver();
        activityTrackerReceiver = new ActivityTrackerReceiver();

    }

    public void testClicked() {
        GoogleApiUtils.getInstance().getHistory().subscribe(t -> {
            String s = "";
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getAppContext().registerReceiver(mTimerReceiver, getTimerUpdateFilter());
        App.getAppContext().registerReceiver(activityTrackerReceiver, getActivityTrackerFilter());
    }

    private IntentFilter getActivityTrackerFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SEND_CALORIES_ACTION);
        intentFilter.addAction(Constants.SEND_SPEED_ACTION);
        intentFilter.addAction(Constants.SEND_STEP_ACTION);
        intentFilter.addAction(Constants.SEND_DISTANCE_ACTION);

        return intentFilter;
    }

    private IntentFilter getTimerUpdateFilter() {
        return new IntentFilter(Constants.SEND_TIMER_UPDATE_ACTION);
    }

    @Override
    public void onPause() {
        if (mTimerReceiver != null) App.getAppContext().unregisterReceiver(mTimerReceiver);
        if (activityTrackerReceiver != null)
            App.getAppContext().unregisterReceiver(activityTrackerReceiver);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
//        App.getAppContext().startService(IntentManager.
//                getActivityTrackerServiceIntent(Constants.STOP_TIMER_ACTION, ""));
        super.onDestroyView();
    }

    public void onBtnStartPauseClicked() {
        if (isStarted) {
            isPaused = !isPaused;
        } else isStarted = true;

        if (!isPaused) {
            App.getAppContext().startService(IntentManager.
                    getActivityTrackerServiceIntent(Constants.START_TIMER_ACTION, currentActivity));
        } else {
            App.getAppContext().startService(IntentManager.
                    getActivityTrackerServiceIntent(Constants.PAUSE_TIMER_ACTION, currentActivity));
        }
        getView().setSpinnerEnabled(false);
        getView().toggleStartPause(!isPaused ? "PAUSE" : "RESUME");
    }

    public void onBtnStopClicked() {
        if (isStarted) {
            isStarted = false;
            isPaused = false;

            App.getAppContext().startService(IntentManager.
                    getActivityTrackerServiceIntent(Constants.STOP_TIMER_ACTION, ""));
            getView().setSpinnerEnabled(true);
            getView().toggleStartPause("START");
        }
    }

    public void onSpinnerItemSelected(int _position) {
        currentActivity = activitiesList.get(_position);
        if (_position == 1) {
            getView().setStepsVisible(false);
        }
        if (_position == 0) {
            getView().setStepsVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isStarted) super.onBackPressed();
    }

    private class TimerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SEND_TIMER_UPDATE_ACTION)) {
                long timer = intent.getLongExtra(Constants.SEND_TIMER_UPDATE_KEY, -1);
                Log.e("w", timer + "");
                if (timer != -1) {
                    Date date = new Date(timer);
                    getView().setTimer(TimeUtil.getStringFromGregorianTime(date));
                }
            }
        }
    }

    private class ActivityTrackerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction().intern();

            switch (action) {
                case Constants.SEND_CALORIES_ACTION:
                    float calories = intent.getFloatExtra(Constants.SEND_CALORIES_KEY, 0f);
                    getView().setCalories(calories + "");
                    break;
                case Constants.SEND_DISTANCE_ACTION:
                    float distance = intent.getFloatExtra(Constants.SEND_DISTANCE_KEY, 0f);
                    getView().setDistance(distance + "");
                    break;
                case Constants.SEND_SPEED_ACTION:
                    float speed = intent.getFloatExtra(Constants.SEND_SPEED_KEY, 0f);
                    getView().setSpeed(speed + "");
                    break;
                case Constants.SEND_STEP_ACTION:
                    int step = intent.getIntExtra(Constants.SEND_STEP_KEY, 0);
                    getView().setSteps(step + "");
            }
        }
    }

    public interface StartActivityView extends BaseActivityView<StartActivityPresenter> {
        void setSpinnerData(List<String> _data);

        void setSpinnerEnabled(boolean _isEnabled);

        void setTimer(String _text);

        void startSenderIntent(IntentSender _intentSender, int _const) throws IntentSender.SendIntentException;

        int getSpinnerSelection();

        void onStartPauseClicked();

        void toggleStartPause(String _text);

        void onStopClicked();

        void setDistance(String _Text);

        void setSpeed(String _Text);

        void setSteps(String _Text);

        void setCalories(String _Text);

        void setDistanceVisible(boolean _isVisible);

        void setSpeedVisible(boolean _isVisible);

        void setStepsVisible(boolean _isVisible);

        void setCaloriesVisible(boolean _isVisible);
    }

}
