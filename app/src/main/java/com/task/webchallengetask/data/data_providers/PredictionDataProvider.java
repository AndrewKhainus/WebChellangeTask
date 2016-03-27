package com.task.webchallengetask.data.data_providers;

import com.google.api.services.prediction.model.Output;
import com.task.webchallengetask.data.data_managers.PredictionManager;
import com.task.webchallengetask.global.exceptions.PredictionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

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

        return newThread(mPredictionManager.getTrainForPredict().flatMap(isTrained -> {
            if (isTrained) {
                try {
                    return newThread(mPredictionManager.predict(data)
                            .map(Output::getOutputLabel));
                } catch (IOException e) {
                    return newThread(Observable.error(e));
                }
            } else return newThread(Observable.error(new PredictionException("Trained failed")));
        }));

    }

    public Observable<Boolean> connectAndTrain() {
        return newThread(PredictionManager.getInstance().connectToGoogleApi());
    }

}
