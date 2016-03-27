package com.task.webchallengetask.ui.modules.activity.views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.custom.CalendarView;
import com.task.webchallengetask.ui.modules.activity.presenters.ActivityDetailPresenter;
import com.task.webchallengetask.ui.base.BaseFragment;

/**
 * Created by klim on 25.03.16.
 */
public class ActivityDetailFragment extends BaseFragment<ActivityDetailPresenter>
        implements ActivityDetailPresenter.ActivityDetailView {

    private MenuItem menuEdit;
    private MenuItem menuSave;
    private TextView tvTitle;
    private TextView tvDate;
    private EditText etActivityTime;
    private EditText etStep;
    private EditText etDistance;
    private EditText etCalories;

    public static ActivityDetailFragment newInstance(int _id) {

        Bundle args = new Bundle();
        args.putInt(Constants.ACTIVITY_ID_KEY, _id);
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getTitle() {
        return R.string.activity_detail;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_activity_details;
    }

    @Override
    public ActivityDetailPresenter initPresenter() {
        return new ActivityDetailPresenter();
    }

    @Override
    public void findUI(View rootView) {
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle_FAD);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate_FAD);
        etActivityTime = (EditText) rootView.findViewById(R.id.etActivityTime_FAD);
        etDistance = (EditText) rootView.findViewById(R.id.etDistance_FAD);
        etStep = (EditText) rootView.findViewById(R.id.etStep_FAD);
        etCalories = (EditText) rootView.findViewById(R.id.etCalories_FAD);
    }

    @Override
    public void setupUI() {
        tvDate.setOnClickListener(v -> getPresenter().onTimeClicked());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_detail, menu);
        menuEdit = menu.findItem(R.id.menu_edit);
        menuSave = menu.findItem(R.id.menu_save);
        menuSave.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                getPresenter().onEditClicked();
                return true;
            case R.id.menu_save:
                getPresenter().onSaveClicked();
                return true;
            case R.id.menu_delete:
                getPresenter().onDeleteClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setEditVisible(boolean _isVisible) {
        menuEdit.setVisible(_isVisible);
    }

    @Override
    public void setSaveVisible(boolean _isVisible) {
        menuSave.setVisible(_isVisible);
    }

    @Override
    public void openStartDateCalendar(CalendarView.Callback _callBack) {
        CalendarView calendarView = new CalendarView(getFragmentManager(), "");
        calendarView.setCallback(_callBack);
        calendarView.show(TimeUtil.stringToDate(tvDate.getText().toString()));
    }

    @Override
    public void setTitle(String _text) {
        tvTitle.setText(_text);
    }

    @Override
    public void setDate(String _text) {
        tvDate.setText(_text);
    }

    @Override
    public void setAllFieldsEditable(boolean _isEditable) {
        etActivityTime.setEnabled(_isEditable);
        etDistance.setEnabled(_isEditable);
        etStep.setEnabled(_isEditable);
        etCalories.setEnabled(_isEditable);
        tvDate.setFocusable(_isEditable);
        tvDate.setEnabled(_isEditable);
    }

    @Override
    public void setActivityTime(String _text) {
        etActivityTime.setText(_text);
    }

    @Override
    public void setDistance(String _text) {
        etDistance.setText(_text);
    }

    @Override
    public void setStep(String _text) {
        etStep.setText(_text);
    }

    @Override
    public void setCalories(String _text) {
        etCalories.setText(_text);
    }

    @Override
    public String getActivityTime() {
        return etActivityTime.getText().toString();
    }

    @Override
    public String getDistance() {
        return etDistance.getText().toString();
    }

    @Override
    public String getStep() {
        return etStep.getText().toString();
    }

    @Override
    public String getCalories() {
        return etCalories.getText().toString();
    }

    @Override
    public String getDate() {
        return tvDate.getText().toString();
    }
}
