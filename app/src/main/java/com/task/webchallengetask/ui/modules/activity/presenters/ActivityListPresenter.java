package com.task.webchallengetask.ui.modules.activity.presenters;

import android.content.Intent;

import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.modules.activity.views.ActivityStartActivity;
import com.task.webchallengetask.ui.modules.activity.views.ActivityDetailFragment;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                .exists(t ->t != null && t.size() == 0)
                .doOnNext(_boolean -> {
                    if (_boolean) getView().showHolder();
                    else getView().hideHolder();
                })
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
                longTime = _modelList.get(i).startTime;
                actionParametersModels.clear();
                if (i == _modelList.size() - 1) getView().addActivities(Collections.singletonList(_modelList.get(i)));
            }
        }
    }

    public void onFABClicked() {
        getView().startActivityForResult(ActivityStartActivity.class, Constants.RC_ACTIVITY_START_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onActivityClicked(ActionParametersModel _model) {
        getView().switchFragment(ActivityDetailFragment.newInstance(_model.id), true);
    }

    public interface ActivityListView extends BaseFragmentView<ActivityListPresenter> {
        void addActivities(List<ActionParametersModel> _data);
        void showHolder();
        void hideHolder();
    }

}
