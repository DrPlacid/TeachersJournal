package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.CellHolder;
import com.doctorplacid.room.grades.Grade;


public class RowAdapter extends ListAdapter<Grade, CellHolder> {

    private ITableActivityListener listener;

    public RowAdapter(ITableActivityListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public static final DiffUtil.ItemCallback<Grade> DIFF_CALLBACK = new DiffUtil.ItemCallback<Grade>() {
        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getGradeId() == newItem.getGradeId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getAmount() == newItem.getAmount() && oldItem.isPresent() == newItem.isPresent();
        }
    };

    @NonNull
    @Override
    public CellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cell, parent, false);
        return new CellHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CellHolder holder, int position) {
        Grade grade = getItem(position);
        holder.setGrade(grade);
    }

}
