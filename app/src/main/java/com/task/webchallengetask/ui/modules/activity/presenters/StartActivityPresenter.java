package com.task.webchallengetask.ui.modules.activity.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.util.Log;

import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.data.data_managers.GoogleApiUtils;
import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.data_providers.ProgramDataProvider;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.programs.Program;
import com.task.webchallengetask.global.programs.ProgramManager;
import com.task.webchallengetask.global.utils.IntentHelper;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.services.ActivityTrackerService;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivityView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

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

    private List<ProgramTable> mPrograms;
    private ProgramDataProvider mProgramDataProvider = ProgramDataProvider.getInstance();
    private ActivityDataProvider mActivityDataProvider = ActivityDataProvider.getInstance();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().showCompleteProgramNotification("asdfasdf", "Asdfasdf");
        activitiesList = Arrays.asList(App.getAppContext().getResources()
                .getStringArray(R.array.activities_list));
        getView().setSpinnerData(activitiesList);
        mTimerReceiver = new TimerReceiver();
        activityTrackerReceiver = new ActivityTrackerReceiver();

        if (mPrograms == null) {
            mProgramDataProvider.getPrograms()
                    .subscribe(programTables -> {
                        if (!programTables.isEmpty()) {
                            mPrograms = programTables;
                        }
                    });
        }
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

    public void onBtnStartPauseClicked() {
        if (isStarted) {
            isPaused = !isPaused;
        } else isStarted = true;

        if (!isPaused) {
            App.getAppContext().startService(IntentHelper.
                    getActivityTrackerServiceIntent(Constants.START_TIMER_ACTION, currentActivity));
        } else {
            App.getAppContext().startService(IntentHelper.
                    getActivityTrackerServiceIntent(Constants.PAUSE_TIMER_ACTION, currentActivity));
        }
        getView().setSpinnerEnabled(false);
        getView().toggleStartPause(!isPaused ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    public void onBtnStopClicked() {
        if (isStarted) {
            isStarted = false;
            isPaused = false;

            App.getAppContext().startService(IntentHelper.
                    getActivityTrackerServiceIntent(Constants.STOP_TIMER_ACTION, currentActivity));
            getView().setSpinnerEnabled(true);
            getView().toggleStartPause(R.drawable.ic_play);
            checkNewData();
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
                if (timer != -1 && getView() != null) {
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
            checkNewData();
        }
    }

    private void checkNewData() {
        for (ProgramTable programTable : mPrograms) {
            Constants.PROGRAM_TYPES type = ProgramManager.defineProgramType(programTable);
            Date today = new Date(TimeUtil.getCurrentDay());
            Date nextDay = TimeUtil.addEndOfDay(today);

            mProgramDataProvider.loadData(type, today, nextDay)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pairs -> {
                        if (pairs.get(0).second >= programTable.getTarget()) {
                            String target = programTable.getTarget() + " " + programTable.getUnit();
                            getView().showCompleteProgramNotification(programTable.getName(), target);
                        }
                    }, Logger::e);

        }
    }


    public interface StartActivityView extends BaseActivityView<StartActivityPresenter> {
        void setSpinnerData(List<String> _data);

        void setSpinnerEnabled(boolean _isEnabled);

        void setTimer(String _text);

        void startSenderIntent(IntentSender _intentSender, int _const) throws IntentSender.SendIntentException;

        int getSpinnerSelection();

        void onStartPauseClicked();

        void toggleStartPause(int _icon);

        void onStopClicked();

        void setDistance(String _Text);

        void setSpeed(String _Text);

        void setSteps(String _Text);

        void setCalories(String _Text);

        void setDistanceVisible(boolean _isVisible);

        void setSpeedVisible(boolean _isVisible);

        void setStepsVisible(boolean _isVisible);

        void setCaloriesVisible(boolean _isVisible);

        void showCompleteProgramNotification(String _programName, String _difficult);

    }

}
