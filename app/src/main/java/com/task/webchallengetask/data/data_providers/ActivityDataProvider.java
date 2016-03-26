package com.task.webchallengetask.data.data_providers;

import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;

import java.util.List;

import rx.Observable;

/**
 * Created by klim on 26.03.16.
 */
public class ActivityDataProvider extends BaseDataProvider {

    private static volatile ActivityDataProvider instance;
    private DatabaseController mDbController = DatabaseController.getInstance();


    private ActivityDataProvider() {
    }

    public static ActivityDataProvider getInstance() {
        ActivityDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (ActivityDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ActivityDataProvider();
                }
            }
        }
        return localInstance;
    }


    public Observable<List<ActionParametersModel>> getActivities(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate)));
    }

    public Observable<List<Float>> getDistance(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
        .flatMap(Observable::from))
                .map(ActionParametersModel::getDistance)
                .toList();

    }

    public Observable<List<Integer>> getSteps(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getStep)
                .toList();

    }

    public Observable<List<Float>> getActualTime(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList();

    }

    public Observable<List<Float>> getSpeed(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList();

    }

    public Observable<List<Float>> getCalories(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList();

    }



}
