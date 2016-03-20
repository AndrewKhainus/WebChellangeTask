package com.task.webchallengetask.global.utils;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class RxUtils {


    public static void unsubscribeIfNotNull(Subscription _subscription) {
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }

    public static void click(View _view, Action1 _action) {
        RxView.clicks(_view)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(_action, Logger::e);
    }


    public static Observable<Object> click(View _view) {
        return RxView.clicks(_view)
                .throttleFirst(800, TimeUnit.MILLISECONDS);
    }

    public static <T> Observable<List<T>> emptyListObservable(Class<T> type) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                subscriber.onNext(new ArrayList<T>());
                subscriber.onCompleted();
            }
        });
    }

}