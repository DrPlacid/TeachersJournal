package com.doctorplacid.room.students;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class StudentDAO {

    @Insert
    abstract long insertStudent(Student student);

    @Insert
    abstract void insertGrades(List<Grade> grades);

    public void insertNewStudentWithGrades(Student student, Group group) {
        List<Grade> grades = new ArrayList<>();

        int id = (int) insertStudent(student);

        for (int i = 0; i < group.getLessons(); i++)
            grades.add(new Grade(id));

        insertGrades(grades);
    }

    @Update
    abstract void update(Student student);

    @Delete
    abstract void delete(Student student);

    @Transaction
    @Query("SELECT * FROM students_table WHERE group_id = :groupId ORDER BY name")
    abstract LiveData<List<StudentWithGrades>> getStudentsWithGrades(int groupId);

    @Query("SELECT id FROM students_table WHERE group_id = :groupId")
    abstract List<Integer> getIds(int groupId);

}
