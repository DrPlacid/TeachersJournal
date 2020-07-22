package com.doctorplacid.room.grades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.doctorplacid.room.students.Student;

@Entity(tableName = "grades_table",
        foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE,
                entity = Student.class, parentColumns = "id", childColumns = "student_id"))
public class Grade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grade_id")
    private int gradeId;

    private int amount;

    private boolean presence;

    @ColumnInfo(name = "student_id")
    private int studentId;

    public Grade(int studentId) {
        this.studentId = studentId;
        this.amount = 0;
        this.presence = true;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
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

    public boolean isPresence() {
        return presence;
    }

    public int getStudentId() {
        return studentId;
    }
}
