package com.doctorplacid.room.students;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.doctorplacid.room.groups.Group;


@Entity(tableName = "students_table",
        foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE,
                entity = Group.class, parentColumns = "id", childColumns = "group_id"))
public class Student {

    @ColumnInfo(name = "student_id")
    @PrimaryKey(autoGenerate = true)
    private int studentId;

    private String name;

    private int sum;

    @ColumnInfo(name = "group_id")
    private int groupId;

    public Student(String name, int groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

}
