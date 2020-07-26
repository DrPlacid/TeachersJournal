package com.doctorplacid.holder;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.room.students.StudentWithGrades;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RowHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView sum;
    private RecyclerView recyclerView;
    private Context context;

    RecyclerView.OnScrollListener scrollListener;
    private ITableActivityListener listener;

    public RowHolder(@NonNull View itemView, Context context, TableAdapter adapter) {
        super(itemView);
        this.context = context;
        listener = (ITableActivityListener) context;
        name = itemView.findViewById(R.id.nameTextView);
        sum = itemView.findViewById(R.id.sumTextView);
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

        RowAdapter adapter = new RowAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.submitList(studentWithGrades.getGrades());
    }

    public void scroll(int dx, int dy) {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.scrollBy(dx, dy);
        recyclerView.addOnScrollListener(scrollListener);
    }

}
