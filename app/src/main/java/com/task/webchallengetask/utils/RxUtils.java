package com.task.webchallengetask.utils;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by klim on 21.10.15.
 */
public class RxUtils {


    public static void unsubscribeIfNotNull(Subscription _subscription) {
        if (_subscription != null) {
            _subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription _subscription) {
        if (_subscription == null || _subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return _subscription;
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

    public static <T> Func1<List<List<T>>, Observable<List<T>>> listListToList() {
        return lists -> {
            List<T> result = new ArrayList<>();
            for (List<T> list : lists) {
                for (T table : list)
                    result.add(table);
            }
            return Observable.just(result);
        };
    }

}
