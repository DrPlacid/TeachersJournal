package com.doctorplacid.activity;



import android.widget.EditText;

import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;

public interface ITableListener {

    void onOpenTable(int groupId);

    void openDeleteStudentDialog(Student student);
    void openDeleteGroupDialog(Group group);
    void openDeleteColumnDialog(Lesson lesson);

    void addStudent(String name);
    void deleteStudent();

    void addGroup(String name);
    void deleteGroup();

    void deleteColumn();

    void onGradeAmountEdited(CellViewHolder holder, EditText editText);
    void onGradePresenceEdited(Grade grade, int position);

}

