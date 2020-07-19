package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.TableCalendar;
import com.doctorplacid.room.lessons.Lesson;

public class LessonHolder extends RecyclerView.ViewHolder {

    private TextView dateTextView;

    public LessonHolder(@NonNull View itemView, @NonNull ITableActivityListener listener) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        dateTextView.setOnClickListener(view -> {
            TableCalendar tableCalendar = new TableCalendar();
            String currentDate = tableCalendar.getDateTwoLines();
            listener.addDate(currentDate);
        });
        dateTextView.setOnLongClickListener(view -> {
            int position = getAdapterPosition();
            listener.deleteDate(position);
            return false;
        });
    }

    public void setText(Lesson lesson) {
        dateTextView.setText(lesson.getDate());
    }

}
