package com.doctorplacid.room.grades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;

@Entity(tableName = "grades_table",
        foreignKeys = {
                @ForeignKey(onDelete = ForeignKey.CASCADE,
                        entity = Student.class, parentColumns = "id", childColumns = "student_id"),
                @ForeignKey(onDelete = ForeignKey.CASCADE,
                        entity = Lesson.class, parentColumns = "id", childColumns = "lesson_id"),
        })
public class Grade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    private int amount;

    private boolean presence;

    @ColumnInfo(name = "student_id")
    private int studentId;

    @ColumnInfo(name = "lesson_id")
    private int lessonId;

    public Grade(int studentId, int lessonId) {
        this.studentId = studentId;
        this.lessonId = lessonId;
        this.presence = false;
    }

    // copy constructor
    public Grade(Grade other) {
        this.studentId = other.getStudentId();
        this.lessonId = other.getLessonId();
        this.id = other.getId();
        this.presence = other.isPresence();
        this.amount = other.getAmount();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPresence() {
        return presence;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getLessonId() {
        return lessonId;
    }

}
