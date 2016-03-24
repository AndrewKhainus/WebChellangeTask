package com.task.webchallengetask.ui.fragments.presenters;

import com.task.webchallengetask.data.data_providers.ProgramDataProvider;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

public class AnalyticsPresenter extends BaseFragmentPresenter<AnalyticsPresenter.AnalyticsView> {

    private ProgramDataProvider dataProvider = ProgramDataProvider.getInstance();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        dataProvider.analyzeWeeklyTrendResults(7, 0)
                .subscribe(s -> getView().showInfoDialog(s, s, null), Logger::e);


    }
    public interface AnalyticsView extends BaseFragmentView<AnalyticsPresenter> {

    }

}
