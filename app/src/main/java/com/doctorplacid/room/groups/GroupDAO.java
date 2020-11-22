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
public interface GroupDAO {

    @Insert
    long insert(Group group);

    @Insert
    void insertLessons(List<Lesson> lessons);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);

    @Query("SELECT * FROM group_table WHERE id = :id")
    Group retrieve(int id);

    @Query("SELECT * FROM group_table ORDER BY id")
    LiveData<List<Group>> retrieveAll();
}
