package com.doctorplacid.holder;

import com.doctorplacid.R;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.Student;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RowGradesHolder extends RecyclerView.ViewHolder {

    private TextView text;

    public RowGradesHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.tempText);
    }

    public void setList(Grade grade) {
        String s = String.valueOf(grade.getAmount());
        text.setText(s);
    }

}
