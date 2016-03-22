package com.task.webchallengetask.ui.activities;

import android.os.Bundle;

import com.task.webchallengetask.ui.activities.presenters.CheckPresenter;
import com.task.webchallengetask.ui.base.BaseActivity;

/**
 * Created by Sergbek on 22.03.2016.
 */
public class CheckActivity extends BaseActivity<CheckPresenter> implements CheckPresenter.CheckView {

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected CheckPresenter initPresenter() {
        return new CheckPresenter();
    }

    @Override
    protected void findUI() {

    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {

    }
}
