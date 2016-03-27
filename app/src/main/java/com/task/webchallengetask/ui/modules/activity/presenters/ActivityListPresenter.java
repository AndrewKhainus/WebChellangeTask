package com.task.webchallengetask.ui.modules.activity.presenters;

import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.modules.activity.views.ActivityStartActivity;
import com.task.webchallengetask.ui.modules.activity.views.ActivityDetailFragment;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

import java.util.ArrayList;
import java.util.List;

public class ActivityListPresenter extends BaseFragmentPresenter<ActivityListPresenter.ActivityListView> {

    private List<ActionParametersModel> actionParametersModels;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        actionParametersModels= new ArrayList<>();

        ActivityDataProvider.getInstance()
                .getActivities()
                .doOnNext(this::sortList)
                .subscribe();

    }

    private void sortList(List<ActionParametersModel> _modelList){
        long longTime = 0;
        int size = _modelList.size();
        for (int i = 0; i < size; i++) {
            if (i == 0){
                longTime = _modelList.get(0).getStartTime();
                actionParametersModels.add(_modelList.get(0));
                if (i == _modelList.size() - 1) getView().addActivities(actionParametersModels);
                continue;
            }
            if  (TimeUtil.isSameDay(longTime, _modelList.get(i).getStartTime())){
                actionParametersModels.add(_modelList.get(i));
                if (i == _modelList.size() - 1) getView().addActivities(actionParametersModels);
            }
            else {
                getView().addActivities(actionParametersModels);
                longTime = actionParametersModels.get(i).startTime;
                actionParametersModels.clear();
            }
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
