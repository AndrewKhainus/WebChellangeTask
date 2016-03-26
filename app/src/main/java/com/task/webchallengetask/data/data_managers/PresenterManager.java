package com.task.webchallengetask.data.data_managers;

import android.os.Bundle;
import android.util.LruCache;

import com.task.webchallengetask.ui.base.BasePresenter;

import java.util.concurrent.atomic.AtomicLong;

public final class PresenterManager {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


    private static final String SIS_KEY_PRESENTER_ID = "presenter_id";
    private static PresenterManager instance;

    private final AtomicLong currentId;

    private final LruCache<Long, BasePresenter> presenters;

    PresenterManager() {
        currentId = new AtomicLong();
        presenters = new LruCache<>( maxMemory / 4);
    }

    public static PresenterManager getInstance() {
        if (instance == null) {
            instance = new PresenterManager();
        }
        return instance;
    }

    public <P extends BasePresenter> P restorePresenter(Bundle savedInstanceState) {
        Long presenterId = savedInstanceState.getLong(SIS_KEY_PRESENTER_ID);
        P presenter = (P) presenters.get(presenterId);
        return presenter;
    }

    public void savePresenter(BasePresenter presenter, Bundle outState) {
        long presenterId = currentId.incrementAndGet();
        presenters.put(presenterId, presenter);
        outState.putLong(SIS_KEY_PRESENTER_ID, presenterId);
    }
}