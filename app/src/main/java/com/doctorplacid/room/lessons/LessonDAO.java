package com.doctorplacid.room.lessons;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LessonDAO {

    @Insert
    long insert(Lesson lesson);

    @Update
    void update(Lesson lesson);

    @Delete
    void delete(Lesson lesson);

    @Query("SELECT * FROM lessons_table WHERE group_id = :groupId ORDER BY id ")
    LiveData<List<Lesson>> retrieveAll(int groupId);

}
