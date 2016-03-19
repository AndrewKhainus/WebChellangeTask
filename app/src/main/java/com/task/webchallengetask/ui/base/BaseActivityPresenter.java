package com.task.webchallengetask.ui.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.transition.Visibility;
import android.view.View;

import com.foxtrapp.qualpro.R;
import com.foxtrapp.qualpro.ui.dialogs.BaseDialog;
import com.foxtrapp.qualpro.ui.dialogs.ConfirmDialog;
import com.foxtrapp.qualpro.ui.dialogs.ErrorDialog;
import com.foxtrapp.qualpro.ui.dialogs.InfoDialog;
import com.foxtrapp.qualpro.ui.dialogs.LoadingDialog;
import com.foxtrapp.qualpro.utility.AnimationUtils;
import com.foxtrapp.qualpro.utility.TransitionHelper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivityPresenter<A extends BaseActivity>
        implements ActivityPresenter {

    private A mActivity;
    private LoadingDialog progressDialog;
    private BaseDialog mDialog;
    private CompositeSubscription mSubscriptions;

    public void onCreate(A _activity) {
        mActivity = _activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        mSubscriptions = new CompositeSubscription();
        if (getActivity().getSupportActionBar() != null)
            getActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                BaseFragment fragment = (BaseFragment) getActivity().getSupportFragmentManager().findFragmentById(getActivity().getFragmentContainer());
                setToolBarTitle(fragment.setTitle());
            }
        });
    }

    public void onResume() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setupWindowAnimations() {
        Visibility transition = AnimationUtils.fadeTransition();
        getActivity().getWindow().setEnterTransition(transition);
        getActivity().getWindow().setExitTransition(transition);
        getActivity().getWindow().setReturnTransition(transition);
        getActivity().getWindow().setReenterTransition(transition);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void clearWindowAnimations() {
        getActivity().getWindow().setEnterTransition(null);
        getActivity().getWindow().setExitTransition(null);
        getActivity().getWindow().setReturnTransition(null);
        getActivity().getWindow().setReenterTransition(null);
    }

    public void onDestroy() {
        mSubscriptions.unsubscribe();
        clearWindowAnimations();
        mActivity = null;
    }

    @Override
    public void onBackPressed() {
        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mActivity.getSupportFragmentManager().popBackStack();
        } else
            finishActivity();
    }

    protected final void addSubscription(Subscription _subscription) {
        mSubscriptions.remove(_subscription);
        mSubscriptions.add(_subscription);
    }

    @Override
    public final A getActivity() {
        return mActivity;
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
        mDialog.show(getActivity().getSupportFragmentManager(), "");
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showLoadingDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new LoadingDialog();
            progressDialog.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    protected void clearBackStack() {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void replaceFragment(final Fragment _fragment, boolean _addToBackStack) {
        replaceFragment(_fragment, _addToBackStack, 0, 0);
    }

    @Override
    public void replaceFragment(Fragment _fragment, boolean _addToBackStack, View _sharedElement, String _sharedName) {
        if (getActivity().getFragmentContainer() == 0) {
            new UnsupportedOperationException("There are not container for fragment" + getClass().getName());
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (_addToBackStack) fragmentTransaction.addToBackStack(null);

            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(getActivity().getFragmentContainer());
            if (fragment != null)
                fragmentTransaction.hide(fragment);
            if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0 ||
                    _addToBackStack) {
                fragmentTransaction.add(getActivity().getFragmentContainer(), _fragment, _fragment.getClass().getName());
            } else {
                fragmentTransaction.replace(getActivity().getFragmentContainer(), _fragment, _fragment.getClass().getName());
            }
            fragmentTransaction.show(_fragment);
            fragmentTransaction.addSharedElement(_sharedElement, _sharedName);
            fragmentTransaction.commitAllowingStateLoss();
            setToolBarTitle(((BaseFragment) _fragment).setTitle());
        }

    }

    @Override
    public void replaceFragment(Fragment _fragment, boolean _addToBackStack, int _beginAnimation, int _endAnimation) {
        if (getActivity().getFragmentContainer() == 0) {
            new UnsupportedOperationException("There are not container for fragment" + getClass().getName());
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (_addToBackStack) fragmentTransaction.addToBackStack(null);

            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(getActivity().getFragmentContainer());
            if (fragment != null)
                fragmentTransaction.hide(fragment);
            if (/*mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0 || */_addToBackStack) {
                fragmentTransaction.add(getActivity().getFragmentContainer(), _fragment, _fragment.getClass().getName());
            } else {
                fragmentTransaction.replace(getActivity().getFragmentContainer(), _fragment, _fragment.getClass().getName());
            }
            fragmentTransaction.show(_fragment);
            if (_beginAnimation != 0 && _endAnimation != 0)
                fragmentTransaction.setCustomAnimations(_beginAnimation, _endAnimation);
            fragmentTransaction.commitAllowingStateLoss();
            setToolBarTitle(((BaseFragment) _fragment).setTitle());
        }
    }

    protected void setToolBarTitle(int _titleRes) {
        if (getActivity().getSupportActionBar() != null && _titleRes != 0)
            getActivity().getSupportActionBar().setTitle(_titleRes);
    }

    @Override
    public void startActivity(Class _activityClass, Bundle _bundle) {
        Intent intent = new Intent(getActivity(), _activityClass);
        if (_bundle != null) intent.putExtras(_bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void startActivity(Class _activityClass, Bundle _bundle, Pair<View, String>[] pairs) {
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);

        Intent intent = new Intent(getActivity(), _activityClass);
        if (_bundle != null) intent.putExtras(_bundle);
        getActivity().startActivity(intent, transitionActivityOptions.toBundle());

    }

    @Override
    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().supportFinishAfterTransition();
        } else {
            getActivity().finish();
        }
    }

}
