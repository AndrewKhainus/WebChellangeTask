package com.task.webchallengetask.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.GoogleApiUtils;
import com.task.webchallengetask.global.utils.IntentManager;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.SharedPrefManager;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.activities.StartActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import static com.task.webchallengetask.global.Constants.START_TIMER_ACTION;


/**
 * Created by andri on 22.03.2016.
 */
public class ActivityTrackerService extends Service {

    private Calendar mTimerTime;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private Timer mTimer;
    private String currentActivity;
    private GoogleApiClient googleApiClient;
//    private boolean isPause;

    private OnDataPointListener mListenerDistance;
    private OnDataPointListener mListenerSpeed;
    private OnDataPointListener mListenerStep;

    private HashMap<OnDataPointListener, DataSource> listenerDataTypeHashMap;

    private int step;
    private float speed;
    private float dist;
    private int weight = SharedPrefManager.getInstance().retrieveWeight();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listenerDataTypeHashMap = new HashMap<>(3);
        googleApiClient = GoogleApiUtils.getInstance().buildGoogleApiClientWithGooglePlus();
        findFitnessDataSources();

        if (notificationManager == null)
            notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        currentActivity = intent.getStringExtra(Constants.ACTIVITY_NAME_KEY);
        switch (intent.getAction()) {
            case START_TIMER_ACTION:
                if (mTimerTime == null) {
                    mTimerTime = TimeUtil.getCalendarFromString("00:00:00");
                }
                mTimer = new Timer();
                mTimer.schedule(new CounterRunnable(), 0, 1000);
                startForeground(Constants.FOREGROUND_NOTIFICATION_SERVICE_ID,
                        getNotification(TimeUtil.getStringFromCalendar(mTimerTime)).build());
//                isPause = false;
                break;
            case Constants.PAUSE_TIMER_ACTION:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                }
//                isPause = true;
                break;
            case Constants.STOP_TIMER_ACTION:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                }
//                isPause = false;
                unregisterFitnessDataListener();
                mTimerTime = null;
                notificationManager.cancel(Constants.FOREGROUND_NOTIFICATION_SERVICE_ID);
                stopForeground(true);
                stopSelf();
                break;
        }
        return Service.START_NOT_STICKY;
    }

    private void registerListeners() {
        for (Map.Entry<OnDataPointListener, DataSource> data : listenerDataTypeHashMap.entrySet()) {
            register(data.getKey(), data.getValue());
        }
    }

    private void register(OnDataPointListener _key, DataSource _value) {

        Fitness.SensorsApi.add(
                googleApiClient,
                new SensorRequest.Builder()
                        .setDataSource(_value)
                        .setDataType(_value.getDataType())
                        .setSamplingRate(1, TimeUnit.SECONDS)
                        .build(),
                _key)
                .setResultCallback(status -> {
                    if (status.isSuccess()) {
                        Logger.d("Listener registered! " + _value);
                    } else {
                        Logger.d("Listener not registered " + _value);
                    }
                });
    }

    private void implStepListener() {
        mListenerStep = _dataPoint -> {
//            if (!isPause) {
            for (Field field : _dataPoint.getDataType().getFields()) {
                Value val = _dataPoint.getValue(field);
                step += val.asInt();
                Logger.d("Detected DataPoint field: " + field.getName());
                Logger.d("Detected DataPoint value: " + val);
                sendBroadcast(IntentManager.sendStepIntent(step));
            }
//            }
        };
    }

    private void implDistanceListener() {
        mListenerDistance = _dataPoint -> {
//            if (!isPause) {
            for (Field field : _dataPoint.getDataType().getFields()) {
                Value val = _dataPoint.getValue(field);

                dist += val.asFloat() / 1000; // meters
                Logger.d("Detected DataPoint field: " + field.getName());
                Logger.d("Detected DataPoint value: " + val);
                sendBroadcast(IntentManager.sendDistanceIntent(dist));
                sendBroadcast(IntentManager.sendCaloriesIntent(calculationCalories()));
            }
//            }
        };
    }

    private void implSpeedListener() {
        mListenerSpeed = _dataPoint -> {
//            if (isPause) {
            for (Field field : _dataPoint.getDataType().getFields()) {
                Value val = _dataPoint.getValue(field);

                speed += val.asFloat();
                Logger.d("Detected DataPoint field: " + field.getName());
                Logger.d("Detected DataPoint value: " + val);
                sendBroadcast(IntentManager.sendSpeedIntent(speed));
            }
//            }
        };
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void unregisterFitnessDataListener() {
        for (Map.Entry<OnDataPointListener, DataSource> listeners : listenerDataTypeHashMap.entrySet()) {
            if (listeners.getKey() == null)
                return;

            Fitness.SensorsApi.remove(
                    googleApiClient,
                    listeners.getKey())
                    .setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Logger.d("Listener was removed!");
                        } else {
                            Logger.d("Listener was not removed.");
                        }
                    });
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
                    listenerDataTypeHashMap.clear();
                    Logger.d("Result: " + dataSourcesResult.getStatus().toString());
                    for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                        Logger.d("Data source found: " + dataSource.toString());
                        Logger.d("Data Source type: " + dataSource.getDataType().getName());

                        if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA) && mListenerStep == null) {
                            implStepListener();
                            listenerDataTypeHashMap.put(mListenerStep, dataSource);


                        } else if (dataSource.getDataType().equals(DataType.TYPE_DISTANCE_DELTA) && mListenerDistance == null) {
                            implDistanceListener();
                            listenerDataTypeHashMap.put(mListenerDistance, dataSource);


                        } else if (dataSource.getDataType().equals(DataType.TYPE_SPEED) && mListenerSpeed == null) {
                            implSpeedListener();
                            listenerDataTypeHashMap.put(mListenerSpeed, dataSource);
                        }
                    }
                    registerListeners();
                    subscribeDataType();
                    checkRecording();
                });
    }

    strictfp private float calculationCalories() {
        return weight * dist;
    }

    private void checkRecording() {
        for (Map.Entry<OnDataPointListener, DataSource> data : listenerDataTypeHashMap.entrySet()) {
            Fitness.RecordingApi.listSubscriptions(googleApiClient, data.getValue().getDataType())
                    .setResultCallback(listSubscriptionsResult -> {
                        for (Subscription sc : listSubscriptionsResult.getSubscriptions()) {
                            DataType dt = sc.getDataType();
                            Logger.d("Active subscription for data type: " + dt.getName());
                        }
                    });
        }
    }

    private void subscribeDataType() {
        for (Map.Entry<OnDataPointListener, DataSource> data : listenerDataTypeHashMap.entrySet()) {
            Fitness.RecordingApi.subscribe(googleApiClient, data.getValue().getDataType())
                    .setResultCallback(_status -> {
                        if (_status.isSuccess()) {
                            if (_status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Logger.d("Existing subscription for activity detected");
                            } else {
                                Logger.d("Successfully subscribed!");
                            }
                        } else {
                            Logger.d("There was a problem subscribing.");
                        }
                    });
        }
    }

    private class CounterRunnable extends TimerTask {

        @Override
        public void run() {
            mTimerTime = TimeUtil.addSecondToCalendar(mTimerTime);
            if (mBuilder != null) {
                mBuilder.setContentText(TimeUtil.getStringFromCalendar(mTimerTime));
                notificationManager.notify(Constants.FOREGROUND_NOTIFICATION_SERVICE_ID, mBuilder.build());
            }
            sendBroadcast(IntentManager.sendTimerUpdateIntent(mTimerTime));
        }
    }

    private NotificationCompat.Builder getNotification(String _message) {
        Intent notificationIntent = new Intent(this, StartActivity.class);
        notificationIntent.setAction(Constants.MAIN_ACTION);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(currentActivity)
                    .setContentText(_message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setColor(ContextCompat.getColor(this, R.color.red_color));
            }
        }
        return mBuilder;
    }
}
