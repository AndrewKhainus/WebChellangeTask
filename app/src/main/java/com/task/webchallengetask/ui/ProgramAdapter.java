package com.task.webchallengetask.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.task.webchallengetask.global.programs.Program;

import java.util.List;

/**
 * Created by klim on 23.03.16.
 */
public class ProgramAdapter extends ArrayAdapter<String> {

    private List<Program> data;

    public ProgramAdapter(Context context, int resource, List<Program> _data) {
        super(context, resource);
        data = _data;
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getName();
    }

    @Override
    public int getCount() {
        return data.size();
    }

}
