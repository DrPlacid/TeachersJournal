package com.doctorplacid.holder;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.StudentWithGrades;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RowHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView sumTextView;
    private RecyclerView recyclerView;

    private ITableActivityListener listener;
    private RowAdapter adapter;

    public RowHeaderViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        listener = (ITableActivityListener) context;
        nameTextView = itemView.findViewById(R.id.nameTextView);
        sumTextView = itemView.findViewById(R.id.sumTextView);
        recyclerView = itemView.findViewById(R.id.RecyclerViewRow);

        nameTextView.setOnLongClickListener(view -> {
            listener.openDeleteDialog(getAdapterPosition());
            return false;
        });
    }

    public void setList(StudentWithGrades studentWithGrades) {
        List<Grade> grades = studentWithGrades.getGrades();
        String nameText = studentWithGrades.getStudent().getName();
        nameTextView.setText(nameText);
        adapter = new RowAdapter(listener);
        recyclerView.setAdapter(adapter);
        submitList(grades);
    }

    public void submitList(List<Grade> grades){
        int sum = 0;
        for (Grade grade : grades)
            sum += grade.getAmount();

        sumTextView.setText(String.valueOf(sum));
        adapter.submitList(grades);
    }

    public RecyclerView getRecycler() {
        return recyclerView;
    }

}
