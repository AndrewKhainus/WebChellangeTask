package com.task.webchallengetask.ui.modules.analytics;


import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.custom.CalendarView;
import com.task.webchallengetask.ui.base.BaseFragment;

import java.util.List;

public class AnalyticsFragment extends BaseFragment<AnalyticsPresenter>
        implements AnalyticsPresenter.AnalyticsView {

    private Spinner spDataType;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private FrameLayout lDiagramContainer;

    public static AnalyticsFragment newInstance() {

        Bundle args = new Bundle();

        AnalyticsFragment fragment = new AnalyticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getTitle() {
        return R.string.analytics;
    }

    @Override
    public final int getLayoutResource() {
        return R.layout.fragment_analytics;
    }

    @Override
    public final  AnalyticsPresenter initPresenter() {
        return new AnalyticsPresenter();
    }

    @Override
    public final  void findUI(View rootView) {
        spDataType = (Spinner) rootView.findViewById(R.id.spDataType_FA);
        tvStartDate = (TextView) rootView.findViewById(R.id.tvStartDate_FA);
        tvEndDate = (TextView) rootView.findViewById(R.id.tvEndDate_FA);
        lDiagramContainer = (FrameLayout) rootView.findViewById(R.id.diagram_container_FA);

    }

    @Override
    public final  void setupUI() {
        spDataType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().onDataTypeChoosed(((DataTypesAdapter)spDataType.getAdapter()).getSelected(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RxUtils.click(tvStartDate, o -> getPresenter().onStartDateClicked());
        RxUtils.click(tvEndDate, o -> getPresenter().onEndDateClicked());

    }

    @Override
    public void setDiagram() {

    }

    @Override
    public void openStartDateCalendar(CalendarView.Callback _callBack) {
        CalendarView calendarView = new CalendarView(getFragmentManager(), "");
        calendarView.setCallback(_callBack);
        calendarView.show(TimeUtil.parseDate(tvStartDate.getText().toString()));

    }

    @Override
    public void openEndDateCalendar(CalendarView.Callback _callBack) {
        CalendarView calendarView = new CalendarView(getFragmentManager(), "");
        calendarView.setCallback(_callBack);
        calendarView.show(TimeUtil.parseDate(tvEndDate.getText().toString()));
    }

    @Override
    public void setStartDate(String _text) {
        tvStartDate.setText(_text);
    }

    @Override
    public void setEndDate(String _text) {
        tvEndDate.setText(_text);
    }

    @Override
    public void setDataTypes(List<Pair<Constants.DATA_TYPES, String>> _data) {
        ArrayAdapter<String> adapter = new DataTypesAdapter(this.getContext(), android.R.layout.simple_spinner_item, _data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDataType.setAdapter(adapter);
    }

    @Override
    public String getStartDate() {
        return tvStartDate.getText().toString();
    }

    @Override
    public String getEndDate() {
        return tvEndDate.getText().toString();
    }
}
