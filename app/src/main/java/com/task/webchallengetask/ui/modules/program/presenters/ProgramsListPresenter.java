package com.task.webchallengetask.ui.modules.program.presenters;

import com.task.webchallengetask.data.data_providers.ProgramDataProvider;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.ui.base.BaseFragmentPresenter;
import com.task.webchallengetask.ui.base.BaseFragmentView;
import com.task.webchallengetask.ui.dialogs.DialogListener;
import com.task.webchallengetask.ui.modules.program.ProgramDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klim on 23.03.16.
 */
public class ProgramsListPresenter extends BaseFragmentPresenter<ProgramsListPresenter.ProgramListView> {

    private List<ProgramTable> programs = new ArrayList<>();

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getPrograms();
    }

    public void getPrograms() {
        ProgramDataProvider.getInstance().getPrograms()
                .subscribe(this::showPrograms, Logger::e);
    }

    public void showPrograms(List<ProgramTable> _data) {
        programs.clear();
        programs.addAll(_data);
        getView().showPrograms(_data);
    }

    public void onAddProgramClicked() {
        getView().showAddProgramDialog(this::getPrograms);
    }

    public void onProgramClicked(int position) {
        getView().switchFragment(ProgramDetailFragment.newInstance(programs.get(position).getName()), true);
    }

    public interface ProgramListView extends BaseFragmentView<ProgramsListPresenter> {
        void showAddProgramDialog(DialogListener _listener);
        void showPrograms(List<ProgramTable> _data);
    }
}