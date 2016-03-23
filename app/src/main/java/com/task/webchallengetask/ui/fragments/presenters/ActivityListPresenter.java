package com.task.webchallengetask.ui.fragments.presenters;

import com.task.webchallengetask.ui.activities.StartActivity;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;
import com.task.webchallengetask.ui.base.BaseView;

public class ActivityListPresenter extends BaseFragmentPresenter<ActivityListPresenter.ActivityListView> {


    public void onFABClicked() {
        getView().startActivity(StartActivity.class);
    }

    public interface ActivityListView extends BaseFragmentView<ActivityListPresenter> {

    }



}
