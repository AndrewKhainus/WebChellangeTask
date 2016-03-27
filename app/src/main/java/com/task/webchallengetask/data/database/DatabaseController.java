package com.task.webchallengetask.data.database;


import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.data.database.tables.ActionParametersModel_Table;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.data.database.tables.ProgramTable_Table;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;


public class DatabaseController {

    private static volatile DatabaseController instance;

    public static DatabaseController getInstance() {
        DatabaseController localInstance = instance;
        if (localInstance == null) {
            synchronized (DatabaseController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DatabaseController();
                }
            }
        }
        return localInstance;
    }

    public List<ActionParametersModel> getActionParametersModel(long _startDate, long _endDate){
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(ActionParametersModel_Table.startTime.greaterThanOrEq(_startDate));
        conditionGroup.and(ActionParametersModel_Table.startTime.lessThan(_endDate));

        return new Select().from(ActionParametersModel.class)
                .where(conditionGroup)
                .orderBy(ActionParametersModel_Table.startTime, true)
                .queryList();
    }

    public List<ActionParametersModel> getAllActionParametersModel(){

        return new Select().from(ActionParametersModel.class)
                .orderBy(ActionParametersModel_Table.startTime, true)
                .queryList();
    }

    public ActionParametersModel getActionParametersModel(int _id){
        return new Select().from(ActionParametersModel.class)
                .where(ActionParametersModel_Table.id.eq(_id))
                .querySingle();
    }

    public boolean deleteActionParametersModel(int _id){
        new Delete().from(ActionParametersModel.class)
                .where(ActionParametersModel_Table.id.eq(_id))
                .query();

        return true;
    }

    public Observable<List<ProgramTable>> getPrograms() {
        return Observable.from(new Select().from(ProgramTable.class).queryList()).toList();
    }

    public Observable<ProgramTable> getProgram(int _id) {
        return Observable.just(new Select().from(ProgramTable.class)
                .where(ProgramTable_Table.id.eq(_id))
                .querySingle());
    }

}
