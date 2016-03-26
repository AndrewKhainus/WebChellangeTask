package com.task.webchallengetask.ui.modules.program.presenters;

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
import com.task.webchallengetask.ui.custom.CalendarView;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by klim on 24.03.16.
 */
public class ProgramDetailPresenter extends BaseFragmentPresenter<ProgramDetailPresenter.ProgramDetailView> {

    private ProgramDataProvider mProgramDataProvider = ProgramDataProvider.getInstance();
    private PredictionDataProvider mPredictionDataProvider = PredictionDataProvider.getInstance();
    private ProgramTable todayProgram;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setDifficultEnabled(false);

        getView().setEndDate(TimeUtil.timeToString(TimeUtil.getCurrentDay()));
        Date weekAgo = TimeUtil.minusDayFromDate(new Date(), 7);
        getView().setStartDate(TimeUtil.timeToString(weekAgo.getTime()));



        mProgramDataProvider.getProgram(getView().getFragmentArguments().getString(Constants.PROGRAM_NAME_KEY))
                .subscribe(programTables -> {
                    createDiagram(programTables);
                    todayProgram = programTables.get(0);
                    fillProgram(todayProgram);
                }, Logger::e);

    }

    public void onStartDateClicked() {
        getView().openStartDateCalendar(_date -> getView().setStartDate(TimeUtil.dateToString(_date)));
    }

    public void onEndDateClicked() {
        getView().openEndDateCalendar(_date -> getView().setEndDate(TimeUtil.dateToString(_date)));
    }

    private void fillProgram(ProgramTable _programTable) {
        getView().setTitle(_programTable.getName());
        getView().setDifficult(getDifficultList(_programTable.getName()));
        getView().setTarget(_programTable.getTarget());
        getView().setActualResults(_programTable.getActualResult());
        if (getDifficult(_programTable.getName(), _programTable.getDifficult()) instanceof DifficultCustom) {
            getView().setTargetEnabled(true);
        } else {
            getView().setTargetEnabled(false);
        }
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

    public void onAnalyze() {
        getView().showLoadingDialog();
        mPredictionDataProvider.analyzeWeeklyTrendResults(7,0)
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
                }, Logger::e, () -> getView().hideLoadingDialog());
    }

    public void onDifficultChanged(int _position) {
        List<Difficult> diffList = getDifficultList(todayProgram.getName());
        if (diffList.get(_position) instanceof DifficultCustom) {
            getView().setTargetEnabled(true);
        } else {
            getView().setTarget(diffList.get(_position).getTarget());
        }
    }


    private void createDiagram(List<ProgramTable> programs) {
        getView().setDiagram();
    }

    public interface ProgramDetailView extends BaseFragmentView<ProgramDetailPresenter> {
        void setTitle(String _text);
        void setDiagram();
        void setDifficult(List<Difficult> _data);
        void setTarget(String _text);
        void setActualResults(String _text);
        void setTargetEnabled(boolean _isEnabled);
        void setDifficultEnabled(boolean _isEnabled);
        void openStartDateCalendar(CalendarView.Callback _callBack);
        void openEndDateCalendar(CalendarView.Callback _callBack);
        void setStartDate(String _text);
        void setEndDate(String _text);
    }
}
