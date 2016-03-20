package com.task.webchallengetask.ui.fragments.presenters;

import com.task.webchallengetask.data.data_providers.AnalyticsDataProvider;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseView;

public class AnalyticsPresenter extends BaseFragmentPresenter<AnalyticsPresenter.AnalyticsView> {

    private AnalyticsDataProvider dataProvider = AnalyticsDataProvider.getInstance();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        dataProvider.getSomeData().subscribe(strings -> {});
    }

    public interface AnalyticsView extends BaseView {

    }

}
