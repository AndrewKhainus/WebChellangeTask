package com.task.webchallengetask.data.data_providers;

import android.util.Pair;

import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
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

    public Observable<List<Pair<Long, Float>>> getDistance(Date _startDate, Date _endDate) {

        List<Date> dateList = new ArrayList<>();
        Date currentDate = _startDate;
        do {
            dateList.add(currentDate);
            currentDate = TimeUtil.addDayToDate(currentDate, 1);
        } while (currentDate.before(_endDate) || currentDate.equals(_endDate));


        return newThread(Observable.from(dateList)
                .flatMap(date -> getDistance(date.getTime())
                .toList()));
    }

    public Observable<List<Pair<Long, Float>>> getSteps(Date _startDate, Date _endDate) {

        List<Date> dateList = new ArrayList<>();
        Date currentDate = _startDate;
        do {
            dateList.add(currentDate);
            currentDate = TimeUtil.addDayToDate(currentDate, 1);
        } while (currentDate.before(_endDate) || currentDate.equals(_endDate));


        return newThread(Observable.from(dateList)
                .flatMap(date -> getSteps(date.getTime())
                        .toList()));
    }

    public Observable<List<Pair<Long, Float>>> getActualTime(Date _startDate, Date _endDate) {

        List<Date> dateList = new ArrayList<>();
        Date currentDate = _startDate;
        do {
            dateList.add(currentDate);
            currentDate = TimeUtil.addDayToDate(currentDate, 1);
        } while (currentDate.before(_endDate) || currentDate.equals(_endDate));


        return newThread(Observable.from(dateList)
                .flatMap(date -> getActualTime(date.getTime())
                        .toList()));
    }

    public Observable<List<Pair<Long, Float>>> getCalories(Date _startDate, Date _endDate) {

        List<Date> dateList = new ArrayList<>();
        Date currentDate = _startDate;
        do {
            dateList.add(currentDate);
            currentDate = TimeUtil.addDayToDate(currentDate, 1);
        } while (currentDate.before(_endDate) || currentDate.equals(_endDate));


        return newThread(Observable.from(dateList)
                .flatMap(date -> getCalories(date.getTime())
                        .toList()));
    }




    public Observable<Pair<Long, Float>> getDistance(long _date) {

        return newThread(Observable.just(mDbController.getActionParametersModel(_date, _date))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getDistance)
                .toList()
                .map(aFloat -> {
                    float totalDistance = 0;
                    for (float value : aFloat) {
                        totalDistance += value;
                    }
                    return new Pair<>(_date, totalDistance);
                });
    }

    public Observable<Pair<Long, Float>> getSteps(long _date) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_date, _date))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getStep)
                .toList()
                .map(integers -> {
                    float totalDistance = 0;
                    for (float value : integers) {
                        totalDistance += value;
                    }
                    return new Pair<>(_date, totalDistance);
                });

    }

    public Observable<Pair<Long, Float>> getActualTime(long _date) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_date, _date))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList()
                .map(aFloat -> {
                    float totalActualTime = 0;
                    for (float value : aFloat) {
                        totalActualTime += value;
                    }
                    return new Pair<>(_date, totalActualTime);
                });


    }

    public Observable<Float> getSpeed(long _date) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_date, _date))
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

    public Observable<Pair<Long, Float>> getCalories(long _date) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_date, _date))
                .flatMap(Observable::from))
                .map(ActionParametersModel::getActivityActualTime)
                .toList()
                .map(aFloat -> {
                    float totalCalories = 0;
                    for (float value : aFloat) {
                        totalCalories += value;
                    }
                    return new Pair<>(_date, totalCalories);
                });


    }


}
