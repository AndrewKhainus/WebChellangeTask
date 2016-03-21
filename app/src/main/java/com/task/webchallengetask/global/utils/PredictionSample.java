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
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class PredictionSample {

    private JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private HttpTransport httpTransport;

    public PredictionSample() {
        httpTransport = AndroidHttp.newCompatibleTransport();
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
                httpTransport, JSON_FACTORY, credential).setApplicationName(Constants.APPLICATION_NAME).build());        }

    public void run() {

        Logger.d("Training started.");
        try {
            authorize()
                    .flatMap(this::createPrediction)
                    .doOnNext(this::insert)
                    .flatMap(this::getTrain)
                    .subscribe(s -> Logger.d("Training completed, result = " + s),
                            Logger::e);
//            train(prediction);
/*
            predict(prediction, "Is this sentence in English?").subscribe(output -> Logger.d("Predicted language: " + output.getOutputLabel()));
            predict(prediction, "¿Es esta frase en Español?").subscribe(output -> Logger.d("Predicted language: " + output.getOutputLabel()));
            predict(prediction, "Est-ce cette phrase en Français?").subscribe(output -> Logger.d("Predicted language: " + output.getOutputLabel()));
*/

        } catch (Exception e) {
            e.printStackTrace();
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

    private Observable<String> get(Prediction prediction) {
        Logger.d("get response");
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

    private Observable<String> getTrain(Prediction prediction) {
        return get(prediction)
                .retryWhen(observable -> observable
                        .flatMap(errors -> {
                            if (errors instanceof UncomplitedException)
                                return Observable.just(null);
                            else return Observable.error(errors);
                        })
                        .zipWith(Observable.range(0, 15), (o, integer) -> integer)
                        .flatMap(retryCount -> Observable.timer(5, TimeUnit.SECONDS)));
    }


    private Observable<Output> predict(Prediction prediction, String text) throws IOException {
        Input input = new Input();
        InputInput inputInput = new InputInput();
        inputInput.setCsvInstance(Collections.<Object>singletonList(text));
        input.setInput(inputInput);
        return Observable.just(prediction.trainedmodels().predict(Constants.PROJECT_ID, Constants.MODEL_ID, input).execute());
    }

}