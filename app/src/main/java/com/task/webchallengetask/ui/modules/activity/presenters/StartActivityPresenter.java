package com.task.webchallengetask.ui.modules.activity.presenters;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.DataTypeCreateRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataTypeResult;
import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.data.data_managers.GoogleApiUtils;
import com.task.webchallengetask.global.utils.IntentHelper;
import com.task.webchallengetask.data.data_managers.SharedPrefManager;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivityView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by andri on 22.03.2016.
 */
public class StartActivityPresenter extends BaseActivityPresenter<StartActivityPresenter.StartActivityView>
        implements GoogleApiClient.ConnectionCallbacks {

    private boolean isStarted = false;
    private boolean isPaused = false;
    private List<String> activitiesList;
    private TimerReceiver mTimerReceiver;
    private GoogleApiClient googleApiClient;
    private String currentActivity = "";
    private PendingIntent mSignInIntent;
    private OnDataPointListener mListenerDistance;
    private OnDataPointListener mListenerSpeed;
    private OnDataPointListener mListenerStep;
    private List<DataType> dataTypesList;
    private List<OnDataPointListener> listenerList;
//    private ActionParametersModel actionParametersModel;

    private int step;
    private float speed;
    private float dist;
    private int weight = SharedPrefManager.getInstance().retrieveWeight();


    private static final String TAG = "StartActivityPresenter";


    @Override
    public void onViewCreated() {
        super.onViewCreated();
        activitiesList = Arrays.asList(App.getAppContext().getResources()
                .getStringArray(R.array.activities_list));
        getView().setSpinnerData(activitiesList);
        mTimerReceiver = new TimerReceiver();
        dataTypesList = new ArrayList<>();
        listenerList = new ArrayList<>();
        setupGoogleApiClient();
//        actionParametersModel = new ActionParametersModel();
    }

    private void setupGoogleApiClient() {
        googleApiClient = GoogleApiUtils.getInstance().buildGoogleApiClient();
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(_connectionResult -> {
            mSignInIntent = _connectionResult.getResolution();
            resolveSignInError();
        });
        googleApiClient.connect();
    }

    public void testClicked(){
        GoogleApiUtils.getInstance().getHistory().subscribe(t -> {
            String s = "";
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.SEND_TIMER_UPDATE_ACTION);
        App.getAppContext().registerReceiver(mTimerReceiver, filter);
    }

    @Override
    public void onPause() {
        if (mTimerReceiver != null) App.getAppContext().unregisterReceiver(mTimerReceiver);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        App.getAppContext().startService(IntentHelper.
                getActivityTrackerServiceIntent(Constants.STOP_TIMER_ACTION, ""));
        unregisterFitnessDataListener();
        super.onDestroyView();
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
        getView().toggleStartPause(!isPaused ? "PAUSE" : "RESUME");
    }

    public void onBtnStopClicked() {
        if (isStarted) {
            isStarted = false;
            isPaused = false;

            App.getAppContext().startService(IntentHelper.
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

    @Override
    public void onConnected(Bundle _bundle) {
        findFitnessDataSources();
    }

    private void checkRecording() {
        int size = dataTypesList.size();
        for (int i = 0; i < size; i++) {
            Fitness.RecordingApi.listSubscriptions(googleApiClient, dataTypesList.get(i))
                    .setResultCallback(listSubscriptionsResult -> {
                        for (Subscription sc : listSubscriptionsResult.getSubscriptions()) {
                            DataType dt = sc.getDataType();
                            Log.i(TAG, "Active subscription for data type: " + dt.getName());
                        }
                    });
        }
    }


    strictfp private float calculationCalories() {
        return weight * dist;
    }

    private void subscribeDataType() {
        int size = dataTypesList.size();
        for (int i = 0; i < size; i++) {
            Fitness.RecordingApi.subscribe(googleApiClient, dataTypesList.get(i))
                    .setResultCallback(_status -> {
                        if (_status.isSuccess()) {
                            if (_status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.i(TAG, "There was a problem subscribing.");
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
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

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                getView().startSenderIntent(mSignInIntent.getIntentSender(),
                        Constants.RC_SIGN_IN_GOOGLE_PLUS);
            } catch (IntentSender.SendIntentException e) {
                googleApiClient.connect();
            }
        }
    }

    public void onActivityRes(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    private void findFitnessDataSources() {
        Fitness.SensorsApi.findDataSources(googleApiClient, new DataSourcesRequest.Builder()
                .setDataTypes(
                        DataType.TYPE_STEP_COUNT_DELTA,
                        DataType.TYPE_DISTANCE_DELTA,
                        DataType.TYPE_SPEED)
                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
                .build())
                .setResultCallback(dataSourcesResult -> {
                    listenerList.clear();
                    dataTypesList.clear();
                    Log.i(TAG, "Result: " + dataSourcesResult.getStatus().toString());
                    for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                        Log.i(TAG, "Data source found: " + dataSource.toString());
                        Log.i(TAG, "Data Source type: " + dataSource.getDataType().getName());

                        if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA) && mListenerStep == null) {
                            listenerList.add(mListenerStep);
                            registerFitnessStepListener(dataSource,
                                    DataType.TYPE_STEP_COUNT_DELTA);
                            dataTypesList.add(dataSource.getDataType());

                        } else if (dataSource.getDataType().equals(DataType.TYPE_DISTANCE_DELTA) && mListenerDistance == null) {
                            listenerList.add(mListenerDistance);
                            registerDistanceListener(dataSource,
                                    DataType.TYPE_DISTANCE_DELTA);

                            dataTypesList.add(dataSource.getDataType());
                        } else if (dataSource.getDataType().equals(DataType.TYPE_SPEED) && mListenerSpeed == null) {
                            listenerList.add(mListenerSpeed);
                            registerSpeedListener(dataSource,
                                    DataType.TYPE_SPEED);
                            dataTypesList.add(dataSource.getDataType());
                        }
                    }
                    subscribeDataType();
                    checkRecording();

                    DataTypeCreateRequest request = new DataTypeCreateRequest.Builder()
                            .setName("com.task.webchallengetask.custom_calories")
                            .addField("cal", Field.FORMAT_FLOAT)
                            .build();

                    PendingResult<DataTypeResult> pendingResult =
                            Fitness.ConfigApi.createCustomDataType(googleApiClient, request);

                    pendingResult.setResultCallback(
                            dataTypeResult -> {
                                DataType customType = dataTypeResult.getDataType();
                            });
                });
    }


    private void registerFitnessStepListener(DataSource dataSource, DataType dataType) {
        mListenerStep = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);
                step += val.asInt();
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                getView().setSteps(step + "");
            }
        };

        Fitness.SensorsApi.add(
                googleApiClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource)
                        .setDataType(dataType)
                        .setSamplingRate(800, TimeUnit.MICROSECONDS)
                        .build(),
                mListenerStep)
                .setResultCallback(status -> {
                    if (status.isSuccess()) {
                        Log.i(TAG, "Listener registered!");
                    } else {
                        Log.i(TAG, "Listener not registered.");
                    }
                });
    }


    private void registerDistanceListener(DataSource dataSource, DataType dataType) {
        mListenerDistance = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);

                dist += val.asFloat();
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                getView().setDistance(dist + "");
                getView().setCalories(calculationCalories() + "");
            }
        };

        Fitness.SensorsApi.add(
                googleApiClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType)// Can't be omitted.
                        .setSamplingRate(800, TimeUnit.MILLISECONDS)
                        .build(),
                mListenerDistance)
                .setResultCallback(status -> {
                    if (status.isSuccess()) {
                        Log.i(TAG, "Listener registered!");
                    } else {
                        Log.i(TAG, "Listener not registered.");
                    }
                });
    }

    private void registerSpeedListener(DataSource dataSource, DataType dataType) {
        mListenerSpeed = dataPoint -> {
            for (Field field : dataPoint.getDataType().getFields()) {
                Value val = dataPoint.getValue(field);

                speed += val.asFloat();
                Log.i(TAG, "Detected DataPoint field: " + field.getName());
                Log.i(TAG, "Detected DataPoint value: " + val);
                getView().setSpeed(speed + "");
            }
        };

        Fitness.SensorsApi.add(
                googleApiClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType)// Can't be omitted.
                        .setSamplingRate(800, TimeUnit.MILLISECONDS)
                        .build(),
                mListenerSpeed)
                .setResultCallback(status -> {
                    if (status.isSuccess()) {
                        Log.i(TAG, "Listener registered!");
                    } else {
                        Log.i(TAG, "Listener not registered.");
                    }
                });
    }


    private void unregisterFitnessDataListener() {
        int size = listenerList.size();
        for (int i = 0; i < size; i++) {
            if (listenerList.get(i) == null)
                return;

            Fitness.SensorsApi.remove(
                    googleApiClient,
                    listenerList.get(i))
                    .setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Listener was removed!");
                        } else {
                            Log.i(TAG, "Listener was not removed.");
                        }
                    });
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