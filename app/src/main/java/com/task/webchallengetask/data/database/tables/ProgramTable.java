package com.task.webchallengetask.data.database.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.task.webchallengetask.data.database.FitDatabase;
import com.task.webchallengetask.data.database.converters.ProgramConverter;
import com.task.webchallengetask.global.programs.Program;

/**
 * Created by klim on 23.03.16.
 */

@Table(database = FitDatabase.class)
public class ProgramTable extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column(typeConverter = ProgramConverter.class)
    public Program program;

    @Column
    public String actualResult;


}
