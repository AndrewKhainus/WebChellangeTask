package com.task.webchallengetask.data.data_providers;

import com.task.webchallengetask.data.rest.RetrofitAdapter;
import com.task.webchallengetask.data.rest.RetrofitInterface;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.RxUtils;

import java.util.List;

import rx.Observable;

public class AnalitycsDataProvider extends BaseDataProvider {

    private static volatile AnalitycsDataProvider instance;

    private AnalitycsDataProvider() {
    }

    public static AnalitycsDataProvider getInstance() {
        AnalitycsDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (AnalitycsDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AnalitycsDataProvider();
                }
            }
        }
        return localInstance;
    }


    public Observable<List<String>> getSomeData() {
        return newThread(RxUtils.emptyListObservable(String.class));
    }

}
