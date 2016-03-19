package com.task.webchallengetask.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foxtrapp.qualpro.utility.TransitionHelper;

public abstract class BaseFragment<P extends BaseFragmentPresenter> extends Fragment implements BaseView{

    private View rootView;
    private P mPresenter;

    protected abstract int setTitle();
    protected abstract int getLayoutResource();
    protected abstract P initPresenter();
    protected abstract void findUI(View rootView);
    protected abstract void setupUI();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = initPresenter();
        if (getPresenter() == null)
            new ClassCastException("Presenter is not created + " + this.getClass().getName());
        getPresenter().onAttach(((BaseActivity) getActivity()).getActivityPresenter());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResource(), container, false);
        findUI(rootView);

        setHasOptionsMenu(true);
        getPresenter().setView(this);
        getPresenter().onCreateView(getArguments());
        return rootView;
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
    public void startActivityForResult(Intent intent, int requestCode) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
        super.startActivityForResult(intent, requestCode, transitionActivityOptions.toBundle());
    }

    @Override
    public void onPause() {
        getPresenter().onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        getPresenter().onDestroy();
        rootView = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getPresenter().setTitle(setTitle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    protected P getPresenter() {
        return mPresenter;
    }

    protected <T extends View> T fbi(@IdRes int resId) {
        if (rootView == null)
            return null;
        return (T) rootView.findViewById(resId);
    }

}
