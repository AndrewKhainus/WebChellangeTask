package com.task.webchallengetask.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public interface BaseView {

    void showInfoDialog(String _title, String _message, View.OnClickListener _listener);
    void showErrorDialog(String _title, String _message, View.OnClickListener _listener);
    void showConfirmDialog(String _title, String _message, View.OnClickListener _listener);

    void showLoadingDialog();
    void hideLoadingDialog();

    void switchFragment(final BaseFragment _fragment, boolean _addToBackStack);

    void onBackPressed();
    boolean isBackStackEmpty();
    void popBackStack();


    void startActivity(Class _activityClass, Bundle _bundle);
    void startActivity(Class _activityClass, int ... _flag);
    void startService(Intent _intent, int ... flags);
    void finishActivity();

}
