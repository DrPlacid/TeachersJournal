package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;
import com.doctorplacid.room.lessons.Lesson;

public class ColumnHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView dateTextView;
    private Lesson lesson;

    public ColumnHeaderViewHolder(@NonNull View itemView, @NonNull ITableListener listener) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        dateTextView.setOnLongClickListener(view -> {
            listener.openDeleteColumnDialog(lesson);
            return false;
        });
    }

    public void setEntity(Lesson lesson) {
        this.lesson = lesson;
        dateTextView.setText(lesson.getDate());
    }

}
