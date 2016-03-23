package com.task.webchallengetask.ui.fragments;


import android.os.Bundle;
import android.view.View;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.base.BaseFragment;
import com.task.webchallengetask.ui.fragments.presenters.AnalyticsPresenter;

public class AnalyticsFragment extends BaseFragment<AnalyticsPresenter>
        implements AnalyticsPresenter.AnalyticsView {

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
    public final int getLayoutResource() {
        return R.layout.fragment_analytics;
    }

    @Override
    public final  AnalyticsPresenter initPresenter() {
        return new AnalyticsPresenter();
    }

    @Override
    public final  void findUI(View rootView) {

    }

    @Override
    public final  void setupUI() {

    }
}
