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
        })
public class Grade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    private int amount;

    private boolean present;

    @ColumnInfo(name = "student_id")
    private int studentId;

    public Grade(int studentId) {
        this.studentId = studentId;
        this.present = false;
    }

    // copy constructor
    public Grade(Grade other) {
        this.studentId = other.getStudentId();
        this.id = other.getId();
        this.present = other.isPresent();
        this.amount = other.getAmount();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPresent(boolean present) {
        this.present = present;
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

    public boolean isPresent() {
        return present;
    }

    public int getStudentId() {
        return studentId;
    }

}
