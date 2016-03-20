package com.task.webchallengetask.ui.fragments;

import android.os.Bundle;
import android.view.View;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.base.BaseFragment;
import com.task.webchallengetask.ui.fragments.presenters.SettingsPresenter;

public class SettingsFragment extends BaseFragment<SettingsPresenter> {

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int setTitle() {
        return R.string.settings;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_settings;
    }

    @Override
    protected SettingsPresenter initPresenter() {
        return new SettingsPresenter();
    }

    @Override
    protected void findUI(View rootView) {

    }

    @Override
    protected void setupUI() {

    }
}
