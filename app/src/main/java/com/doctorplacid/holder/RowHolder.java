package com.doctorplacid.holder;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.adapter.GradesAdapter;
import com.doctorplacid.room.students.StudentWithGrades;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RowHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView sum;
    private RecyclerView recyclerView;

    private ITableActivityListener listener;

    private boolean isScrollEnabled;

    public RowHolder(@NonNull View itemView, Context context) {
        super(itemView);
        listener = (ITableActivityListener) context;
        isScrollEnabled = false;
        name = itemView.findViewById(R.id.nameTextView);
        sum = itemView.findViewById(R.id.sumTextView);
        recyclerView = itemView.findViewById(R.id.RecyclerViewRow);

        LinearLayoutManager manager =
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false ) {
            @Override
            public boolean canScrollHorizontally() {
                return isScrollEnabled;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        name.setOnLongClickListener(view -> {
            listener.openDeleteDialog(getAdapterPosition());
            return false;
        });
    }

    public void setList(StudentWithGrades studentWithGrades) {
        String nameText = studentWithGrades.getStudent().getName();
        String sumText = String.valueOf(studentWithGrades.getStudent().getSum());
        name.setText(nameText);
        sum.setText(sumText);

        GradesAdapter adapter = new GradesAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItems(studentWithGrades.getGrades());
    }

    public void scroll(int dx, int dy) {
        this.isScrollEnabled = true;
        recyclerView.scrollBy(dx, dy);
        this.isScrollEnabled = false;
    }

}
