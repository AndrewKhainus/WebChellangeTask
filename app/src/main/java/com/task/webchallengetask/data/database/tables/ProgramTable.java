package com.task.webchallengetask.data.database.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.task.webchallengetask.data.database.FitDatabase;

/**
 * Created by klim on 23.03.16.
 */

@Table(database = FitDatabase.class)
public class ProgramTable extends BaseModel {

    @PrimaryKey
    @Column
    public String name;

    @Column
    public String difficult;

    @Column
    public String target;

    @Column
    public String unit;

    public String getName() {
        return name;
    }

    public String getDifficult() {
        return difficult;
    }

    public String getTarget() {
        return target;
    }

    public String getUnit() {
        return unit;
    }
}
