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

    public  Observable<List<ActionParametersModel>> getActivities(){
        return newThread(Observable.just(mDbController.getAllActionParametersModel()));
    }

    public  Observable<ActionParametersModel> getActivitie(int _id){
        return newThread(Observable.just(mDbController.getActionParametersModel(_id)));
    }

    public Observable<List<Float>> getDistance(long _startDate, long _endDate) {
    public Observable<Float> getDistance(long _startDate, long _endDate) {

        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getDistance)
                .toList()
                .map(aFloat -> {
                    float totalDistance = 0;
                    for (float value : aFloat) {
                        totalDistance += value;
                    }
                    return totalDistance;
                });
    }

    public Observable<Integer> getSteps(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getStep)
                .toList()
                .map(integers -> {
                    int totalDistance = 0;
                    for (float value : integers) {
                        totalDistance += value;
                    }
                    return totalDistance;
                });

    }

    public Observable<Float> getActualTime(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList()
                .map(aFloat -> {
                    float totalActualTime = 0;
                    for (float value : aFloat) {
                        totalActualTime += value;
                    }
                    return totalActualTime;
                });


    }

    public Observable<Float> getSpeed(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList()
                .map(aFloat -> {
                    float totalSteps = 0;
                    for (float value : aFloat) {
                        totalSteps += value;
                    }
                    return totalSteps;
                });


    }

    public Observable<Float> getCalories(long _startDate, long _endDate) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_startDate, _endDate))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList()
                .map(aFloat -> {
                    float totalCalories = 0;
                    for (float value : aFloat) {
                        totalCalories += value;
                    }
                    return totalCalories;
                });


    }


}
