package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.NameHolder;
import com.doctorplacid.room.students.Student;

import java.util.ArrayList;
import java.util.List;

public class NamesAdapter extends RecyclerView.Adapter<NameHolder> {

    private List<Student> studentNames = new ArrayList<>();

    private ITableActivityListener iGlobalListener;

    public NamesAdapter(ITableActivityListener iGlobalListener) {
        this.iGlobalListener = iGlobalListener;
    }

    @NonNull
    @Override
    public NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_header, parent, false);
        return new NameHolder(itemView, iGlobalListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NameHolder holder, int position) {
        holder.setText(studentNames.get(position));
    }

    @Override
    public int getItemCount() {
        return studentNames.size();
    }

    public void setItems(List<Student> students) {
        this.studentNames = students;
        notifyDataSetChanged();
    }

    public Student getItemAt(int position) {
        return studentNames.get(position);
    }
}
