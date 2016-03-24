package com.task.webchallengetask.data.data_providers;

import com.google.api.services.prediction.model.Output;
import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.data.rest.RestClient;
import com.task.webchallengetask.global.exceptions.PredictionException;
import com.task.webchallengetask.global.utils.PredictionManager;
import com.task.webchallengetask.global.utils.RxUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ProgramDataProvider extends BaseDataProvider {

    private static volatile ProgramDataProvider instance;
    private RestClient mRestClient = RestClient.getInstance();
    private DatabaseController mDatabaseController = DatabaseController.getInstance();
    private PredictionManager mPredictionManager = PredictionManager.getInstance();

    private ProgramDataProvider() {
    }

    public static ProgramDataProvider getInstance() {
        ProgramDataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (ProgramDataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProgramDataProvider();
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

    public Observable<List<ProgramTable>> getPrograms() {
        return newThread(mDatabaseController.getPrograms());
    }

    public Observable<List<ProgramTable>> getProgram(String _name) {
        return newThread(mDatabaseController.getProgram(_name));
    }

}
