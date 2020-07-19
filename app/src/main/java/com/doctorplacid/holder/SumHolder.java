package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.room.students.Student;

public class SumHolder extends RecyclerView.ViewHolder {

    TextView sumTextView;

    public SumHolder(@NonNull View itemView) {
        super(itemView);
        sumTextView = itemView.findViewById(R.id.sumTextView);
    }

    public void SetText(Student student) {
        String sum = String.valueOf(student.getSum());
        sumTextView.setText(sum);
    }
}
