package com.doctorplacid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.holder.RowGradesHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.StudentWithGrades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GradesAdapter extends RecyclerView.Adapter<RowGradesHolder> {

    private Set<RowGradesHolder> holderSet = new HashSet<>();
    private int dx;
    private int dy;
    private Context context;
    private List<StudentWithGrades> studentsWithGrades = new ArrayList<>();

    public GradesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RowGradesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row,parent, false);
        RowGradesHolder holder = new RowGradesHolder(itemView, context);
        holderSet.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RowGradesHolder holder, int position) {
        List <Grade> grades = studentsWithGrades.get(position).getGrades();
        holder.setList(grades);
        holder.scroll(dx, dy);
    }

    @Override
    public int getItemCount() {
        return studentsWithGrades.size();
    }

    public void setItems(List<StudentWithGrades> studentsWithGrades) {
        this.studentsWithGrades = studentsWithGrades;
        notifyDataSetChanged();
    }

    public void scrollAllItems(int dx, int dy) {
        for (RowGradesHolder holder : holderSet) {
            holder.scroll(dx, dy);
        }
    }
}
