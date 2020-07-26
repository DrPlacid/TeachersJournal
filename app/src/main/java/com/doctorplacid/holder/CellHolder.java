package com.doctorplacid.holder;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.activity.TableActivity;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.R;

public class CellHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private EditText editText;

    private Grade grade;

    public CellHolder(@NonNull View itemView, Context context) {
        super(itemView);
        textView = itemView.findViewById(R.id.gradeTextView);
        editText = itemView.findViewById(R.id.gradeEditText);
        ITableActivityListener listener = (ITableActivityListener) context;

        textView.setOnClickListener(view -> listener.onGradeEdited(CellHolder.this));
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
        setText();
    }

    public Grade getGrade() {
        return grade;
    }

    public void setText() {
        textView.setText(String.valueOf(grade.getAmount()));
    }

    public EditText showEditText() {
        textView.setVisibility(View.INVISIBLE);
        String text = String.valueOf(grade.getAmount());
        editText.setText(text);
        editText.setSelection(text.length());
        editText.setVisibility(View.VISIBLE);
        return editText;
    }

    public Grade showTextView() {
        String text = editText.getText().toString().trim();
        int amount = Integer.parseInt(text);
        editText.setVisibility(View.INVISIBLE);
        grade.setAmount(amount);
        textView.setVisibility(View.VISIBLE);
        setText();
        return grade;
    }

}
