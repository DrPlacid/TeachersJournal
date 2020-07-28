package com.doctorplacid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.activity.TableActivity;
import com.doctorplacid.holder.RowHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.StudentWithGrades;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableAdapter extends ListAdapter<StudentWithGrades, RowHolder> {

    public static Set<RowHolder> holderSet = new HashSet<>();
    private Context context;


    public TableAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<StudentWithGrades> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StudentWithGrades>() {
                @Override
                public boolean areItemsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
                    return oldItem.getStudent().getStudentId() == newItem.getStudent().getStudentId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
                    return (oldItem.getStudent().getName().equals(newItem.getStudent().getName()) &&
                            oldItem.getGrades().equals(newItem.getGrades()));
                }
            };


    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row,parent, false);
        RowHolder holder = new RowHolder(itemView, context, this);
        holderSet.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        StudentWithGrades studentWithGrades = getItem(position);
        holder.setList(studentWithGrades);
    }

    public void scrollAllItems(int dx, int dy, RowHolder scrolledHolder) {
        holderSet.remove(scrolledHolder);
        for (RowHolder holder : holderSet)
            holder.scroll(dx, dy);

        holderSet.add(scrolledHolder);

        ((TableActivity) context).scroll(dx, dy);
    }

    public void scrollAllItems(int dx, int dy) {
        for (RowHolder holder : holderSet)
            holder.scroll(dx, dy);
    }



    public StudentWithGrades getItemAt(int position) {
        return getItem(position);
    }
}
