package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.room.students.Student;

public class NameHolder extends RecyclerView.ViewHolder {

    private TextView nameText;

    public NameHolder(@NonNull View itemView, ITableActivityListener listener) {
        super(itemView);
        nameText = itemView.findViewById(R.id.nameTextView);
        nameText.setOnLongClickListener(l -> {
            listener.openDeleteDialog(getAdapterPosition());
            return false;
        });
    }

    public void setText(Student student) {
        nameText.setText(student.getName());
    }
}
