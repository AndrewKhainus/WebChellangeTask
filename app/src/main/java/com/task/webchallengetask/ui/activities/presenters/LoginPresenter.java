package com.task.webchallengetask.ui.activities.presenters;

import com.task.webchallengetask.App;
import com.task.webchallengetask.global.utils.PredictionSample;
import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;

import rx.Observable;
import rx.schedulers.Schedulers;


public class LoginPresenter extends BaseActivityPresenter<LoginPresenter.LoginView> {

    public void onFacebookClicked() {
        getView().startActivity(MainActivity.class, null);
    }

    public void onGoogleClicked() {
        getView().showInfoDialog("Example", "Login with facebook", null);
    }

    public void onPredictionClicked() {
        try {

            Observable.just(null)
                    .subscribeOn(Schedulers.newThread())
                    .doOnNext(o -> {
                        PredictionSample sample = new PredictionSample(App.getAppContext());
                        try {
                            sample.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).subscribe();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface LoginView extends BaseView {

    }

}
