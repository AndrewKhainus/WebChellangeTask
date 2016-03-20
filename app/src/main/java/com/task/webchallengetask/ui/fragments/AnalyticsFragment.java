package com.task.webchallengetask.ui.fragments;


import android.os.Bundle;
import android.view.View;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.base.BaseFragment;
import com.task.webchallengetask.ui.fragments.presenters.AnalyticsPresenter;

public class AnalyticsFragment extends BaseFragment<AnalyticsPresenter>{

    public static AnalyticsFragment newInstance() {

        Bundle args = new Bundle();

        AnalyticsFragment fragment = new AnalyticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setTitle() {
        return R.string.analytics;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_analytics;
    }

    @Override
    protected AnalyticsPresenter initPresenter() {
        return new AnalyticsPresenter();
    }

    @Override
    protected void findUI(View rootView) {

    }

    @Override
    protected void setupUI() {

    }
}
