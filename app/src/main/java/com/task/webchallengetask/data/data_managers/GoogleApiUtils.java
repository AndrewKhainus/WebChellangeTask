package com.task.webchallengetask.data.data_managers;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.plus.Plus;
import com.task.webchallengetask.App;
import com.task.webchallengetask.global.utils.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by Sergbek on 24.03.2016.
 */
public final class GoogleApiUtils {

    private static GoogleApiUtils instance;
    private GoogleApiClient googleApiClient;

    public static GoogleApiUtils getInstance() {
        GoogleApiUtils localInstance = instance;
        if (localInstance == null) {
            synchronized (GoogleApiUtils.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GoogleApiUtils();
                }
            }
        }
        return localInstance;
    }

    public GoogleApiClient buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(App.getAppContext())
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.SESSIONS_API)
                    .addApi(Fitness.CONFIG_API)
                    .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                    .addScope(Fitness.SCOPE_LOCATION_READ_WRITE)
                    .build();
        }

        return googleApiClient;
    }

    public GoogleApiClient buildGoogleApiClientWithGooglePlus() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(App.getAppContext())
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Plus.API, Plus.PlusOptions.builder().build())
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.SESSIONS_API)
                    .addApi(Fitness.CONFIG_API)
                    .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                    .addScope(Fitness.SCOPE_LOCATION_READ_WRITE)
                    .addScope(new Scope(Scopes.PROFILE))
                    .addScope(new Scope(Scopes.PLUS_ME))
                    .addScope(new Scope(Scopes.PLUS_LOGIN))
                    .build();
        }

        return googleApiClient;
    }

    public boolean isNotEmptyClient() {
        return googleApiClient != null;
    }

    public void logoutGooglePlus() {
        Plus.AccountApi.clearDefaultAccount(googleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
        disableFit();
    }

    public void disableFit() {
        Fitness.ConfigApi.disableFit(googleApiClient);
        googleApiClient = null;
    }

    public Observable<DataPoint> getHistory() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        return Observable.create(_history -> {
            if (googleApiClient == null)
                _history.onError(new GoogleAuthException());

            Fitness.HistoryApi
                    .readData(googleApiClient, requestHistory(startTime, endTime))
                    .setResultCallback(_dataReadResult -> {
                        if (_dataReadResult.getBuckets().size() > 0) {
//                            Logger.d("DataSet.size(): " + _dataReadResult.getBuckets().size());
                            for (Bucket bucket : _dataReadResult.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
//                                    Logger.d("dataSet.dataType: " + dataSet.getDataType().getName());

                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        describeDataPoint(dp, new SimpleDateFormat("dd.MM.yyyy"));
                                        _history.onNext(dp);
                                    }
                                }
                            }
                        } else if (_dataReadResult.getDataSets().size() > 0) {
//                            Logger.d("dataSet.size(): " + _dataReadResult.getDataSets().size());
                            for (DataSet dataSet : _dataReadResult.getDataSets()) {
//                                Logger.d("dataType: " + dataSet.getDataType().getName());

                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    describeDataPoint(dp, new SimpleDateFormat("dd.MM.yyyy"));
                                    _history.onNext(dp);
                                }
                            }
                        }
                    });
        });
    }

    public Observable<DataPoint> getHistory(Date _date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(_date);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        return Observable.create(_history -> {
            if (googleApiClient == null)
                _history.onError(new GoogleAuthException());

            Fitness.HistoryApi
                    .readData(googleApiClient, requestHistory(startTime, endTime))
                    .setResultCallback(_dataReadResult -> {
                        if (_dataReadResult.getBuckets().size() > 0) {
//                            Logger.d("DataSet.size(): " + _dataReadResult.getBuckets().size());
                            for (Bucket bucket : _dataReadResult.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
//                                    Logger.d("dataSet.dataType: " + dataSet.getDataType().getName());

                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        describeDataPoint(dp, new SimpleDateFormat("dd.MM.yyyy"));
                                        _history.onNext(dp);
                                    }
                                }
                            }
                        } else if (_dataReadResult.getDataSets().size() > 0) {
//                            Logger.d("dataSet.size(): " + _dataReadResult.getDataSets().size());
                            for (DataSet dataSet : _dataReadResult.getDataSets()) {
//                                Logger.d("dataType: " + dataSet.getDataType().getName());

                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    describeDataPoint(dp, new SimpleDateFormat("dd.MM.yyyy"));
                                    _history.onNext(dp);
                                }
                            }
                        }
                    });
        });
    }

    public Observable<DataPoint> getHistory(long _start, long _end) {

        return Observable.create(_history -> {
            if (googleApiClient == null)
                _history.onError(new GoogleAuthException());

            Fitness.HistoryApi
                    .readData(googleApiClient, requestHistory(_start, _end))
                    .setResultCallback(_dataReadResult -> {
                        if (_dataReadResult.getBuckets().size() > 0) {
//                            Logger.d("DataSet.size(): " + _dataReadResult.getBuckets().size());
                            for (Bucket bucket : _dataReadResult.getBuckets()) {
                                List<DataSet> dataSets = bucket.getDataSets();
                                for (DataSet dataSet : dataSets) {
//                                    Logger.d("dataSet.dataType: " + dataSet.getDataType().getName());

                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        describeDataPoint(dp, new SimpleDateFormat("dd.MM.yyyy"));
                                        _history.onNext(dp);
                                    }
                                }
                            }
                        } else if (_dataReadResult.getDataSets().size() > 0) {
//                            Logger.d("dataSet.size(): " + _dataReadResult.getDataSets().size());
                            for (DataSet dataSet : _dataReadResult.getDataSets()) {
//                                Logger.d("dataType: " + dataSet.getDataType().getName());

                                for (DataPoint dp : dataSet.getDataPoints()) {
                                    describeDataPoint(dp, new SimpleDateFormat("dd.MM.yyyy"));
                                    _history.onNext(dp);
                                }
                            }
                        }
                    });
        });
    }

    private DataReadRequest requestHistory(long _start, long _end) {
        return new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .aggregate(DataType.TYPE_SPEED, DataType.AGGREGATE_SPEED_SUMMARY)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(_start, _end, TimeUnit.MILLISECONDS)
                .build();
    }

    public static void describeDataPoint(DataPoint dp, DateFormat dateFormat) {
        String msg = "dataPoint: "
                + "type: " + dp.getDataType().getName() + "\n"
                + ", range: [" + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                + "-" + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                + "]\n"
                + ", fields: [";

        for (Field field : dp.getDataType().getFields()) {
            msg += field.getName() + "=" + dp.getValue(field) + " ";
        }

        msg += "]";
        Logger.d(msg);
    }
}
