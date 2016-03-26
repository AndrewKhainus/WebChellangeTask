package com.task.webchallengetask.ui.modules.analytics;

import android.support.v4.util.Pair;

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

    private ProgramDataProvider dataProvider = ProgramDataProvider.getInstance();
    public static final Pair<Constants.DATA_TYPES, String> activityDataType = new Pair(Constants.DATA_TYPES.ACTIVITY_TIME, "Activity time");
    public static final Pair<Constants.DATA_TYPES, String> stepDataType = new Pair(Constants.DATA_TYPES.STEP, "Steps");
    public static final Pair<Constants.DATA_TYPES, String> distanceDataType = new Pair(Constants.DATA_TYPES.DISTANCE, "Distance");
    public static final Pair<Constants.DATA_TYPES, String> caloriesDataType = new Pair(Constants.DATA_TYPES.CALORIES, "Calories");
    public static final Pair<Constants.DATA_TYPES, String> allDataType = new Pair(Constants.DATA_TYPES.All, "All");

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        List<Pair<Constants.DATA_TYPES, String>> dataTypesList = new ArrayList<>();
        dataTypesList.add(activityDataType);
        dataTypesList.add(stepDataType);
        dataTypesList.add(distanceDataType);
        dataTypesList.add(caloriesDataType);
        dataTypesList.add(allDataType);
        getView().setDataTypes(dataTypesList);

        getView().setEndDate(TimeUtil.timeToString(TimeUtil.getCurrentDay()));
        Date weekAgo = TimeUtil.minusDayFromDate(new Date(), 7);
        getView().setStartDate(TimeUtil.timeToString(weekAgo.getTime()));

    }

    public void onStartDateClicked() {
        getView().openStartDateCalendar(_date -> getView().setStartDate(TimeUtil.dateToString(_date)));
    }

    public void onEndDateClicked() {
        getView().openEndDateCalendar(_date -> getView().setEndDate(TimeUtil.dateToString(_date)));
    }


    public void onDataTypeChoosed(Pair<Constants.DATA_TYPES, String> _dataType) {
        // TODO: change diagram
    }

    public interface AnalyticsView extends BaseFragmentView<AnalyticsPresenter> {

        void setDiagram();
        void openStartDateCalendar(CalendarView.Callback _callBack);
        void openEndDateCalendar(CalendarView.Callback _callBack);
        void setStartDate(String _text);
        void setEndDate(String _text);
        void setDataTypes(List<Pair<Constants.DATA_TYPES, String>> _data);
    }

}
