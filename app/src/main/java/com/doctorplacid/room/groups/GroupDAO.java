package com.doctorplacid.room.groups;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.lessons.Lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Dao
public abstract class GroupDAO {

    @Insert
    abstract long insert(Group group);

    @Insert
    abstract long[] insertLessons(List<Lesson> lessons);

    void insertNewGroup(Group group) {
        int id = (int) insert(group);
        List<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < group.getLessons(); i++) {
            lessons.add(new Lesson(id));
        }

        long[] ids = insertLessons(lessons);
    }

    @Update
    abstract void update(Group group);

    @Delete
    abstract void delete(Group group);

    @Query("SELECT * FROM group_table WHERE id = :id")
    abstract Group retrieve(int id);

    @Query("SELECT * FROM group_table ORDER BY id")
    abstract LiveData<List<Group>> retrieveAll();
}
