package com.bignerdranch.android.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.R;

/**
 * @author pec
 * @since 01/12/2016
 */

public class ConfirmationDialogFragment extends DialogFragment {
    private static final String DIALOG_TITLE_ARG = "dialog_title";
    private static final String DIALOG_TEXT_ARG = "dialog_text";

    public static ConfirmationDialogFragment newInstance(int titleResId, int textResId) {
        ConfirmationDialogFragment confirmationDialog = new ConfirmationDialogFragment();
        confirmationDialog.setArguments(new ArgumentsBuilder()
                                                .addDialogTitle(titleResId)
                                                .addDialogText(textResId)
                                                .build());
        return confirmationDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args != null && args.containsKey(DIALOG_TITLE_ARG) && args.containsKey(DIALOG_TEXT_ARG)) {
            final Fragment targetFragment = getTargetFragment();
            if(targetFragment != null) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int requestCode = getTargetRequestCode();
                        if(which == DialogInterface.BUTTON_POSITIVE) {
                            targetFragment.onActivityResult(requestCode, Activity.RESULT_OK, null);
                        } else {
                            targetFragment.onActivityResult(requestCode, Activity.RESULT_CANCELED, null);
                        }
                    }
                };

                return new AlertDialog.Builder(getActivity())
                        .setTitle(args.getInt(DIALOG_TITLE_ARG))
                        .setMessage(args.getInt(DIALOG_TEXT_ARG))
                        .setPositiveButton(android.R.string.yes, listener)
                        .setNegativeButton(android.R.string.no, listener)
                        .create();
            } else {
                throw new IllegalArgumentException(getString(R.string.no_target_fragment));
            }
        } else {
            throw new IllegalArgumentException(getString(R.string.no_enough_params_dialog_creation));
        }
    }



    static class ArgumentsBuilder {
        private Bundle args;

        ArgumentsBuilder() {
            this.args = new Bundle();
        }

        ArgumentsBuilder addDialogTitle(int dialogTitleResId) {
            args.putInt(DIALOG_TITLE_ARG, dialogTitleResId);
            return this;
        }

        ArgumentsBuilder addDialogText(int dialogTextResId) {
            args.putInt(DIALOG_TEXT_ARG, dialogTextResId);
            return this;
        }

        Bundle build() {
            return args;
        }
    }
}
