package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.kotula.nikolai.trainingpeakscodetest.R;

public class ErrorDialog extends DialogFragment {
    public static final String ERROR_DIALOG_TAG = "com.kotula.nikolai.trainingpeakscodetest.fragments.ErrorDialog";
    public static final String ERROR_MESSAGE = "com.kotula.nikolai.trainingpeakscodetest.fragments.ErrorDialog.ERROR_MESSAGE";

    public static ErrorDialog newInstance(String errorMsg) {
        ErrorDialog dialog = new ErrorDialog();

        Bundle args = new Bundle();
        args.putString(ERROR_MESSAGE, (errorMsg == null) ? "" : errorMsg);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String errorMessage = "";
        if (getArguments() != null) {
            String msg = getArguments().getString(ERROR_MESSAGE);
            if (msg != null)
                errorMessage += msg;
        }
        return new AlertDialog.Builder(getActivity())
                .setMessage(errorMessage)
                .setNegativeButton(R.string.lbl_go_back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent();
                            intent.putExtra(PeakFragment.INTENT, PeakFragment.INTENT_FINISH);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .setPositiveButton(R.string.lbl_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() != null) {
                            Intent intent = new Intent();
                            intent.putExtra(PeakFragment.INTENT, PeakFragment.INTENT_REFRESH);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                    }
                })
                .create();
    }
}
