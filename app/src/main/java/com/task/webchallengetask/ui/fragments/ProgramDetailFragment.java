package com.task.webchallengetask.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.programs.difficults.Difficult;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.ui.adapters.DifficultAdapter;
import com.task.webchallengetask.ui.base.BaseFragment;
import com.task.webchallengetask.ui.fragments.presenters.ProgramDetailPresenter;

import java.util.List;

/**
 * Created by klim on 24.03.16.
 */
public class ProgramDetailFragment extends BaseFragment<ProgramDetailPresenter> implements
        ProgramDetailPresenter.ProgramDetailView {

    private TextView tvTitle;
    private FrameLayout lDiagramContainer;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private EditText etTarget;
    private Spinner spDifficult;
    private TextView tvActualResult;
    private Button btnAnalyze;


    public static ProgramDetailFragment newInstance(String _programName) {

        Bundle args = new Bundle();
        args.putString(Constants.PROGRAM_NAME_KEY, _programName);
        ProgramDetailFragment fragment = new ProgramDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setTitle() {
        return R.string.program_detail;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_program_detail;
    }

    @Override
    public ProgramDetailPresenter initPresenter() {
        return new ProgramDetailPresenter();
    }

    @Override
    public void findUI(View rootView) {
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle_FP);
        lDiagramContainer  = (FrameLayout) rootView.findViewById(R.id.diagram_container);
        tvStartDate  = (TextView) rootView.findViewById(R.id.tvStartDate_FP);
        tvEndDate  = (TextView) rootView.findViewById(R.id.tvEndDate_FP);
        etTarget  = (EditText) rootView.findViewById(R.id.etTarget_FP);
        spDifficult = (Spinner) rootView.findViewById(R.id.spDifficult_FP);
        tvActualResult = (TextView) rootView.findViewById(R.id.tvActualResult_FP);
        btnAnalyze = (Button) rootView.findViewById(R.id.btnAnalyze_FP);

    }

    @Override
    public void setupUI() {
        spDifficult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().onDifficultChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etTarget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etTarget.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxUtils.click(btnAnalyze, o -> getPresenter().onAnalyze());
    }

    @Override
    public void setTitle(String _text) {
        tvTitle.setText(_text);
    }

    @Override
    public void setDiagram() {

    }

    @Override
    public void setDifficult(List<Difficult> _data) {
        DifficultAdapter adapter = new DifficultAdapter(this.getContext(), android.R.layout.simple_spinner_item, _data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDifficult.setAdapter(adapter);

    }

    @Override
    public void setTarget(String _text) {
        etTarget.setText(_text);
    }

    @Override
    public void setActualResults(String _text) {
        tvActualResult.setText(_text);
    }

    @Override
    public void setTargetEnabled(boolean _isEnabled) {
        etTarget.setEnabled(_isEnabled);
    }

    @Override
    public void setDifficultEnabled(boolean _isEnabled) {
        spDifficult.setEnabled(_isEnabled);
    }
}
