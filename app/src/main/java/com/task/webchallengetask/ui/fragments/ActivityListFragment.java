package com.task.webchallengetask.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.base.BaseFragment;
import com.task.webchallengetask.ui.fragments.presenters.ActivityListPresenter;


public class ActivityListFragment extends BaseFragment<ActivityListPresenter>
        implements ActivityListPresenter.ActivityListView {

    private FloatingActionButton fabCreateActivity;

    public static ActivityListFragment newInstance() {

        Bundle args = new Bundle();

        ActivityListFragment fragment = new ActivityListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setTitle() {
        return R.string.activity_list;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_activity_list;
    }

    @Override
    protected ActivityListPresenter initPresenter() {
        return new ActivityListPresenter();
    }

    @Override
    protected void findUI(View rootView) {
        fabCreateActivity = (FloatingActionButton) rootView.findViewById(R.id.fab);
    }

    @Override
    protected void setupUI() {
        fabCreateActivity.setOnClickListener(view -> getPresenter().onFABClicked());
    }
}
