package com.doctorplacid.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.R;
import com.doctorplacid.activity.DialogManager;
import com.doctorplacid.room.lessons.Lesson;

public class ColumnHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView dateTextView;
    private TextView monthTextView;
    private ImageView slashImage;
    private Lesson lesson;

    public ColumnHeaderViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.dayTextView);
        monthTextView = itemView.findViewById(R.id.monthTextView);
        slashImage = itemView.findViewById(R.id.slash);

        itemView.setOnLongClickListener(view -> {
            DialogManager.openDeleteColumnDialog(lesson, context);
            return false;
        });
    }

    public void setData(Lesson lesson) {
        this.lesson = lesson;
        String day = lesson.getDay();
        String month = lesson.getMonth();
        dateTextView.setText(day);
        monthTextView.setText(month);
        if (!("").equals(month) && !("").equals(day)) {
            slashImage.setVisibility(View.VISIBLE);
        } else {
            slashImage.setVisibility(View.INVISIBLE);
        }
    }

}
