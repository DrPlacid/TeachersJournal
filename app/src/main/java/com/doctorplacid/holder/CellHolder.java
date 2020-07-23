package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.R;

public class CellHolder extends RecyclerView.ViewHolder {

    private TextView textView;

    public CellHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.gradeTextView);
    }

    public void setText(Grade grade) {
        String s = String.valueOf(grade.getAmount());
        textView.setText(s);
    }
}
