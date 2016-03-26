package com.task.webchallengetask.ui.modules.analytics;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.custom.CalendarView;
import com.task.webchallengetask.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsFragment extends BaseFragment<AnalyticsPresenter>
        implements AnalyticsPresenter.AnalyticsView {

    private Spinner spDataType;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private FrameLayout lDiagramContainer;
    private CombinedChart mChart;
    private final int itemcount = 12;

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
        mChart = (CombinedChart) rootView.findViewById(R.id.chart_FA);
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

    public String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    @Override
    public void setDiagram(ArrayList<Entry> _data) {
        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        CombinedData data = new CombinedData(mMonths);

        data.setData(generateBarData());

        mChart.setData(data);
        mChart.invalidate();
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    private BarData generateBarData() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new BarEntry(getRandom(15, 30), index));

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
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
