package com.task.webchallengetask.global.utils.cache;

import android.graphics.Bitmap;

import rx.Observable;

/**
 * Created by Sergbek on 21.03.2016.
 */
public class CacheImp implements Cache{

    @Override
    public Observable<Bitmap> get(int userId) {
        return null;
    }

    @Override
    public void put(Bitmap userEntity) {

    }

    @Override
    public boolean isCached(int userId) {
        return false;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void evictAll() {

    }
}
