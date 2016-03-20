package com.task.webchallengetask.ui.dialogs;


import com.task.webchallengetask.R;

public class ErrorDialog extends InfoDialog {

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_error_layout;
    }

    @Override
    protected void setupUI() {
        super.setupUI();
        if (tvTitle.getText().toString().isEmpty()) {
            tvTitle.setText(getString(R.string.error));
        }
    }
}
