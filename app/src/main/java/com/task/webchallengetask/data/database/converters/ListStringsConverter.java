package com.task.webchallengetask.data.database.converters;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.task.webchallengetask.data.database.models.ListStringsModel;

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class ListStringsConverter extends TypeConverter<String, ListStringsModel> {

    @Override
    public String getDBValue(ListStringsModel _model) {
        Gson gs = new Gson();
        return gs.toJson(_model);
    }

    @Override
    public ListStringsModel getModelValue(String _data) {
        Gson gs = new Gson();
        return gs.fromJson(_data, ListStringsModel.class);
    }


}
