package com.task.webchallengetask.data.data_providers;

import com.google.android.gms.fitness.data.DataPoint;
import com.task.webchallengetask.data.data_managers.GoogleApiUtils;
import com.task.webchallengetask.data.database.DatabaseController;

import rx.Observable;

/**
 * Created by klim on 26.03.16.
 */
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

}
