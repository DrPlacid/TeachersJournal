package com.doctorplacid.activity;

import android.content.Context;

import com.doctorplacid.dialog.DialogAddGroup;
import com.doctorplacid.dialog.DialogAddStudent;
import com.doctorplacid.dialog.DialogDeleteGroup;
import com.doctorplacid.dialog.DialogDeleteColumn;
import com.doctorplacid.dialog.DialogDeleteStudent;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;

public class DialogManager {

    public static void openAddStudentDialog(Context context) {
        DialogAddStudent dialogAddStudent = new DialogAddStudent();
        dialogAddStudent.show(((MainActivity) context).getSupportFragmentManager(), "Add student dialog");
    }

    public static void openAddGroupDialog(Context context) {
        DialogAddGroup dialogAddGroup = new DialogAddGroup();
        dialogAddGroup.show(((MainActivity) context).getSupportFragmentManager(), "Add group dialog");
    }

    public static void openDeleteStudentDialog(Student student, Context context) {
        DialogDeleteStudent dialogDelete = new DialogDeleteStudent(student);
        dialogDelete.show(((MainActivity) context).getSupportFragmentManager(), "Delete student dialog");
    }

    public static void openDeleteGroupDialog(Group group, Context context) {
        DialogDeleteGroup dialogDelete = new DialogDeleteGroup(group);
        dialogDelete.show(((MainActivity) context).getSupportFragmentManager(), "Delete group dialog");
    }

    public static void openDeleteColumnDialog(Lesson lesson, Context context) {
        DialogDeleteColumn dialogDelete = new DialogDeleteColumn(lesson);
        dialogDelete.show(((MainActivity) context).getSupportFragmentManager(), "Delete column dialog");
    }

}
