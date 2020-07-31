package com.doctorplacid.holder;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.students.StudentWithGrades;

import android.content.Context;
import android.util.Log;
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

    private RecyclerView.OnScrollListener scrollListener;
    private ITableActivityListener listener;
    private RowAdapter adapter;

    public RowHolder(@NonNull View itemView, Context context, TableAdapter adapter) {
        super(itemView);
        listener = (ITableActivityListener) context;
        nameTextView = itemView.findViewById(R.id.nameTextView);
        sumTextView = itemView.findViewById(R.id.sumTextView);
        recyclerView = itemView.findViewById(R.id.RecyclerViewRow);

        LinearLayoutManager manager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false ){
            @Override
            public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrollRange = super.scrollHorizontallyBy(dx, recycler, state);
                int overscroll = dx - scrollRange;
                if (overscroll > 0) {
                    listener.expandButton();
                }
                return scrollRange;
            }
        };

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                adapter.scrollAllItems(dx, dy, RowHolder.this);
                if(dx < 0) listener.collapseButton();
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

    public void scroll(int dx, int dy) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(scrollListener);
    }

}
