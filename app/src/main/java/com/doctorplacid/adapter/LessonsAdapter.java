package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.LessonHolder;
import com.doctorplacid.room.lessons.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonHolder> {

    private List<Lesson> lessonsList = new ArrayList<>();
    private ITableActivityListener listener;

    private int groupId;
    private int lessons;

    public LessonsAdapter(ITableActivityListener listener, int groupId, int lessons) {
        this.listener = listener;
        this.groupId = groupId;
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_column_header, parent, false);
        return new LessonHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonHolder holder, int position) {
        Lesson lesson = lessonsList.get(position);
        holder.setText(lesson);
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    public void setItems(List<Lesson> lessonsList) {
        this.lessonsList = lessonsList;
            for(int i = 0; i < lessons; i++)
                this.lessonsList.add(new Lesson("", groupId));
        notifyDataSetChanged();
    }

    public Lesson getItemAt(int position) {
        return lessonsList.get(position);
    }
}
