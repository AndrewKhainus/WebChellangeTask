package com.task.webchallengetask.ui.modules.settings;

import android.os.Bundle;
import android.view.View;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.base.BaseFragment;

public class SettingsFragment extends BaseFragment<SettingsPresenter> implements SettingsPresenter.SettingsView {

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getTitle() {
        return R.string.settings;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_settings;
    }

    @Override
    public SettingsPresenter initPresenter() {
        return new SettingsPresenter();
    }

    @Override
    public void findUI(View rootView) {

    }

    @Override
    public void setupUI() {

    }
}
