package com.task.webchallengetask.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.task.webchallengetask.ui.dialogs.ConfirmDialog;
import com.task.webchallengetask.ui.dialogs.ErrorDialog;
import com.task.webchallengetask.ui.dialogs.InfoDialog;
import com.task.webchallengetask.ui.dialogs.LoadingDialog;

public abstract class BaseActivity<P extends BaseActivityPresenter> extends AppCompatActivity
        implements BaseView {

    private P mPresenter;
    private LoadingDialog progressDialog;
    private BaseDialog mDialog;

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract P initPresenter();

    protected abstract void findUI();

    protected abstract void setupUI(Bundle savedInstanceState);

    protected int getFragmentContainer() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if (getLayoutResource() != 0) setContentView(getLayoutResource());
        mPresenter.bindView(this);

        findUI();
        setupUI(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentById(getFragmentContainer());
                setToolBarTitle(fragment.setTitle());
            }
        });

        mPresenter.onViewCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroyView();
        mPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPressed();
    }

    public boolean isBackStackEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @Override
    public void showInfoDialog(String _title, String _message, View.OnClickListener _listener) {
        showDialog(new InfoDialog(), _title, _message, _listener);
    }

    @Override
    public void showErrorDialog(String _title, String _message, View.OnClickListener _listener) {
        showDialog(new ErrorDialog(), _title, _message, _listener);
    }

    @Override
    public void showConfirmDialog(String _title, String _message, View.OnClickListener _listener) {
        showDialog(new ConfirmDialog(), _title, _message, _listener);
    }

    private void showDialog(BaseDialog _dialog, String _title, String _message, View.OnClickListener _listener) {
        if (mDialog != null && mDialog.isVisible()) mDialog.dismiss();
        mDialog = _dialog;
        mDialog.setTitle(_title);
        mDialog.setMessage(_message);
        mDialog.setOnClickListener(_listener);
        mDialog.show(getSupportFragmentManager(), "");
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new LoadingDialog();
            progressDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void switchFragment(final BaseFragment _fragment, boolean _addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(getFragmentContainer());
        if (fragment != null)
            fragmentTransaction.hide(fragment);

        if (_addToBackStack) {
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.add(getFragmentContainer(), _fragment, _fragment.getClass().getName());
        } else {
            fragmentTransaction.replace(getFragmentContainer(), _fragment, _fragment.getClass().getName());
        }
        fragmentTransaction.show(_fragment);
        fragmentTransaction.commitAllowingStateLoss();
        setToolBarTitle(_fragment.setTitle());
    }

    protected void setToolBarTitle(int _titleRes) {
        if (getSupportActionBar() != null && _titleRes != 0)
            getSupportActionBar().setTitle(_titleRes);
    }

    @Override
    public void startActivity(Class _activityClass, Bundle _bundle) {
        Intent intent = new Intent(this, _activityClass);
        if (_bundle != null) intent.putExtras(_bundle);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class _activityClass, int ... _flag) {
        Intent intent = new Intent(this, _activityClass);
        for (int a_flag : _flag) {
            intent.setFlags(a_flag);
        }
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        finish();
    }


}
