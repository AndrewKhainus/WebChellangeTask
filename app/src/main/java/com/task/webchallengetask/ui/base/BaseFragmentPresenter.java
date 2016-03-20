package com.task.webchallengetask.ui.base;

import com.task.webchallengetask.global.utils.RxUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseFragmentPresenter<V extends BaseView> implements BasePresenter<V> {

    protected V mView;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    public void bindView(V _view) {
        mView = _view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public void onViewCreated() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroyView() {
        RxUtils.unsubscribeIfNotNull(mSubscriptions);
    }

    protected void addSubscription(Subscription _subscription) {
        mSubscriptions.remove(_subscription);
        mSubscriptions.add(_subscription);
    }

    @Override
    public void onBackPressed() {
        if (!mView.isBackStackEmpty()) mView.popBackStack();
        else mView.finishActivity();
    }

}
