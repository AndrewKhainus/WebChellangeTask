package com.task.webchallengetask.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.ui.activities.presenters.StartActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivity;

import java.util.List;

/**
 * Created by andri on 22.03.2016.
 */
public class StartActivity extends BaseActivity<StartActivityPresenter>
        implements StartActivityPresenter.StartActivityView {

    private Toolbar mToolbar;
    private TextView tvTimer;
    private Spinner spChooseActivity;
    private TextView tvDistance;
    private ViewGroup vgDistanceContainer;
    private TextView tvSpeed;
    private ViewGroup vgSpeedContainer;
    private TextView tvSteps;
    private ViewGroup vgStepsContainer;
    private TextView tvCalories;
    private ViewGroup vgCaloriesContainer;
    private TextView btnStartPause;
    private TextView btnStop;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_start_activity;
    }

    @Override
    public StartActivityPresenter initPresenter() {
        return new StartActivityPresenter();
    }

    @Override
    public void findUI(View _rootView) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTimer = (TextView) findViewById(R.id.tvTimer_ASA);
        spChooseActivity = (Spinner) findViewById(R.id.spChooseActivity_ASA);
        tvDistance = (TextView) findViewById(R.id.tvDistance_ASA);
        vgDistanceContainer = (ViewGroup) findViewById(R.id.distanceContainer_ASA);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed_ASA);
        vgSpeedContainer = (ViewGroup) findViewById(R.id.speedContainer_ASA);
        tvSteps = (TextView) findViewById(R.id.tvSteps_ASA);
        vgStepsContainer = (ViewGroup) findViewById(R.id.stepsContainer_ASA);
        tvCalories = (TextView) findViewById(R.id.tvCalories_ASA);
        vgCaloriesContainer = (ViewGroup) findViewById(R.id.caloriesContainer_ASA);
        btnStartPause = (TextView) findViewById(R.id.btnStartPause_ASA);
        btnStop = (TextView) findViewById(R.id.btnStop_ASA);
    }

    @Override
    public void setupUI() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.start_activity);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spChooseActivity.setPrompt("Choose activity");
        spChooseActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().onSpinnerItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RxUtils.click(btnStartPause, o -> getPresenter().onBtnStartPauseClicked());
        RxUtils.click(btnStop, o -> getPresenter().onBtnStopClicked());
    }

    @Override
    public void setSpinnerData(List<String> _data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, _data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChooseActivity.setAdapter(adapter);
    }

    @Override
    public void setSpinnerEnabled(boolean _isEnabled) {
        spChooseActivity.setEnabled(_isEnabled);
    }

    @Override
    public void setTimer(String _text) {
        tvTimer.setText(_text);
    }

    @Override
    public int getSpinnerSelection() {
        return spChooseActivity.getSelectedItemPosition();
    }

    @Override
    public void onStartPauseClicked() {
        getPresenter().onBtnStartPauseClicked();
    }

    @Override
    public void toggleStartPause(String _text) {
        btnStartPause.setText(_text);
    }

    @Override
    public void onStopClicked() {
        getPresenter().onBtnStopClicked();
    }

    @Override
    public void setDistance(String _text) {
        tvDistance.setText(_text);
    }

    @Override
    public void setSpeed(String _text) {
        tvSpeed.setText(_text);
    }

    @Override
    public void setSteps(String _text) {
        tvSteps.setText(_text);
    }

    @Override
    public void setCalories(String _text) {
        tvCalories.setText(_text);
    }

    @Override
    public void setDistanceVisible(boolean _isVisible) {
        vgDistanceContainer.setVisibility(_isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSpeedVisible(boolean _isVisible) {
        vgSpeedContainer.setVisibility(_isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setStepsVisible(boolean _isVisible) {
        vgStepsContainer.setVisibility(_isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCaloriesVisible(boolean _isVisible) {
        vgCaloriesContainer.setVisibility(_isVisible ? View.VISIBLE : View.GONE);
    }

}
