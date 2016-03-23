package com.task.webchallengetask.data.database.converters;


import com.google.gson.Gson;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.task.webchallengetask.global.programs.Program;

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class ProgramConverter extends TypeConverter<String, Program> {

    @Override
    public String getDBValue(Program _model) {
        Gson gs = new Gson();
        return gs.toJson(_model);
    }

    @Override
    public Program getModelValue(String _data) {
        Gson gs = new Gson();
        return gs.fromJson(_data, Program.class);
    }
}
