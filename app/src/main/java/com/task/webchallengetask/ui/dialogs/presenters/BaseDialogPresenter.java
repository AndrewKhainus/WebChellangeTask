package com.task.webchallengetask.ui.dialogs.presenters;

import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.ui.base.BaseDialogView;
import com.task.webchallengetask.ui.base.BasePresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by klim on 23.03.16.
 */
public abstract class BaseDialogPresenter<V extends BaseDialogView> implements BasePresenter<V> {
    private V mView;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    public void bindView(V _view) {
        mView = _view;
    }

    @Override
    public void unbindView() {

    }

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {
        RxUtils.unsubscribeIfNotNull(mSubscriptions);
    }

    @Override
    public void onBackPressed() {

    }

    protected void addSubscription(Subscription _subscription) {
        mSubscriptions.remove(_subscription);
        mSubscriptions.add(_subscription);
    }

}
