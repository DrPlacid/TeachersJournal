package com.doctorplacid.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.TeachersViewModel;
import com.doctorplacid.holder.RowGradesHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.Student;

import java.util.ArrayList;
import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<RowGradesHolder> {

    private static final String TAG = "GRADES_ADAPTER";

    private List<Grade> studentList = new ArrayList<>();

    @NonNull
    @Override
    public RowGradesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_temp_row,parent, false);
        return new RowGradesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RowGradesHolder holder, int position) {
        Grade grade = studentList.get(position);
        holder.setList(grade);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setItems(List<Grade> studentList) {
        this.studentList = studentList;
        notifyDataSetChanged();
    }
}
