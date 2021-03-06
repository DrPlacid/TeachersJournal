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

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;

import java.util.Objects;

public class DialogAddGroup extends AppCompatDialogFragment {

    private ITableListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add, null);

        EditText nameEditText = view.findViewById(R.id.editTextGroupName);

        String title = getString(R.string.dialog_title_create_group);
        String cancel = getString(R.string.dialog_cancel);
        String add = getString(R.string.dialog_add);

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(cancel, (dialog, which) ->
                        DialogAddGroup.super.onDestroy())
                .setPositiveButton(add, (dialog, which) -> {
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
            throw new ClassCastException(context.toString() + "Must implement ITableListener");
        }
    }
}
