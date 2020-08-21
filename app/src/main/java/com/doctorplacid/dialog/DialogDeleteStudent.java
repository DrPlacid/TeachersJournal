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

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;
import com.doctorplacid.activity.MainActivity;
import com.doctorplacid.room.students.Student;

public class DialogDeleteStudent extends AppCompatDialogFragment {

    private ITableListener listener;

    private Student student;
    private String name;

    public DialogDeleteStudent(Student student) {
        this.student = student;
        this.name = student.getName();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.fragment_dialog_delete, null);

        TextView deletedText = view.findViewById(R.id.textViewDelete);
        deletedText.setText(name);
        deletedText.setLetterSpacing(MainActivity.LETTER_SPACING_NARROW);

        String title = getString(R.string.dialog_title_delete_student);
        String cancel = getString(R.string.dialog_cancel);
        String delete = getString(R.string.dialog_delete);

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(cancel, (dialog, which) ->
                        DialogDeleteStudent.super.onDestroy())
                .setPositiveButton(delete, (dialog, which) ->
                        listener.deleteStudent(student));
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
