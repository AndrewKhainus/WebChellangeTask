package com.task.webchallengetask.ui.base;

import com.task.webchallengetask.ui.dialogs.presenters.BaseDialogPresenter;

/**
 * Created by klim on 23.03.16.
 */
public interface BaseDialogView<P extends BaseDialogPresenter> extends BaseView<P> {

    void dismissDialog();
    void hideKeyboard();
}
