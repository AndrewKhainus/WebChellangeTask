package com.task.webchallengetask.data.data_providers;

import com.google.api.services.prediction.model.Output;
import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.rest.RestClient;
import com.task.webchallengetask.global.exceptions.PredictionException;
import com.task.webchallengetask.global.utils.PredictionManager;
import com.task.webchallengetask.global.utils.RxUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class AnalyticsDataProvider extends BaseDataProvider {

    private static volatile AnalyticsDataProvider instance;
    private RestClient restClient = RestClient.getInstance();
    private DatabaseController databaseController = DatabaseController.getInstance();
    private PredictionManager mPredictionManager = PredictionManager.getInstance();

    private AnalyticsDataProvider() {
    }

    public static AnalyticsDataProvider getInstance() {
        AnalyticsDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (AnalyticsDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AnalyticsDataProvider();
                }
            }
        }
        return localInstance;
    }

    public Observable<String> analyzeWeeklyTrendResults(int completed, int failed) {

        List<Object> data = new ArrayList<>();
        data.add(String.valueOf(completed));
        data.add(String.valueOf(failed));

        return newThread(mPredictionManager.connect().flatMap(isTrained -> {
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

    public Observable<List<String>> getSomeData() {
        return newThread(RxUtils.emptyListObservable(String.class));
    }

}
