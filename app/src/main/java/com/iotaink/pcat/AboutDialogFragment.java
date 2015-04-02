package com.iotaink.pcat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * A DialogFragment to display information about the app
 */
public class AboutDialogFragment extends DialogFragment {

    /**
     * onCreateDialog
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        // Get the inflater
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        // Build the dialog
        builder.setTitle(R.string.about_dialog_title)
                .setView(inflater.inflate(R.layout.dialog_about, null))
                .setPositiveButton(R.string.about_confirmation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AboutDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
