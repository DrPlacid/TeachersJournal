package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.holder.CellHolder;
import com.doctorplacid.room.grades.Grade;

import java.util.ArrayList;
import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<CellHolder> {

    private List<Grade> grades = new ArrayList<>();

    @NonNull
    @Override
    public CellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cell, parent, false);
        return new CellHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CellHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.setText(grade);
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    public void setItems(List<Grade> grades) {
        this.grades = grades;
        notifyDataSetChanged();
    }
}
