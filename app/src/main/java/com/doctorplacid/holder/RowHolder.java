package com.doctorplacid.holder;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.students.StudentWithGrades;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RowHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView sumTextView;
    private RecyclerView recyclerView;

    RecyclerView.OnScrollListener scrollListener;
    private ITableActivityListener listener;

    public RowHolder(@NonNull View itemView, Context context, TableAdapter adapter) {
        super(itemView);
        listener = (ITableActivityListener) context;
        nameTextView = itemView.findViewById(R.id.nameTextView);
        sumTextView = itemView.findViewById(R.id.sumTextView);
        recyclerView = itemView.findViewById(R.id.RecyclerViewRow);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false ));
        recyclerView.setHasFixedSize(true);

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                adapter.scrollAllItems(dx, dy, RowHolder.this);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        nameTextView.setOnLongClickListener(view -> {
            listener.openDeleteDialog(getAdapterPosition());
            return false;
        });
    }

    public void setList(StudentWithGrades studentWithGrades) {
        List<Grade> grades = studentWithGrades.getGrades();
        String nameText = studentWithGrades.getStudent().getName();

        int sum = 0;
        for (Grade grade : grades)
            sum += grade.getAmount();

        sumTextView.setText(String.valueOf(sum));
        nameTextView.setText(nameText);

        RowAdapter adapter = new RowAdapter(listener);
        recyclerView.setAdapter(adapter);
        adapter.submitList(grades);
    }

    public void scroll(int dx, int dy) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(scrollListener);
    }

}
