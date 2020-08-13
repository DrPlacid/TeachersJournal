package com.doctorplacid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.ColumnHeaderViewHolder;
import com.doctorplacid.room.lessons.Lesson;


public class ColumnHeadersAdapter extends ListAdapter<Lesson, ColumnHeaderViewHolder> {

    private ITableListener listener;

    public ColumnHeadersAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.listener = (ITableListener) context;
    }

    public static final DiffUtil.ItemCallback<Lesson> DIFF_CALLBACK = new DiffUtil.ItemCallback<Lesson>() {
        @Override
        public boolean areItemsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
            return oldItem.getDate().equals(newItem.getDate());
        }
    };


    @NonNull
    @Override
    public ColumnHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_column_header, parent, false);
        return new ColumnHeaderViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnHeaderViewHolder holder, int position) {
        Lesson lesson = getItem(position);
        holder.setEntity(lesson);
    }


    public Lesson getItemAt(int position) {
        return getItem(position);
    }
}
