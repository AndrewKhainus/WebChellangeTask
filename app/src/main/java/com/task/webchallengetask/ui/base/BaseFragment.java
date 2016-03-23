package com.task.webchallengetask.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment<P extends BaseFragmentPresenter> extends Fragment
        implements BaseFragmentView<P> {

    private BaseActivity mActivity;
    private P mPresenter;

    protected abstract int setTitle();

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = initPresenter();
        if (getPresenter() == null)
            new ClassCastException("Presenter is not created + " + this.getClass().getName());
        if (context instanceof BaseActivity) mActivity = (BaseActivity) context;
        else
            new ClassCastException("Activity should extend BaseActivity " + this.getClass().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        getPresenter().bindView(this);
        findUI(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        getPresenter().onViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    public void onPause() {
        getPresenter().onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        getPresenter().unbindView();
        getPresenter().onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void showLoadingDialog() {
        mActivity.showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        mActivity.hideLoadingDialog();
    }

    @Override
    public void showInfoDialog(String _title, String _message, View.OnClickListener _listener) {
        mActivity.showInfoDialog(_title, _message, _listener);
    }

    @Override
    public void showErrorDialog(String _title, String _message, View.OnClickListener _listener) {
        mActivity.showErrorDialog(_title, _message, _listener);
    }

    @Override
    public void showConfirmDialog(String _title, String _message, View.OnClickListener _listener) {
        mActivity.showConfirmDialog(_title, _message, _listener);
    }

    @Override
    public void switchFragment(BaseFragment _fragment, boolean _addToBackStack) {
        mActivity.switchFragment(_fragment, _addToBackStack);
    }

    @Override
    public void startActivity(Class _activityClass, Bundle _bundle) {
        mActivity.startActivity(_activityClass, _bundle);
    }

    @Override
    public void startActivity(Class _activityClass, int ... _flag) {
        mActivity.startActivity(_activityClass, _flag);
    }

    @Override
    public void finishActivity() {
        mActivity.finishActivity();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressed();
    }

    @Override
    public boolean isBackStackEmpty() {
        return mActivity.getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    @Override
    public void popBackStack() {
        mActivity.getSupportFragmentManager().popBackStack();
    }

}
