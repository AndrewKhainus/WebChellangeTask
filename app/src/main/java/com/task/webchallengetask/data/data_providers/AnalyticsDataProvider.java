package com.task.webchallengetask.data.data_providers;

import com.task.webchallengetask.global.utils.RxUtils;

import java.util.List;

import rx.Observable;

public class AnalyticsDataProvider extends BaseDataProvider {

    private static volatile AnalyticsDataProvider instance;

    private AnalyticsDataProvider() {
    }

    public static AnalyticsDataProvider getInstance() {
        AnalyticsDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (AnalyticsDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AnalyticsDataProvider();
                }
            }
        }
        return localInstance;
    }


    public Observable<List<String>> getSomeData() {
        return newThread(RxUtils.emptyListObservable(String.class));
    }

}
