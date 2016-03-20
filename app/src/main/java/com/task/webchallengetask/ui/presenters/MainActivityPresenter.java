package com.task.webchallengetask.ui.presenters;

import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;

/**
 * Created by andri on 20.03.2016.
 */
public class MainActivityPresenter extends BaseActivityPresenter<MainActivityPresenter.MainView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setText("Example");

    }

    public interface MainView extends BaseView {
        void setText(String _text);
    }

}
