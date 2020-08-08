package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;


public class RowAdapter extends ListAdapter<Grade, CellViewHolder> {

    private ITableActivityListener listener;

    public RowAdapter(ITableActivityListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public static final DiffUtil.ItemCallback<Grade> DIFF_CALLBACK = new DiffUtil.ItemCallback<Grade>() {
        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getAmount() == newItem.getAmount() && oldItem.isPresent() == newItem.isPresent();
        }
    };

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cell, parent, false);
        return new CellViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        Grade grade = getItem(position);
        holder.setEntity(grade);
    }

}
