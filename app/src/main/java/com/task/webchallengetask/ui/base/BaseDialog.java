package com.task.webchallengetask.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.utils.RxUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseDialog extends DialogFragment {

    private int mContentResource = getLayoutResource();

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        super.onCreateView(_inflater, _container, _savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = _inflater.inflate(R.layout.dialog_base_layout, _container, false);
        if (mContentResource != 0) {
            FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.content_container_BDL);
            frameLayout.removeAllViews();
            frameLayout.addView(_inflater.inflate(mContentResource, frameLayout, false));
        }
        findUI(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View _view, Bundle _savedInstanceState) {
        super.onViewCreated(_view, _savedInstanceState);
        setupUI();
    }

    @Override
    public void onDestroy() {
        RxUtils.unsubscribeIfNotNull(mSubscriptions);
        super.onDestroy();
    }

    protected void addSubscription(Subscription _subscription) {
        mSubscriptions.remove(_subscription);
        mSubscriptions.add(_subscription);
    }

    protected abstract int getLayoutResource();

    protected abstract void setupUI();
    protected abstract void findUI(View rootView);

    public void setTitle(String _title) {
        new UnsupportedOperationException();
    }

    public void setMessage(String _message) {
        new UnsupportedOperationException();
    }

    public void setOnClickListener(View.OnClickListener _listener) {
        new UnsupportedOperationException();
    }

}
