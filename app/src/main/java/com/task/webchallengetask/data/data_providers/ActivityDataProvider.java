package com.task.webchallengetask.data.data_providers;

import android.util.Pair;

import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.RxUtils;
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

    public Observable<List<ActionParametersModel>> getActivities() {
        return newThread(Observable.just(mDbController.getAllActionParametersModel()));
    }

    public Observable<ActionParametersModel> getActivityById(int _id) {
        return newThread(Observable.just(mDbController.getActionParametersModel(_id)));
    }

    public Observable<Boolean> deleteActivities(int _id) {
        return newThread(Observable.just(mDbController.deleteActionParametersModel(_id)));
    }

    public Observable<List<Pair<Long, Float>>> getDistance(Date _startDate, Date _endDate) {
        return newThread(RxUtils.createDateList(_startDate, _endDate)
                .flatMap(this::getDayDistance)
                .toList());
    }

    public Observable<List<Pair<Long, Float>>> getSteps(Date _startDate, Date _endDate) {
        return newThread(RxUtils.createDateList(_startDate, _endDate)
                .flatMap(this::getDaySteps)
                .toList());
    }

    public Observable<List<Pair<Long, Float>>> getActualTime(Date _startDate, Date _endDate) {
        return newThread(RxUtils.createDateList(_startDate, _endDate)
                .flatMap(this::getDayActivityTime)
                .toList());

    }

    public Observable<List<Pair<Long, Float>>> getSpeed(Date _startDate, Date _endDate) {
        return newThread(RxUtils.createDateList(_startDate, _endDate)
                .flatMap(this::getDaySpeed)
                .toList());

    }


    public Observable<List<Pair<Long, Float>>> getCalories(Date _startDate, Date _endDate) {
        return newThread(RxUtils.createDateList(_startDate, _endDate)
                .flatMap(this::getDayCalories)
                .toList());

    }

    private Observable<Pair<Long, Float>> getDaySteps(Date _date) {
        Date nextDay = TimeUtil.addEndOfDay(_date);
        return Observable
                .just(mDbController.getActionParametersModel(_date.getTime(), nextDay.getTime()))
                .map(models -> {
                    float totalSteps = 0;
                    for (ActionParametersModel model : models) {
                        totalSteps += model.getStep();
                    }
                    return new Pair<>(_date.getTime(), totalSteps);
                });


    }

    private Observable<Pair<Long, Float>> getDayActivityTime(Date _date) {
        Date nextDay = TimeUtil.addEndOfDay(_date);
        return Observable
                .just(mDbController.getActionParametersModel(_date.getTime(), nextDay.getTime()))
                .map(models -> {
                    float totalTime = 0;
                    for (ActionParametersModel model : models) {
                        totalTime += model.getActivityActualTime();
                    }
                    return new Pair<>(_date.getTime(), totalTime);
                });


    }

    private Observable<Pair<Long, Float>> getDaySpeed(Date _date) {
        Date nextDay = TimeUtil.addEndOfDay(_date);
        return Observable
                .just(mDbController.getActionParametersModel(_date.getTime(), nextDay.getTime()))
                .map(models -> {
                    float totalSpeed = 0;
                    for (ActionParametersModel model : models) {
                        totalSpeed += model.getSpeed();
                    }
                    return new Pair<>(_date.getTime(), totalSpeed);
                });


    }

    private Observable<Pair<Long, Float>> getDayCalories(Date _date) {
        Date nextDay = TimeUtil.addEndOfDay(_date);
        return Observable
                .just(mDbController.getActionParametersModel(_date.getTime(), nextDay.getTime()))
                .map(models -> {
                    float totalCalories = 0;
                    for (ActionParametersModel model : models) {
                        totalCalories += model.getCalories();
                    }
                    return new Pair<>(_date.getTime(), totalCalories);
                });


    }

    private Observable<Pair<Long, Float>> getDayDistance(Date _date) {
        Date nextDay = TimeUtil.addEndOfDay(_date);
        return Observable
                .just(mDbController.getActionParametersModel(_date.getTime(), nextDay.getTime()))
                .map(models -> {
                    float totalDistance = 0;
                    for (ActionParametersModel model : models) {
                        totalDistance += model.getDistance();
                    }
                    return new Pair<>(_date.getTime(), totalDistance);
                });

    }


}
