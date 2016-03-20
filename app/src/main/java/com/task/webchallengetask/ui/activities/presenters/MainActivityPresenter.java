package com.task.webchallengetask.ui.activities.presenters;

import android.graphics.Bitmap;

import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;
import com.task.webchallengetask.ui.fragments.ActivityListFragment;
import com.task.webchallengetask.ui.fragments.AnalyticsFragment;
import com.task.webchallengetask.ui.fragments.SettingsFragment;

public class MainActivityPresenter extends BaseActivityPresenter<MainActivityPresenter.MainView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().switchFragment(ActivityListFragment.newInstance(), false);
        getView().setHeaderTitle("Cool men");
        getView().setHeaderSubTitle("Who care about his health");
    }

    @Override
    public void onBackPressed() {
        if (getView().isDrawerOpen()) {
            getView().closeDrawer(null);
        } else
            super.onBackPressed();
    }

    public void onActivityListClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(ActivityListFragment.newInstance(), false));
    }

    public void onAnalyticsClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(AnalyticsFragment.newInstance(), false));
    }

    public void onSettingsClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(SettingsFragment.newInstance(), false));
    }

    public void onLogoutClicked() {
        getView().showConfirmDialog("Confirm",
                "Do you really want to logout?",
                v -> getView().closeDrawer(null));
    }

    public interface MainView extends BaseView {
        boolean isDrawerOpen();
        void closeDrawer(MainActivity.DrawerCallBack _callback);
        void setHeaderAvatar(Bitmap _bitmap);
        void setHeaderSubTitle(String _description);
        void setHeaderTitle(String _title);

    }

}