package com.task.webchallengetask.global.utils;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.api.services.prediction.model.Insert;
import com.google.api.services.prediction.model.Insert2;
import com.google.api.services.prediction.model.Output;
import com.google.api.services.storage.StorageScopes;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.exceptions.UncomplitedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class PredictionManager {

    private static volatile PredictionManager instance;
    private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private HttpTransport httpTransport;
    private Prediction mPrediction;

    private PredictionManager() {
    }

    public static PredictionManager getInstance() {
        PredictionManager localInstance = instance;
        if (localInstance == null) {
            synchronized (PredictionManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PredictionManager();
                }
            }
        }
        return localInstance;
    }

    private Observable<GoogleCredential> authorize() {


        try {
            return Observable.just(new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId(Constants.SERVICE_ACCT_EMAIL)
                    .setServiceAccountPrivateKeyFromP12File(AssetManager.getFile(Constants.SERVICE_ACCT_KEYFILE))
                    .setServiceAccountScopes(Arrays.asList(PredictionScopes.PREDICTION,
                            StorageScopes.DEVSTORAGE_READ_ONLY))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return Observable.error(e);
        }
    }

    private Observable<Prediction> createPrediction(GoogleCredential credential) {
        return Observable.just(new Prediction.Builder(
                httpTransport, JSON_FACTORY, credential).setApplicationName(Constants.APPLICATION_NAME).build());
    }


    public Observable<Boolean> connect() {
        if (httpTransport == null)
            httpTransport = AndroidHttp.newCompatibleTransport();

        if (mPrediction != null) {
            return Observable.just(true);
        } else {
            return authorize()
                    .flatMap(this::createPrediction)
                    .doOnNext(prediction -> mPrediction = prediction)
                    .doOnNext(this::insert)
                    .flatMap(this::getTrain);
        }
    }

    private Observable<Insert2> insert(Prediction prediction) {
        Insert trainingData = new Insert();
        trainingData.setId(Constants.MODEL_ID);
        trainingData.setStorageDataLocation(Constants.STORAGE_DATA_LOCATION);

        try {
            return Observable.just(prediction.trainedmodels().insert(Constants.PROJECT_ID, trainingData).execute());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    private Observable<String> checkTraining(Prediction prediction) {
        Logger.d("checkTraining response");
        return Observable.just(true)
                .map(aBoolean1 -> {
                    try {
                        return prediction.trainedmodels().get(Constants.PROJECT_ID, Constants.MODEL_ID).executeUnparsed();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .flatMap(httpResponse -> {
                    try {
                        String result = httpResponse.parseAs(Insert2.class).getTrainingStatus();
                        if (result.equals("DONE")) return Observable.just(result);
                        else return Observable.error(new UncomplitedException());
                    } catch (IOException e) {
                        return Observable.error(e);
                    }
                });
    }

    private Observable<Boolean> getTrain(Prediction prediction) {
        return checkTraining(prediction)
                .retryWhen(observable -> observable
                        .flatMap(errors -> {
                            if (errors instanceof UncomplitedException)
                                return Observable.just(null);
                            else return Observable.error(errors);
                        })
                        .zipWith(Observable.range(0, 15), (o, integer) -> integer)
                        .flatMap(retryCount -> Observable.timer(5, TimeUnit.SECONDS)))
                .map(s -> s.equals("DONE"));
    }


    public Observable<Output> predict(List<Object> data) throws IOException {
        Input input = new Input();
        InputInput inputInput = new InputInput();
        inputInput.setCsvInstance(data);
        input.setInput(inputInput);
        return Observable.just(mPrediction.trainedmodels().predict(Constants.PROJECT_ID, Constants.MODEL_ID, input).execute());
    }

}