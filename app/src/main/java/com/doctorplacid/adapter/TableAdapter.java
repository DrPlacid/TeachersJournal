package com.doctorplacid.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.R;
import com.doctorplacid.RecyclerUIManager;
import com.doctorplacid.holder.RowHeaderViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.StudentWithGrades;

import java.util.List;

public class TableAdapter extends ListAdapter<StudentWithGrades, RowHeaderViewHolder> {

    private Context context;
    private RecyclerUIManager recyclerUIManager;

    public TableAdapter(Context context, RecyclerUIManager recyclerUIManager) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.recyclerUIManager = recyclerUIManager;
    }


    private static final DiffUtil.ItemCallback<StudentWithGrades> DIFF_CALLBACK = new DiffUtil.ItemCallback<StudentWithGrades>() {

        @Override
        public boolean areItemsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            return oldItem.getStudent().getStudentId() == newItem.getStudent().getStudentId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            return oldItem.getGrades().equals(newItem.getGrades());
        }

        @Override
        public Object getChangePayload(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            if (oldItem.getGrades().size() != newItem.getGrades().size()) {
                return 2;
            }

            for(int i = 0; i < oldItem.getGrades().size(); i++) {
                int amountOld = oldItem.getGrades().get(i).getAmount();
                int amountNew = newItem.getGrades().get(i).getAmount();
                boolean presenceOld = oldItem.getGrades().get(i).isPresent();
                boolean presenceNew = newItem.getGrades().get(i).isPresent();

                if (amountOld != amountNew || presenceOld != presenceNew) {
                    return 1;
                }
            }
            return 0;
        }
    };

    @NonNull
    @Override
    public RowHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row,parent, false);
        RowHeaderViewHolder holder = new RowHeaderViewHolder(itemView, context);
        recyclerUIManager.addRow(holder.getRecycler());
        recyclerUIManager.syncRowPosition(holder.getRecycler());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RowHeaderViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            List<Grade> grades = getItem(position).getGrades();
            for (Object data : payloads) {
                switch ((int) data) {
                    case 2:
                        onBindViewHolder(holder, position);
                        break;
                    case 1:
                        holder.submitList(grades);
                        break;
                    case 0:
                        break;
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RowHeaderViewHolder holder, int position) {
        StudentWithGrades studentWithGrades = getItem(position);
        holder.setList(studentWithGrades);
    }

    public StudentWithGrades getItemAt(int position) {
        return getItem(position);
    }

}
