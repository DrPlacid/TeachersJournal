package com.doctorplacid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.doctorplacid.R;
import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.activity.MainActivity;
import com.doctorplacid.room.lessons.Lesson;

public class DialogDeleteColumn extends AppCompatDialogFragment {

    ITableListener listener;

    private Lesson lesson;
    private String dd;
    private String mm;

    public DialogDeleteColumn(Lesson lesson) {
        this.lesson = lesson;
        this.dd = lesson.getDay();
        this.mm = lesson.getMonth();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.fragment_dialog_delete, null);

        TextView deletedText = view.findViewById(R.id.textViewDelete);
        String infoText = (("").equals(dd) && ("").equals(mm)) ? "" : dd + "/" + mm;
        deletedText.setText(infoText);
        deletedText.setLetterSpacing(MainActivity.LETTER_SPACING_NARROW);

        String title = getString(R.string.dialog_title_delete_column);
        String cancel = getString(R.string.dialog_cancel);
        String delete = getString(R.string.dialog_delete);

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(cancel, (dialog, which) ->
                        DialogDeleteColumn.super.onDestroy())
                .setPositiveButton(delete, (dialog, which) ->
                        listener.deleteColumn(lesson));
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
