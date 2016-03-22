package com.task.webchallengetask.global.utils.cache;

import android.graphics.Bitmap;

import com.google.gson.Gson;

/**
 * Created by Sergbek on 21.03.2016.
 */
public class JsonSerializer {
    private final Gson gson = new Gson();


    /**
     * Serialize an object to Json.
     *
     * @param userEntity {@link Bitmap} to serialize.
     */
    public String serialize(Bitmap userEntity) {
        String jsonString = gson.toJson(userEntity, Bitmap.class);
        return jsonString;
    }

    /**
     * Deserialize a json representation of an object.
     *
     * @param jsonString A json string to deserialize.
     * @return {@link Bitmap}
     */
    public Bitmap deserialize(String jsonString) {
        Bitmap userEntity = gson.fromJson(jsonString, Bitmap.class);
        return userEntity;
    }
}
