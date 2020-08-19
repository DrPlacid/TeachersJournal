package com.doctorplacid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;


public class RowAdapter extends ListAdapter<Grade, CellViewHolder> {

    private Context context;

    public RowAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public static final DiffUtil.ItemCallback<Grade> DIFF_CALLBACK = new DiffUtil.ItemCallback<Grade>() {
        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public CellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cell, parent, false);
        return new CellViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CellViewHolder holder, int position) {
        Grade grade = getItem(position);
        holder.setData(grade);
    }

}
