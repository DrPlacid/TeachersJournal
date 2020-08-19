package com.doctorplacid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.doctorplacid.R;
import com.doctorplacid.activity.ITableListener;

public class DialogChangeLanguage extends AppCompatDialogFragment {

    ITableListener listener;

    RadioButton en;
    RadioButton ua;
    RadioButton ru;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.fragment_dialog_language, null);

        en = view.findViewById(R.id.radioButtonEn);
        ua = view.findViewById(R.id.radioButtonUa);
        ru = view.findViewById(R.id.radioButtonRu);


        String title = getString(R.string.dialog_title_change_language);
        String cancel = getString(R.string.dialog_cancel);
        String set = getString(R.string.dialog_set);

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(cancel, (dialog, which) ->
                        DialogChangeLanguage.super.onDestroy())
                .setPositiveButton(set, (dialog, which) ->
                        listener.onChangeLanguage(checkLanguage()));
        return builder.create();
    }

    private String checkLanguage() {
        if (en.isChecked()) {
            return "en";
        }
        if (ua.isChecked()) {
            return "uk";
        }
        if (ru.isChecked()) {
            return "ru";
        } else {
            return "";
        }
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
