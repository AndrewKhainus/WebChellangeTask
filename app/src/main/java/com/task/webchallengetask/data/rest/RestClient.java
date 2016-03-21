package com.task.webchallengetask.data.rest;

import com.task.webchallengetask.global.Constants;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestClient {
    private static volatile RestClient instance;
    private RetrofitInterface mApi;

    private RestClient() {
        RetrofitAdapter adapter = new RetrofitAdapter();
        mApi = adapter.createApi(RetrofitInterface.class, Constants.PREDICTION_END_POINT);
    }

    public static RestClient getInstance() {
        RestClient localInstance = instance;
        if (localInstance == null) {
            synchronized (RestClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RestClient();
                }
            }
        }
        return localInstance;
    }

    private RetrofitInterface getApi() {
        return getInstance().mApi;
    }

    protected <T> Observable<T> responseWrapper(Observable<T> _response) {
        return _response.subscribeOn(Schedulers.newThread())
                .timeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }


}
