package com.task.webchallengetask.ui.fragments.presenters;

import com.task.webchallengetask.data.data_providers.AnalitycsDataProvider;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseView;

public class AnalyticsPresenter extends BaseFragmentPresenter<AnalyticsPresenter.AnalyticsView> {

    private AnalitycsDataProvider dataProvider = AnalitycsDataProvider.getInstance();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        dataProvider.getSomeData().subscribe(strings -> {});
    }

    public interface AnalyticsView extends BaseView {

    }

}
