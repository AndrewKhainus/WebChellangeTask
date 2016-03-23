package com.task.webchallengetask.ui.fragments.presenters;

import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;
import com.task.webchallengetask.ui.base.BaseView;
import com.task.webchallengetask.ui.dialogs.AddProgramDialog;

/**
 * Created by klim on 23.03.16.
 */
public class ProgramsListPresenter extends BaseFragmentPresenter<ProgramsListPresenter.ProgramListView> {

    public void onAddProgramClicked() {
        getView().showAddProgramDialog();
    }

    public interface ProgramListView extends BaseFragmentView<ProgramsListPresenter> {
        void showAddProgramDialog();
    }
}
