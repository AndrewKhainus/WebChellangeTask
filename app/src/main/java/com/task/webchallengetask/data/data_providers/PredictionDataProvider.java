package com.task.webchallengetask.data.data_providers;

import com.google.api.services.prediction.model.Output;
import com.task.webchallengetask.global.exceptions.PredictionException;
import com.task.webchallengetask.data.data_managers.PredictionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Created by klim on 25.03.16.
 */
public class PredictionDataProvider extends BaseDataProvider {

    private static volatile PredictionDataProvider instance;


    private PredictionDataProvider() {
    }

    public static PredictionDataProvider getInstance() {
        PredictionDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (PredictionDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PredictionDataProvider();
                }
            }
        }
        return localInstance;
    }


    private PredictionManager mPredictionManager = PredictionManager.getInstance();

    public Observable<String> analyzeWeeklyTrendResults(int completed, int failed) {

        List<Object> data = new ArrayList<>();
        data.add(String.valueOf(completed));
        data.add(String.valueOf(failed));

        return newThread(mPredictionManager.connectToGoogleApi().flatMap(isTrained -> {
            if (isTrained) {
                try {
                    return mPredictionManager.predict(data)
                            .map(Output::getOutputLabel);
                } catch (IOException e) {
                    return Observable.error(e);
                }
            } else return Observable.error(new PredictionException("Trained failed"));
        }));

    }

    public Observable<Boolean> connectAndTrain() {
        ConnectableObservable connectableObservable=  PredictionManager.getInstance().connectToGoogleApi();
        PredictionManager.getInstance().connectToGoogleApi().connect();
        return connectableObservable;
    }

}
