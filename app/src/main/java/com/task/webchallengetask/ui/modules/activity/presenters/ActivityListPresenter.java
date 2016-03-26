package com.task.webchallengetask.ui.modules.activity.presenters;

import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.modules.activity.views.ActivityStartActivity;
import com.task.webchallengetask.ui.modules.activity.views.ActivityDetailFragment;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityListPresenter extends BaseFragmentPresenter<ActivityListPresenter.ActivityListView> {

    private List<ActionParametersModel> mActivities = new ArrayList<>();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        // TODO: put real activities
        ActionParametersModel model1 = new ActionParametersModel();
        model1.startTime = TimeUtil.minusDayFromDate(new Date(), 1).getTime();
        model1.endTime = new Date().getTime();
        model1.name = "running";
        mActivities.add(model1);
        List<ActionParametersModel> currentDayList1 = new ArrayList<>();
        currentDayList1.add(model1);
        getView().addActivities(currentDayList1);

        ActionParametersModel model2 = new ActionParametersModel();
        model2.startTime = TimeUtil.minusDayFromDate(new Date(), 2).getTime();
        model2.endTime = new Date().getTime();
        model2.name = "walking";
        List<ActionParametersModel> currentDayList2 = new ArrayList<>();
        currentDayList2.add(model2);
        mActivities.add(model2);
        getView().addActivities(currentDayList2);

    }

    public void onFABClicked() {
        getView().startActivity(ActivityStartActivity.class);
    }

    public void onActivityClicked(ActionParametersModel _model) {
        getView().switchFragment(ActivityDetailFragment.newInstance(_model.id), true);
    }

    public interface ActivityListView extends BaseFragmentView<ActivityListPresenter> {
        void addActivities(List<ActionParametersModel> _data);
    }

}
