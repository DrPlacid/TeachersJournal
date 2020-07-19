package com.doctorplacid.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.grades.GradeDAO;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.lessons.LessonDAO;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.groups.GroupDAO;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.room.students.StudentDAO;

@Database(entities = {Group.class, Student.class, Lesson.class, Grade.class}, version = 1)
public abstract class TeachersDatabase extends RoomDatabase {

    private static TeachersDatabase instance;

    public abstract GroupDAO groupDAO();
    public abstract StudentDAO studentDAO();
    public abstract LessonDAO dateDAO();
    public abstract GradeDAO gradeDAO();

    public static synchronized TeachersDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TeachersDatabase.class, "teachers_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
