package com.doctorplacid.room.lessons;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.doctorplacid.room.groups.Group;

@Entity(tableName = "lessons_table",
        foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE,
                entity = Group.class, parentColumns = "id", childColumns = "group_id"))
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    @ColumnInfo(name = "group_id")
    private int groupId;

    public Lesson(String date, int groupId) {
        this.date = date;
        this.groupId = groupId;
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
