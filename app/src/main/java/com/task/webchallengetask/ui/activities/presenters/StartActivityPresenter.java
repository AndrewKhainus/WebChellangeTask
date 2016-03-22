package com.task.webchallengetask.ui.activities.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.IntentManager;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by andri on 22.03.2016.
 */
public class StartActivityPresenter extends BaseActivityPresenter<StartActivityPresenter.StartActivityView> {

    private boolean isStarted = false;
    private boolean isPaused = false;
    private List<String> activitiesList;
    private TimerReceiver mTimerReceiver;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        activitiesList = Arrays.asList(App.getAppContext().getResources()
                .getStringArray(R.array.activities_list));
        getView().setSpinnerData(activitiesList);
        mTimerReceiver = new TimerReceiver();
        IntentFilter filter = new IntentFilter(Constants.SEND_TIMER_UPDATE_ACTION);
        App.getAppContext().registerReceiver(mTimerReceiver, filter);

    }

    @Override
    public void onDestroyView() {
        if (mTimerReceiver != null) App.getAppContext().unregisterReceiver(mTimerReceiver);
        super.onDestroyView();
    }

    public void onBtnStartPauseClicked() {
        if (isStarted) {
            isPaused = !isPaused;
        } else isStarted = true;

        if (!isPaused) {
            App.getAppContext().startService(IntentManager.
                    getActivityTrackerServiceIntent(Constants.START_TIMER_ACTION));
        } else {
            App.getAppContext().startService(IntentManager.
                    getActivityTrackerServiceIntent(Constants.PAUSE_TIMER_ACTION));
        }
        getView().setSpinnerEnabled(false);
        getView().toggleStartPause(!isPaused ? "PAUSE" : "RESUME");
    }

    public void onBtnStopClicked() {
        if (isStarted) {
            isStarted = false;
            isPaused = false;

            App.getAppContext().startService(IntentManager.
                    getActivityTrackerServiceIntent(Constants.STOP_TIMER_ACTION));
            getView().setSpinnerEnabled(true);
            getView().toggleStartPause("START");
        }
    }

    public void onSpinnerItemSelected(int _position) {
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
                if (timer != -1) {
                    Date date = new Date(timer);
                    getView().setTimer(TimeUtil.getStringFromGregorianTime(date));
                }
            }
        }
    }


    public interface StartActivityView extends BaseView {
        void setSpinnerData(List<String> _data);
        void setSpinnerEnabled(boolean _isEnabled);
        void setTimer(String _text);

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
