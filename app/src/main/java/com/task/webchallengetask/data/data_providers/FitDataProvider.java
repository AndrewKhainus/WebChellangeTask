package com.task.webchallengetask.data.data_providers;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Value;
import com.task.webchallengetask.data.data_managers.GoogleApiUtils;
import com.task.webchallengetask.data.database.DatabaseController;

import rx.Observable;


public class FitDataProvider extends BaseDataProvider {

    private static volatile FitDataProvider instance;
    private DatabaseController mDbController = DatabaseController.getInstance();
    private GoogleApiUtils mGoogleApi = GoogleApiUtils.getInstance();

    private FitDataProvider() {
    }

    public static FitDataProvider getInstance() {
        FitDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (FitDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FitDataProvider();
                }
            }
        }
        return localInstance;
    }

    public Observable<DataPoint> getHistory(long _startDate, long _endDate) {
        return newThread(mGoogleApi.getHistory(_startDate, _endDate));
    }

    public Observable<Value> getDistance(long _startDate, long _endDate) {
        return newThread(getHistory(_startDate, _endDate)
                .filter(dataPoint -> dataPoint.getDataType() == DataType.TYPE_DISTANCE_DELTA)
                .map(dataPoint1 -> dataPoint1.getValue(dataPoint1.getDataType().getFields().get(0))));
    }

    public Observable<Value> getStep(long _startDate, long _endDate) {
        return newThread(getHistory(_startDate, _endDate)
                .filter(dataPoint -> dataPoint.getDataType() == DataType.TYPE_STEP_COUNT_DELTA)
                .map(dataPoint1 -> dataPoint1.getValue(dataPoint1.getDataType().getFields().get(0))));
    }

    public Observable<Value> getSpeed(long _startDate, long _endDate) {
        return newThread(getHistory(_startDate, _endDate)
                .filter(dataPoint -> dataPoint.getDataType() == DataType.TYPE_SPEED)
                .map(dataPoint1 -> dataPoint1.getValue(dataPoint1.getDataType().getFields().get(0))));
    }


}
