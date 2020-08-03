package com.doctorplacid.room.grades;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GradeDAO {

    @Insert
    void insert(List<Grade> grades);

    @Update
    void update(Grade grade);

    @Delete
    void delete(Grade grade);

}
