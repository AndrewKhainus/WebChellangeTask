package com.task.webchallengetask.ui.modules.analytics;

import android.graphics.Color;
import android.support.v4.util.Pair;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.data_providers.ProgramDataProvider;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.custom.CalendarView;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalyticsPresenter extends BaseFragmentPresenter<AnalyticsPresenter.AnalyticsView> {

    private ActivityDataProvider mActivitiesProvider = ActivityDataProvider.getInstance();
    public static final Pair<Constants.DATA_TYPES, String> activityDataType = new Pair<>(Constants.DATA_TYPES.ACTIVITY_TIME, "Activity time");
    public static final Pair<Constants.DATA_TYPES, String> stepDataType = new Pair<>(Constants.DATA_TYPES.STEP, "Steps");
    public static final Pair<Constants.DATA_TYPES, String> distanceDataType = new Pair<>(Constants.DATA_TYPES.DISTANCE, "Distance");
    public static final Pair<Constants.DATA_TYPES, String> caloriesDataType = new Pair<>(Constants.DATA_TYPES.CALORIES, "Calories");
//    public static final Pair<Constants.DATA_TYPES, String> allDataType = new Pair(Constants.DATA_TYPES.All, "All");

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        List<Pair<Constants.DATA_TYPES, String>> dataTypesList = new ArrayList<>();
        dataTypesList.add(activityDataType);
        dataTypesList.add(stepDataType);
        dataTypesList.add(distanceDataType);
        dataTypesList.add(caloriesDataType);
//        dataTypesList.add(allDataType);
        getView().setDataTypes(dataTypesList);

        Date currentDay = new Date(TimeUtil.getCurrentDay());
        Date weekAgo = TimeUtil.minusDayFromDate(currentDay, 7);

        getView().setEndDate(TimeUtil.timeToString(TimeUtil.getCurrentDay()));

        getView().setStartDate(TimeUtil.timeToString(weekAgo.getTime()));

//        getDiagram(activityDataType);

   }

    public void getDiagram(Pair<Constants.DATA_TYPES, String> _dataType){
        Date start = TimeUtil.parseDate(getView().getStartDate());
        Date end = TimeUtil.parseDate(getView().getEndDate());
        switch (_dataType.first){
            case ACTIVITY_TIME:
                ActivityDataProvider.getInstance().getActualTime(start,end).subscribe((floats) -> {
                    setDiagramData(floats, App.getAppContext().getString(R.string.c_time));
                });
                break;
            case STEP:
                ActivityDataProvider.getInstance().getSteps(start,end).subscribe((floats) -> {
                    setDiagramData(floats, App.getAppContext().getString(R.string.c_step));
                });
                break;
            case DISTANCE:
                ActivityDataProvider.getInstance().getDistance(start,end).subscribe((floats) -> {
                    setDiagramData(floats, App.getAppContext().getString(R.string.c_meters));
                });
                break;
            case CALORIES:
                ActivityDataProvider.getInstance().getCalories(start,end).subscribe((floats) -> {
                    setDiagramData(floats, App.getAppContext().getString(R.string.c_calories));
                });
                break;
        }
    }

    public void setDiagramData(List<android.util.Pair<Long, Float>> floats, String _units){
        ArrayList<BarEntry> _entry = new ArrayList<>();
        BarData mDiagram = new BarData();
        String[] dates = new String[floats.size()];
        for (int i = 0; i < floats.size(); i++){
          dates[i] = TimeUtil.timeToStringDDMM(floats.get(i).first);
            _entry.add(new BarEntry(floats.get(i).second, i));
        }
        CombinedData data = new CombinedData(dates);
        BarDataSet set = new BarDataSet(_entry, _units);
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        mDiagram.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        getView().setDiagram(mDiagram, data);
    }

    public void onStartDateClicked() {
        getView().openStartDateCalendar(_date -> {
            getView().setStartDate(TimeUtil.dateToString(_date));
            getDiagram(getView().getDataTypes());
        });
    }

    public void onEndDateClicked() {
        getView().openEndDateCalendar(_date -> {
            getView().setEndDate(TimeUtil.dateToString(_date));
            getDiagram(getView().getDataTypes());
        });
    }


    public void onDataTypeChoosed(Pair<Constants.DATA_TYPES, String> _dataType) {
        // TODO: change diagram
        getDiagram(_dataType);
    }

    public interface AnalyticsView extends BaseFragmentView<AnalyticsPresenter> {

        void setDiagram(BarData _data, CombinedData data);
        void openStartDateCalendar(CalendarView.Callback _callBack);
        void openEndDateCalendar(CalendarView.Callback _callBack);
        void setStartDate(String _text);
        void setEndDate(String _text);
        void setDataTypes(List<Pair<Constants.DATA_TYPES, String>> _data);
        String getStartDate();
        String getEndDate();
        Pair<Constants.DATA_TYPES, String> getDataTypes();
    }

}

