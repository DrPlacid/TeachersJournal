package com.doctorplacid.activity;

import android.widget.EditText;

import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;

public interface ITableListener {

    void openTableFromNavigationPanel(int groupId);

    void addStudent(String name);
    void addGroup(String name);

    void deleteStudent(Student student);
    void deleteGroup(Group group);
    void deleteColumn(Lesson lesson);
    void clearCell(Grade grade);

    void gradeAmountEdited(CellViewHolder holder, EditText editText);
    void gradePresenceEdited(Grade grade, int position);


}

