package com.doctorplacid;



import android.widget.EditText;

import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.lessons.Lesson;

public interface ITableListener {

    void onPreInitTable(int groupId);

    void openDeleteDialog(int position);

    void addStudent(String name);
    void deleteStudent();

    void addGroup(String name);
    void deleteGroup();

    void onGradeAmountEdited(CellViewHolder holder, EditText editText);
    void onGradePresenceEdited(Grade grade, int position);

}

