package com.task.webchallengetask.ui.base;

public interface BasePresenter<V extends BaseView> {

    void bindView(V _view);

    void unbindView();

    V getView();

    void onViewCreated();

    void onPause();

    void onResume();

    void onStart();

    void onStop();

    void onDestroyView();

    void onBackPressed();
}
