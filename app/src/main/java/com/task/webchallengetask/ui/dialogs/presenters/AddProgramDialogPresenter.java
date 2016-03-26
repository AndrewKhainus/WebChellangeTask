package com.task.webchallengetask.ui.dialogs.presenters;

import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.programs.Program;
import com.task.webchallengetask.global.programs.ProgramFactory;
import com.task.webchallengetask.global.programs.difficults.Difficult;
import com.task.webchallengetask.global.programs.difficults.DifficultCustom;
import com.task.webchallengetask.global.utils.TimeUtil;
import com.task.webchallengetask.ui.base.BaseDialogView;

import java.util.List;

/**
 * Created by klim on 23.03.16.
 */
public class AddProgramDialogPresenter extends BaseDialogPresenter<AddProgramDialogPresenter.AddProgramView> {

    private List<Program> programs;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        programs = ProgramFactory.getPrograms();
        getView().setPrograms(programs);
    }

    public void onProgramChose(int _position) {
        String description = "";
        switch (_position) {
            case 0:
                description = App.getAppContext().getString(R.string.distance_description);
                break;
            case 1:
                description = App.getAppContext().getString(R.string.activity_description);
                break;
        }
        getView().setDescription(description);
        getView().setDifficultEnabled(true);
        getView().setTarget(programs.get(_position).getDifficult().get(0).getTarget());
        getView().setUnit(programs.get(_position).getDifficult().get(0).getUnit());
        getView().setDifficult(programs.get(_position).getDifficult());
    }

    public void onDifficultChoosed(int _position) {
        if (programs.get(getView().getProgram()).getDifficult().get(_position) instanceof DifficultCustom) {
            getView().setTargetEditable(true);
        } else {
            getView().setTarget(programs.get(getView().getProgram()).getDifficult().get(_position).getTarget());
            getView().setTargetEditable(false);
        }

    }

    public void onSaveClicked() {
        getView().hideKeyboard();
        if (validate()) {
            Program program = programs.get(getView().getProgram());
            Difficult difficult = program.getDifficult().get(getView().getDifficult());

            ProgramTable programTable = new ProgramTable();
            programTable.name = program.getName();
            programTable.difficult = difficult.getName();
            programTable.target = Integer.valueOf(getView().getTarget());
//            programTable.actualResult = "0";
            programTable.unit = getView().getUnit();
//            programTable.date = TimeUtil.getCurrentDay();
            programTable.save();
            getView().dismissDialog();

        }
    }

    private boolean validate() {
        Difficult difficult = programs.get(getView().getProgram()).getDifficult().
                get(getView().getDifficult());

        if (difficult instanceof DifficultCustom) {
            try {
                boolean isValid = Integer.valueOf(getView().getTarget()) > 0;
                if (!isValid) getView().setTargetError("Enter some value");
                return isValid;
            } catch (NumberFormatException e) {
                getView().setTargetError("Enter some value");
                return false;
            }
        } else return true;
    }

    public interface AddProgramView extends BaseDialogView<AddProgramDialogPresenter> {
        void setPrograms(List<Program> data);
        int getProgram();
        void setDifficult(List<Difficult> data);
        int getDifficult();
        void setDifficultEnabled(boolean _isEnabled);
        void setUnit(String _text);
        String getUnit();
        void setDescription(String _text);
        void setTarget(String _text);
        void setTargetError(String _text);
        String getTarget();
        void setTargetEditable(boolean isEditable);
    }
}
