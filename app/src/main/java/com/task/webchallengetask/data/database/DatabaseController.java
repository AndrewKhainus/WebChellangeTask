package com.task.webchallengetask.data.database;


import com.raizlabs.android.dbflow.sql.language.Select;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.data.database.tables.ProgramTable_Table;

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

    public List<ActionParametersModel> getActionParametersModel(){
        return new Select().from(ActionParametersModel.class).queryList();
    }

    public Observable<List<ProgramTable>> getPrograms() {
        return Observable.from(new Select().from(ProgramTable.class).queryList()).toList();
    }

    public Observable<List<ProgramTable>> getProgram(String _name) {
        return Observable.just(new Select().from(ProgramTable.class)
                .where(ProgramTable_Table.name.eq(_name))
                .orderBy(ProgramTable_Table.date, false)
                .queryList());
    }

}
