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
                    getView().setActivityTime(String.valueOf(_model.getActivityActualTime()));
                    getView().setDistance(String.valueOf(_model.getDistance()));
                    getView().setStep(String.valueOf(_model.getStep()));
                    getView().setCalories(String.valueOf(_model.getCalories()));
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
                    model.activityActualTime = Float.parseFloat(getView().getActivityTime());
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

        void setActivityTime(String _text);

        void setDistance(String _text);

        void setStep(String _text);

        void setCalories(String _text);

        String getActivityTime();

        String getDistance();

        String getStep();

        String getCalories();

        String getDate();
    }
}
