package com.task.webchallengetask.ui.activities;

import android.view.View;

import com.task.webchallengetask.ui.activities.presenters.CheckPresenter;
import com.task.webchallengetask.ui.base.BaseActivity;

/**
 * Created by Sergbek on 22.03.2016.
 */
public class CheckActivity extends BaseActivity<CheckPresenter> implements CheckPresenter.CheckView {

    @Override
    public int getLayoutResource() {
        return 0;
    }

    @Override
    public CheckPresenter initPresenter() {
        return new CheckPresenter();
    }

    @Override
    public void findUI(View _rootView) {

    }

    @Override
    public void setupUI() {

    }
}
