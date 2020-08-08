package com.doctorplacid.room.lessons;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.doctorplacid.room.groups.Group;

@Entity(tableName = "lessons_table",
        foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE,
                entity = Group.class, parentColumns = "id", childColumns = "group_id"), indices = @Index("group_id"))
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    @ColumnInfo(name = "group_id")
    private int groupId;

    public Lesson(int groupId) {
        this.groupId = groupId;
        this.date = "";
    }

    //copy constructor
    public Lesson(Lesson lesson) {
        this.id = lesson.getId();
        this.date = lesson.getDate();
        this.groupId = lesson.getGroupId();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getGroupId() {
        return groupId;
    }
}
