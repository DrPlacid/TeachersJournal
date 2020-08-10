package com.doctorplacid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.doctorplacid.ITableListener;
import com.doctorplacid.R;

public class DialogDeleteGroup extends AppCompatDialogFragment {

    ITableListener listener;

    private String name;

    public DialogDeleteGroup(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_delete_group, null);

        builder.setView(view)
                .setTitle("Delete " + name + "?")
                .setNegativeButton("Cancel", (dialog, which) ->
                        DialogDeleteGroup.super.onDestroy())
                .setPositiveButton("Delete", (dialog, which) ->
                        listener.deleteGroup());
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ITableListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement IMainActivityListener");
        }
    }
}
