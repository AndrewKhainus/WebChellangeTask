package com.task.webchallengetask.ui.modules.activity.presenters;

import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;
import com.task.webchallengetask.ui.custom.CalendarView;

import java.util.Date;

/**
 * Created by klim on 25.03.16.
 */
public class ActivityDetailPresenter extends BaseFragmentPresenter<ActivityDetailPresenter.ActivityDetailView> {

    private int id;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        id = getView().getFragmentArguments().getInt(Constants.ACTIVITY_ID_KEY);

        ActivityDataProvider.getInstance().getActivityById(id)
                .subscribe(_model -> {
                    getView().setAllFieldsEditable(false);
                    getView().setDate(TimeUtil.dateToString(new Date(_model.getDate())));
                    float actualTime = _model.getActivityActualTime();
                    getView().setActivityTime(actualTime / 60); // from seconds
                    getView().setActivityTimeUint("min");
                    getView().setDistance(_model.getDistance());
                    getView().setStep(_model.getStep());
                    getView().setCalories(_model.getCalories());
                });
    }

    public void onEditClicked() {
        getView().setSaveVisible(true);
        getView().setEditVisible(false);
        getView().setAllFieldsEditable(true);
    }

    public void onSaveClicked() {
        getView().setSaveVisible(false);
        getView().setEditVisible(true);
        getView().setAllFieldsEditable(false);
        ActivityDataProvider.getInstance().getActivityById(id)
                .subscribe(model -> {
                    model.date = TimeUtil.stringToDate(getView().getDate()).getTime();
                    model.distance = Float.parseFloat(getView().getDistance());
                    float activityTime = Float.parseFloat(getView().getActivityTime());
                    model.activityActualTime = activityTime * 60; // to seconds
                    model.step = Integer.parseInt(getView().getStep());
                    model.calories = Float.parseFloat(getView().getCalories());
                    model.save();

                }, Logger::e);
    }

    public void onDeleteClicked() {
        ActivityDataProvider.getInstance()
                .deleteActivities(id)
                .subscribe(t -> getView().onBackPressed());
    }

    public void onTimeClicked() {
        getView().openStartDateCalendar(_date -> getView().setDate(TimeUtil.dateToString(_date)));
    }

    public interface ActivityDetailView extends BaseFragmentView<ActivityDetailPresenter> {
        void setEditVisible(boolean _isVisible);

        void setSaveVisible(boolean _isVisible);

        void openStartDateCalendar(CalendarView.Callback _callBack);

        void setTitle(String _text);

        void setDate(String _text);

        void setAllFieldsEditable(boolean _isEditable);

        void setActivityTime(float _text);

        void setActivityTimeUint(String _text);

        void setDistance(float _text);

        void setStep(float _text);

        void setCalories(float _text);

        String getActivityTime();

        String getDistance();

        String getStep();

        String getCalories();

        String getDate();
    }
}
