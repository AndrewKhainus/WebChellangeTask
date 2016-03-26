package com.task.webchallengetask.ui.modules.program.presenters;

import android.util.Pair;

import com.task.webchallengetask.data.data_providers.ActivityDataProvider;
import com.task.webchallengetask.data.data_providers.PredictionDataProvider;
import com.task.webchallengetask.data.data_providers.ProgramDataProvider;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.programs.Program;
import com.task.webchallengetask.global.programs.ProgramFactory;
import com.task.webchallengetask.global.programs.difficults.Difficult;
import com.task.webchallengetask.global.programs.difficults.DifficultCustom;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;
import com.task.webchallengetask.ui.custom.CalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by klim on 24.03.16.
 */
public class ProgramDetailPresenter extends BaseFragmentPresenter<ProgramDetailPresenter.ProgramDetailView> {

    private ProgramDataProvider mProgramDataProvider = ProgramDataProvider.getInstance();
    private PredictionDataProvider mPredictionDataProvider = PredictionDataProvider.getInstance();
    private ActivityDataProvider mActivityDataProvider = ActivityDataProvider.getInstance();
    private ProgramTable mProgramTable;
    private Program mProgram;
    private List<Pair<Long, Float>> mDataList = new ArrayList<>();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setDifficultEnabled(false);

        getView().setEndDate(TimeUtil.timeToString(TimeUtil.getCurrentDay()));
        Date weekAgo = TimeUtil.minusDayFromDate(new Date(), 7);
        getView().setStartDate(TimeUtil.timeToString(weekAgo.getTime()));

        mProgramDataProvider.getProgram(getView().getFragmentArguments().getInt(Constants.PROGRAM_ID_KEY))
                .subscribe(programTable -> {
                    mProgramTable = programTable;
                    mProgram = defineProgram(mProgramTable);
                    fillProgram(mProgramTable);
                }, Logger::e);

    }

    private Program defineProgram(ProgramTable _table) {
        for (Program program : ProgramFactory.getPrograms()) {
            if (program.getName().equals(_table.getName())) {
                return program;
            }
        }
        return null;
    }

    public void onSaveClicked() {
        mProgramTable.target = Integer.valueOf(getView().getTarget());
        mProgramTable.difficult = getView().getDifficult().getName();
        mProgramTable.update();
        getView().setSaveVisible(false);
        getView().setDifficultEnabled(false);
        getView().setTargetEnabled(false);
    }


    public void onStartDateClicked() {
        getView().openStartDateCalendar(_date -> getView().setStartDate(TimeUtil.dateToString(_date)));
    }

    public void onEndDateClicked() {
        getView().openEndDateCalendar(_date -> getView().setEndDate(TimeUtil.dateToString(_date)));
    }

    private void fillProgram(ProgramTable _programTable) {
        getView().setTitle(_programTable.getName());
        getView().setDifficultList(getDifficultList(_programTable.getName()));
        getView().setDifficult(getDifficultPosition(_programTable.getName(), _programTable.getDifficult()));
        getView().setTarget(String.valueOf(_programTable.getTarget()));
        getView().setUnit(_programTable.getUnit());
        if (getDifficult(_programTable.getName(), _programTable.getDifficult()) instanceof DifficultCustom) {
            getView().setTargetEnabled(true);
        } else {
            getView().setTargetEnabled(false);
        }


        final Date startDate = TimeUtil.parseDate(getView().getStartDate());
        final Date endDate = TimeUtil.parseDate(getView().getEndDate());

        switch (mProgram.getType()) {
            case ACTIVE_LIFE:
                mActivityDataProvider.getActualTime(startDate, endDate)
                        .subscribe(floats -> {
                            mDataList.clear();
                            mDataList.addAll(floats);
                            if (!floats.isEmpty())
                                getView().setActualResults(String.valueOf(floats.get(floats.size() - 1).second));
                            fillDiagram();
                        });
                break;
            case LONG_DISTANCE:
                mActivityDataProvider.getDistance(startDate, endDate)
                        .subscribe(floats -> {
                            mDataList.clear();
                            mDataList.addAll(floats);
                            if (!floats.isEmpty())
                                getView().setActualResults(String.valueOf(floats.get(floats.size() - 1).second));

                            fillDiagram();
                        });
                break;
        }
    }

    private void fillDiagram() {
        getView().setDiagram();
    }

    private List<Difficult> getDifficultList(String _name) {
        List<Program> programs = ProgramFactory.getPrograms();
        for (Program program : programs) {
            if (program.getName().equals(_name)) {
                return program.getDifficult();
            }
        }
        return new ArrayList<>();
    }

    private Difficult getDifficult(String _programName, String _difficultName) {
        for (Difficult diff : getDifficultList(_programName)) {
            if (diff.getName().equals(_difficultName)) {
                return diff;
            }
        }
        return null;
    }

    private int getDifficultPosition(String _programName, String _difficultName) {
        List<Difficult> list = getDifficultList(_programName);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(_difficultName)) {
                return i;
            }
        }
        return 0;
    }


    public void onAnalyze() {
        int complited = 0;
        int uncomplited = 0;

        for (Pair<Long, Float> value : mDataList) {
            float v = value.second;
            if (v == 0) continue;
            if (v >= Float.valueOf(getView().getTarget())) {
                complited++;
            } else
                uncomplited++;

        }

        getView().showLoadingDialog();
        addSubscription(mPredictionDataProvider.analyzeWeeklyTrendResults(complited, uncomplited)
                .subscribe(s -> {
                            String message = "";
                            if (s.equals("Increase")) {
                                message = "You could increase your difficult";
                            }
                            if (s.equals("Stay")) {
                                message = "You should stay with current difficult";
                            }
                            if (s.equals("Reduce")) {
                                message = "You should make a current difficult easier";
                            }
                            getView().setDifficultEnabled(true);
                            getView().showInfoDialog("Recommendation", message, null);
                            getView().setSaveVisible(true);
                            getView().hideLoadingDialog();
                        }, throwable -> {
                            Logger.e(throwable);
                            getView().hideLoadingDialog();
                        }

                ));
    }

    public void onDifficultChanged(int _position) {
        List<Difficult> diffList = getDifficultList(mProgramTable.getName());
        if (diffList.get(_position) instanceof DifficultCustom) {
            getView().setTargetEnabled(true);
        } else {
            getView().setTarget(diffList.get(_position).getTarget());
        }
    }

    public interface ProgramDetailView extends BaseFragmentView<ProgramDetailPresenter> {
        void setSaveVisible(boolean _isVisible);

        void setTitle(String _text);

        void setDiagram();

        void setDifficultList(List<Difficult> _data);

        void setDifficult(int _position);

        void setTarget(String _text);

        void setUnit(String _text);

        void setActualResults(String _text);

        void setTargetEnabled(boolean _isEnabled);

        void setDifficultEnabled(boolean _isEnabled);

        void openStartDateCalendar(CalendarView.Callback _callBack);

        void openEndDateCalendar(CalendarView.Callback _callBack);

        void setStartDate(String _text);

        void setEndDate(String _text);

        String getStartDate();

        String getEndDate();

        String getTarget();

        Difficult getDifficult();
    }
}
