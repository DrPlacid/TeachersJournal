package com.doctorplacid.activity;



import android.widget.EditText;

import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;

public interface ITableListener {

    void onOpenTable(int groupId);

    void onClearCell(Grade grade);

    void onAddStudent(String name);
    void onAddGroup(String name);

    void onDeleteStudent(Student student);
    void onDeleteGroup(Group group);
    void onDeleteColumn(Lesson lesson);

    void onGradeAmountEdited(CellViewHolder holder, EditText editText);
    void onGradePresenceEdited(Grade grade, int position);

    void onChangeLanguage(String tag);

}

