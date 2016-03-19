package com.task.webchallengetask.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

public interface ActivityPresenter {

    void showInfoDialog(String _title, String _message, View.OnClickListener _listener);
    void showErrorDialog(String _title, String _message, View.OnClickListener _listener);
    void showConfirmDialog(String _title, String _message, View.OnClickListener _listener);

    void showLoadingDialog();
    void hideLoadingDialog();

    void replaceFragment(final Fragment _fragment, boolean _addToBackStack);
    void replaceFragment(Fragment _fragment, boolean _addToBackStack, int _beginAnimation, int _endAnimation);
    void replaceFragment(Fragment _fragment, boolean _addToBackStack, View _sharedElement, String _sharedName);

    void onBackPressed();

    void startActivity(Class _activityClass, Bundle _bundle);
    void startActivity(Class _activityClass, Bundle _bundle, Pair<View, String>[] pairs);
    void finishActivity();
    BaseActivity getActivity();

}
