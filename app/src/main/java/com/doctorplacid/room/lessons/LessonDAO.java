package com.doctorplacid.room.lessons;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.doctorplacid.room.grades.Grade;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class LessonDAO {

    @Insert
    abstract void insertGrades(List<Grade> grades);

    @Transaction
    void insertColumn(Lesson lesson, List<Integer> studentIds) {
        int lessonId = (int) insert(lesson);

        List<Grade> grades = new ArrayList<>();

        for(int studentId : studentIds) {
            grades.add(new Grade(studentId, lessonId));
        }

        insertGrades(grades);
    }

    @Insert
    abstract long insert(Lesson lesson);

    @Update
    abstract void update(Lesson lesson);

    @Delete
    abstract void delete(Lesson lesson);

    @Query("SELECT * FROM lessons_table WHERE group_id = :groupId ORDER BY id ")
    abstract LiveData<List<Lesson>> retrieveAll(int groupId);

}
