package com.doctorplacid.room.lessons;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LessonDAO {

    @Insert
    void insert(Lesson lesson);

    @Delete
    void delete(Lesson lesson);

    @Query("SELECT * FROM lessons_table WHERE group_id = :groupId")
    LiveData<List<Lesson>> retrieveAll(int groupId);

}
