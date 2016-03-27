package com.task.webchallengetask.ui.modules.activity.presenters;

import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.modules.activity.views.ActivityStartActivity;
import com.task.webchallengetask.ui.modules.activity.views.ActivityDetailFragment;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ActivityListPresenter extends BaseFragmentPresenter<ActivityListPresenter.ActivityListView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        ActivityDataProvider.getInstance()
                .getActivities()
                .doOnNext(this::fillList)
                .subscribe();

    }

    private void fillList(List<ActionParametersModel> _modelList) {
        if (!_modelList.isEmpty()) {

            long previousDay = _modelList.get(0).getStartTime();
            ActionParametersModel previousModel = _modelList.get(0);
            List<ActionParametersModel> currentList = new ArrayList<>();
            currentList.add(previousModel);

            for (int i = 1; i < _modelList.size(); i++) {
                long currentDay = _modelList.get(i).getStartTime();
                if (TimeUtil.compareDay(previousDay, currentDay) != 0) {
                    getView().addActivities(currentList);
                    currentList.clear();
                }
                currentList.add(_modelList.get(i));
                previousDay = currentDay;
            }

            getView().addActivities(currentList);
        }
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
