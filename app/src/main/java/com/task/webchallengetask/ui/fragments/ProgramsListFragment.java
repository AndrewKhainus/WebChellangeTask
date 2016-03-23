package com.task.webchallengetask.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.ui.base.BaseFragment;
import com.task.webchallengetask.ui.dialogs.AddProgramDialog;
import com.task.webchallengetask.ui.fragments.presenters.ProgramsListPresenter;

/**
 * Created by klim on 23.03.16.
 */
public class ProgramsListFragment extends BaseFragment<ProgramsListPresenter>
        implements ProgramsListPresenter.ProgramListView {

    private FloatingActionButton fabAddProgram;

    public static ProgramsListFragment newInstance() {

        Bundle args = new Bundle();

        ProgramsListFragment fragment = new ProgramsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setTitle() {
        return R.string.programs;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_programs_layout;
    }

    @Override
    public ProgramsListPresenter initPresenter() {
        return new ProgramsListPresenter();
    }

    @Override
    public void findUI(View rootView) {
        fabAddProgram = (FloatingActionButton) rootView.findViewById(R.id.fab);
    }

    @Override
    public void setupUI() {
        RxUtils.click(fabAddProgram, view -> getPresenter().onAddProgramClicked());
    }

    @Override
    public void showAddProgramDialog() {
        AddProgramDialog dialog = new AddProgramDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "");
    }
}
