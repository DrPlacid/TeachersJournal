package com.doctorplacid.holder;

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.room.students.StudentWithGrades;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RowViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView sumTextView;
    private RecyclerView recyclerView;

    private ITableListener listener;
    private RowAdapter adapter;
    private Student student;

    public RowViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        listener = (ITableListener) context;
        nameTextView = itemView.findViewById(R.id.nameTextView);
        sumTextView = itemView.findViewById(R.id.sumTextView);
        recyclerView = itemView.findViewById(R.id.RecyclerViewRow);

        nameTextView.setOnLongClickListener(view -> {
            listener.openDeleteStudentDialog((this.student));
            return false;
        });
    }

    public void setData(StudentWithGrades studentWithGrades) {
        this.student = studentWithGrades.getStudent();
        List<Grade> grades = studentWithGrades.getGrades();
        String nameText = student.getName();
        nameTextView.setText(nameText);
        adapter = new RowAdapter(listener);
        recyclerView.setAdapter(adapter);
        submitList(grades);
    }

    public void submitList(List<Grade> grades){
        int sum = 0;
        for (Grade grade : grades)
            sum += grade.getAmount();

        sumTextView.setText(String.valueOf(sum));
        adapter.submitList(grades);
    }

    public RecyclerView getRecycler() {
        return recyclerView;
    }

}
