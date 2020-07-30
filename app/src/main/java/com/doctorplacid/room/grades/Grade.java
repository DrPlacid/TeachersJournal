package com.doctorplacid.room.grades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.doctorplacid.room.students.Student;

@Entity(tableName = "grades_table",
        foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE,
                entity = Student.class, parentColumns = "id", childColumns = "student_id"), indices = @Index("student_id"))
public class Grade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grade_id")
    private int gradeId;

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
        this.gradeId = other.getGradeId();
        this.present = other.isPresent();
        this.amount = other.getAmount();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGradeId() {
        return gradeId;
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
