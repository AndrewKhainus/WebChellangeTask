package com.task.webchallengetask.ui.modules.activity.presenters;

import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

/**
 * Created by klim on 25.03.16.
 */
public class ActivityDetailPresenter extends BaseFragmentPresenter<ActivityDetailPresenter.ActivityDetailView> {

    private ActionParametersModel actionParametersModel;
    private int id;
    @Override
    public void onViewCreated() {
        super.onViewCreated();
        id = getView().getFragmentArguments().getInt(Constants.ACTIVITY_ID_KEY);

        ActivityDataProvider.getInstance().getActivitie(id)
                .subscribe(_model -> {
                    actionParametersModel = _model;
                    getView().setAllFieldsEditable(false);
                    getView().setStartTime(TimeUtil.timeToString(_model.getStartTime()));
                    getView().setEndTime(TimeUtil.timeToString(_model.getEndTime()));
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
        actionParametersModel.distance = Float.parseFloat(getView().getDistance());
        actionParametersModel.activityActualTime = Float.parseFloat(getView().getActivityTime());
        actionParametersModel.step = Integer.parseInt(getView().getStep());
        actionParametersModel.calories = Float.parseFloat(getView().getCalories());
        actionParametersModel.save();
    }

    public void onDeleteClicked() {
        ActivityDataProvider.getInstance()
                .deleteActivities(id)
                .subscribe(t -> getView().onBackPressed());
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
