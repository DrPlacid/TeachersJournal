package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.NameHolder;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.room.students.StudentWithGrades;


public class NamesAdapter extends ListAdapter<StudentWithGrades, NameHolder> {

    private ITableActivityListener Listener;

    public NamesAdapter(ITableActivityListener listener) {
        super(DIFF_CALLBACK);
        this.Listener = listener;
    }

    private static final DiffUtil.ItemCallback<StudentWithGrades> DIFF_CALLBACK = new DiffUtil.ItemCallback<StudentWithGrades>() {
        @Override
        public boolean areItemsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            return oldItem.getStudent().getStudentId() == newItem.getStudent().getStudentId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StudentWithGrades oldItem, @NonNull StudentWithGrades newItem) {
            return oldItem.getStudent().getName().equals(newItem.getStudent().getName());
        }
    };


    @NonNull
    @Override
    public NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_header, parent, false);
        return new NameHolder(itemView, Listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NameHolder holder, int position) {
        Student student = getItem(position).getStudent();
        holder.setText(student);
    }


    public Student getItemAt(int position) {
        return getItem(position).getStudent();
    }
}
