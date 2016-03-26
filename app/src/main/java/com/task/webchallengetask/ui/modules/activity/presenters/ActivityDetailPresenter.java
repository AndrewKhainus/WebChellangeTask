package com.task.webchallengetask.ui.modules.activity.presenters;

import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

/**
 * Created by klim on 25.03.16.
 */
public class ActivityDetailPresenter extends BaseFragmentPresenter<ActivityDetailPresenter.ActivityDetailView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        final int id = getView().getFragmentArguments().getInt(Constants.ACTIVITY_ID_KEY);

        // TODO: get real activity from DB
        ActionParametersModel model = new ActionParametersModel();
        model.name = "StepByStep";
        model.startTime = TimeUtil.parseTime("12:00:00").getTime();
        model.endTime = TimeUtil.parseTime("12:30:00").getTime();
        model.activityActualTime = 30;
        model.calories = 3000;
        model.step = 1500;
        model.distance = 1234;

        getView().setAllFieldsEditable(false);
        getView().setStartTime(TimeUtil.timeToString(model.getStartTime()));
        getView().setEndTime(TimeUtil.timeToString(model.getEndTime()));
        getView().setActivityTime(String.valueOf(model.getActivityActualTime()));
        getView().setDistance(String.valueOf(model.getDistance()));
        getView().setStep(String.valueOf(model.getStep()));
        getView().setCalories(String.valueOf(model.getCalories()));

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
        // TODO: save to db
    }

    public interface ActivityDetailView extends BaseFragmentView<ActivityDetailPresenter> {
        void setEditVisible(boolean _isVisible);
        void setSaveVisible(boolean _isVisible);
        void setTitle(String _text);
        void setStartTime(String _text);
        void setEndTime(String _text);
        void setAllFieldsEditable(boolean _isEditable);
        void setActivityTime(String _text);
        void setDistance(String _text);
        void setStep(String _text);
        void setCalories(String _text);
        String getActivityTime();
        String getDistance();
        String getStep();
        String getCalories();
    }
}
