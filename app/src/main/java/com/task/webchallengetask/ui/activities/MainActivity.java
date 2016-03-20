package com.task.webchallengetask.ui.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.base.BaseActivity;
import com.task.webchallengetask.ui.presenters.MainActivityPresenter;

public class MainActivity extends BaseActivity<MainActivityPresenter>
        implements MainActivityPresenter.MainView {

    private TextView tvSample;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected MainActivityPresenter initPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    protected void findUI() {
        tvSample = (TextView) findViewById(R.id.sample_text);
    }


    @Override
    protected void setupUI(Bundle savedInstanceState) {

    }

    @Override
    public void setText(String _text) {
        tvSample.setText(_text);
    }
}
