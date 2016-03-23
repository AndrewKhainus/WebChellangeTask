package com.task.webchallengetask.ui.dialogs.presenters;

import com.task.webchallengetask.ui.base.BaseDialogView;

/**
 * Created by klim on 23.03.16.
 */
public class ConfirmDialogPresenter extends BaseDialogPresenter<ConfirmDialogPresenter.ConfirmDialogView>{

    public void onPositiviClicked() {
        getView().dismissDialog();
    }

    public void onNegativeClicked() {
        getView().dismissDialog();
    }

    public interface ConfirmDialogView extends BaseDialogView<ConfirmDialogPresenter> {

    }
}
