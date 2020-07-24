package com.doctorplacid.room.students;

import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.doctorplacid.room.grades.Grade;

import java.util.List;

public class StudentWithGrades {

    @Embedded
    private Student student;

    @Relation(parentColumn = "id", entityColumn = "student_id")
    private List<Grade> grades;

    public StudentWithGrades(Student student) {
        this.student = student;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
        int sum = 0;
        for (Grade grade : grades)
            sum += grade.getAmount();
        this.student.setSum(sum);
    }

    public Student getStudent() {
        return student;
    }

    public List<Grade> getGrades() {
        return grades;
    }

}
