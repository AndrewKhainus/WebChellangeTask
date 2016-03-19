package com.task.webchallengetask.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.foxtrapp.qualpro.utility.RxUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseFragmentPresenter<V> implements FragmentPresenter {

    protected V view;
    private ActivityPresenter mActivityPresenter;
    private CompositeSubscription mSubscriptions;
    private Bundle arguments;

    public void onCreateView(Bundle _arguments) {
        arguments = _arguments;
        mSubscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(mSubscriptions);
    }

    public void onViewCreated() {}

    public void onResume() {
    }

    public void onPause() {
    }

    public void onDestroy() {
        view = null;
        RxUtils.unsubscribeIfNotNull(mSubscriptions);
        mSubscriptions = null;
        arguments = null;
    }

    public void onAttach(ActivityPresenter _activity) {
        mActivityPresenter = _activity;
    }

    public void setView(V _view) {
        view = _view;
    }

    public V getView() {
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

    protected void addSubscription(Subscription _subscription) {
        mSubscriptions.remove(_subscription);
        mSubscriptions.add(_subscription);
    }

    protected Bundle getArguments() {
        return arguments;
    }

    @Override
    public void showLoadingDialog() {
        getActivityPresenter().showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        getActivityPresenter().hideLoadingDialog();
    }

    @Override
    public void showInfoDialog(String _title, String _message, View.OnClickListener _listener) {
        getActivityPresenter().showInfoDialog(_title, _message, _listener);
    }

    @Override
    public void showErrorDialog(String _title, String _message, View.OnClickListener _listener) {
        getActivityPresenter().showErrorDialog(_title, _message, _listener);
    }

    @Override
    public void showConfirmDialog(String _title, String _message, View.OnClickListener _listener) {
        getActivityPresenter().showConfirmDialog(_title, _message, _listener);
    }

    @Override
    public void replaceFragment(Fragment _fragment, boolean _addToBackStack) {
        getActivityPresenter().replaceFragment(_fragment, _addToBackStack);
    }

    @Override
    public void replaceFragment(Fragment _fragment, boolean _addToBackStack, View _sharedElement, String _sharedName) {
        getActivityPresenter().replaceFragment(_fragment, _addToBackStack, _sharedElement, _sharedName);
    }

    @Override
    public void replaceFragment(Fragment _fragment, boolean _addToBackStack, int _beginAnimation, int _endAnimation) {
        getActivityPresenter().replaceFragment(_fragment, _addToBackStack, _beginAnimation, _endAnimation);
    }

    @Override
    public void onBackPressed() {
        mActivityPresenter.onBackPressed();
    }

    @Override
    public ActivityPresenter getActivityPresenter() {
        return mActivityPresenter;
    }

    @Override
    public void setTitle(int _titleRes) {
        if (mActivityPresenter.getActivity().getSupportActionBar() != null && _titleRes != 0)
            mActivityPresenter.getActivity().getSupportActionBar().setTitle(_titleRes);
    }

}
