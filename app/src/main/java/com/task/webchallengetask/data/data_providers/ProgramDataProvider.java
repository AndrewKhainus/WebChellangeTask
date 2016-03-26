package com.task.webchallengetask.data.data_providers;

import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.database.tables.ProgramTable;

import java.util.List;

import rx.Observable;

public class ProgramDataProvider extends BaseDataProvider {

    private static volatile ProgramDataProvider instance;
    private DatabaseController mDatabaseController = DatabaseController.getInstance();


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

    public Observable<List<ProgramTable>> getPrograms() {
        return newThread(mDatabaseController.getPrograms());
    }

    public Observable<List<ProgramTable>> getProgram(String _name) {
        return newThread(mDatabaseController.getProgram(_name));
    }

}
