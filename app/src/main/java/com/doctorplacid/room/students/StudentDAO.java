package com.doctorplacid.room.students;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.doctorplacid.room.grades.Grade;

import java.util.List;

@Dao
public interface StudentDAO {

    @Insert
    long insertStudent(Student student);

    @Insert
    void insertGrades(List<Grade> grades);

    @Delete
    void delete(Student student);

    @Transaction
    @Query("SELECT * FROM students_table WHERE group_id = :groupId ORDER BY name")
    LiveData<List<StudentWithGrades>> getStudentsWithGrades(int groupId);

    @Query("SELECT id FROM students_table WHERE group_id = :groupId")
    List<Integer> getIds(int groupId);

}
