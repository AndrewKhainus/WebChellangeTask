package com.task.webchallengetask.data.database.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.task.webchallengetask.data.database.FitDatabase;

/**
 * Created by Sergbek on 24.03.2016.
 */
@Table(database = FitDatabase.class)
public class ActionParametersModel extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column
    public long startTime;

    @Column
    public long endTime;

    @Column
    public float activityActualTime;

    @Column
    public int step;

    @Column
    public float distance;

    @Column
    public float speed;

    @Column
    public float calories;

    @Column
    public boolean synchroniz;
}
