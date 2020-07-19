package com.doctorplacid.room.students;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentDAO {

    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);

    @Query("SELECT * FROM students_table WHERE name = :name")
    Student getIdByName(String name);

    @Query("SELECT * FROM students_table WHERE group_id = :groupId ORDER BY name")
    LiveData<List<Student>> retrieveAll(int groupId);

}
