package com.task.webchallengetask.data.data_providers;

import android.util.Pair;

import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.data.database.DatabaseController;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.programs.Program;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.global.utils.TimeUtil;

import java.util.Date;
import java.util.List;

import rx.Observable;

public class ProgramDataProvider extends BaseDataProvider {

    private static volatile ProgramDataProvider instance;
    private DatabaseController mDatabaseController = DatabaseController.getInstance();
    private ActivityDataProvider mActivityDataProvider = ActivityDataProvider.getInstance();

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

    public Observable<ProgramTable> getProgram(int _id) {
        return newThread(mDatabaseController.getProgram(_id));
    }

    public Observable<List<Pair<Long, Float>>> loadData(Constants.PROGRAM_TYPES _type,
                                                        Date _startDate, Date _endDate) {

        switch (_type) {
            case ACTIVE_LIFE:
                return mActivityDataProvider.getActualTime(_startDate, _endDate);

            case LONG_DISTANCE:
                return mActivityDataProvider.getDistance(_startDate, _endDate);
        }
        return null;
    }

}
