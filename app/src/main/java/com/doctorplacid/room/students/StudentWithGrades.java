package com.doctorplacid.room.students;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.doctorplacid.room.grades.Grade;

import java.util.List;

public class StudentWithGrades {

    @Embedded
    private Student student;

    @Relation(parentColumn = "id", entityColumn = "student_id")
    private List<Grade> grades;

    public StudentWithGrades(Student student, List<Grade> grades) {
        this.student = student;
        this.grades = grades;
    }

    public Student getStudent() {
        return student;
    }

    public List<Grade> getGrades() {
        return grades;
    }

}
