package com.task.webchallengetask.ui.base;

import android.support.v4.app.Fragment;
import android.view.View;


public interface FragmentPresenter {

    void showInfoDialog(String _title, String _message, View.OnClickListener _listener);
    void showErrorDialog(String _title, String _message, View.OnClickListener _listener);
    void showConfirmDialog(String _title, String _message, View.OnClickListener _listener);

    void showLoadingDialog();

    void hideLoadingDialog();

    void replaceFragment(final Fragment _fragment, boolean _addToBackStack);

    void replaceFragment(final Fragment _fragment, boolean _addToBackStack, View _sharedElement, String _sharedName);

    void replaceFragment(Fragment _fragment, boolean _addToBackStack, int _beginAnimation, int _endAnimation);

    void onBackPressed();

    ActivityPresenter getActivityPresenter();

    void setTitle(int _titleRes);

}
