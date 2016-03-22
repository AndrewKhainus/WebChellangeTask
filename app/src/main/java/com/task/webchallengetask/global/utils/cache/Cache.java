package com.task.webchallengetask.global.utils.cache;

import android.graphics.Bitmap;

import rx.Observable;

/**
 * Created by Sergbek on 21.03.2016.
 */
public interface Cache {
    /**
     * Gets an {@link rx.Observable} which will emit a {@link Bitmap}.
     *
     * @param userId The user id to retrieve data.
     */
    Observable<Bitmap> get(final int userId);

    /**
     * Puts and element into the cache.
     *
     * @param userEntity Element to insert in the cache.
     */
    void put(Bitmap userEntity);

    /**
     * Checks if an element (User) exists in the cache.
     *
     * @param userId The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final int userId);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired();

    /**
     * Evict all elements of the cache.
     */
    void evictAll();
}
