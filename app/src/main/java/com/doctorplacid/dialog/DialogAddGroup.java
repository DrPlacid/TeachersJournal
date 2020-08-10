package com.doctorplacid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.doctorplacid.ITableListener;
import com.doctorplacid.R;

public class DialogAddGroup extends AppCompatDialogFragment {

    ITableListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.dialog_add, null);

        EditText nameEditText = view.findViewById(R.id.editTextGroupName);

        builder.setView(view)
                .setTitle("Add new")
                .setNegativeButton("Cancel", (dialog, which) ->
                        DialogAddGroup.super.onDestroy())
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString().toUpperCase().trim();
                    if (name.length() > 0) {
                        listener.addGroup(name);
                    }
                });

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
