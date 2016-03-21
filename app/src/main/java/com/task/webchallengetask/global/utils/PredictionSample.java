package com.task.webchallengetask.global.utils;

import android.content.Context;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class PredictionSample {

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private Context context;

    public PredictionSample(Context _context) {
        context = _context;
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GoogleCredential authorize() throws Exception {
        File keyFile = new File(context.getCacheDir().getPath() + "/temp");
        copyInputStreamToFile(context.getAssets().open(Constants.SERVICE_ACCT_KEYFILE), keyFile);

        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(Constants.SERVICE_ACCT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(keyFile)
                .setServiceAccountScopes(Arrays.asList(PredictionScopes.PREDICTION,
                        StorageScopes.DEVSTORAGE_READ_ONLY))
                .build();
    }

    public void run() {

        httpTransport = AndroidHttp.newCompatibleTransport();
        // authorization
        GoogleCredential credential = null;
        try {
            credential = authorize();
            Prediction prediction = new Prediction.Builder(
                    httpTransport, JSON_FACTORY, credential).setApplicationName(Constants.APPLICATION_NAME).build();

            insert(prediction)
                    .flatMap(insert2 -> {
                        try {
                            return getTrain(prediction);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return Observable.just("error");
                        }
                    })
                    .subscribe(s -> {
                        if (s.equals("DONE")) {
                            Logger.d("Training completed.");
                        } else {
                            Logger.d("Error!!!!!");
                        }
                    });
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


    @RxLogObservable
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

    private Observable<HttpResponse> getResponse(Prediction prediction) throws IOException {
        Logger.d("get response");
        return Observable.just(prediction.trainedmodels().get(Constants.PROJECT_ID, Constants.MODEL_ID).executeUnparsed());
    }

    @RxLogObservable
    private Observable<String> getTrain(Prediction prediction) throws IOException {

        Logger.d("Training started.");
        Logger.d("Waiting for training to complete");

        return getResponse(prediction)
                .repeat(10)
                .timeout(3, TimeUnit.SECONDS)
                .map(httpResponse1 -> {
                    try {
                        Logger.d(httpResponse1.getStatusMessage());
                        return httpResponse1.parseAs(Insert2.class).getTrainingStatus();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "error";
                    }
                }).first(s -> {
                    Logger.d(s);
                    return s == "DONE";
                });
    }

    private void train(Prediction prediction) throws IOException {


/*
        int triesCounter = 0;
        Insert2 trainingModel;
        while (triesCounter < 100) {
            // NOTE: if model not found, it will throw an HttpResponseException with a 404 error
            try {
                HttpResponse response = prediction.trainedmodels().get(Constants.PROJECT_ID, Constants.MODEL_ID).executeUnparsed();
                if (response.getStatusCode() == 200) {
                    trainingModel = response.parseAs(Insert2.class);
                    String trainingStatus = trainingModel.getTrainingStatus();
                    if (trainingStatus.equals("DONE")) {
                        Logger.d("Training completed.");
                        Logger.d("" + trainingModel.getModelInfo());
                        return;
                    }
                }
                response.ignore();
            } catch (HttpResponseException e) {
            }

            try {
                // 5 seconds times the tries counter
                Thread.sleep(5000 * (triesCounter + 1));
            } catch (InterruptedException e) {
                break;
            }
            System.out.print(".");
            System.out.flush();
            triesCounter++;
        }
        error("ERROR: training not completed.");
*/
    }

    private void error(String errorMessage) {
        Logger.d(errorMessage);
    }

    private Observable<Output> predict(Prediction prediction, String text) throws IOException {
        Input input = new Input();
        InputInput inputInput = new InputInput();
        inputInput.setCsvInstance(Collections.<Object>singletonList(text));
        input.setInput(inputInput);
        return Observable.just(prediction.trainedmodels().predict(Constants.PROJECT_ID, Constants.MODEL_ID, input).execute());
    }

}