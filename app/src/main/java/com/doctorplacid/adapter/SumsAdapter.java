package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.holder.SumHolder;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.room.students.StudentWithGrades;

import java.util.ArrayList;
import java.util.List;

public class SumsAdapter extends ListAdapter<StudentWithGrades, SumHolder> {


    public SumsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<StudentWithGrades> DIFF_CALLBACK = new DiffUtil.ItemCallback<StudentWithGrades>() {
        @Override
        public boolean areItemsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            return oldItem.getStudent().getStudentId() == newItem.getStudent().getStudentId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            return oldItem.getStudent().getSum() == newItem.getStudent().getSum();
        }
    };

    @NonNull
    @Override
    public SumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_sum, parent, false);
        return new SumHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SumHolder holder, int position) {
        Student student = getItem(position).getStudent();
        holder.SetText(student);
    }

}
