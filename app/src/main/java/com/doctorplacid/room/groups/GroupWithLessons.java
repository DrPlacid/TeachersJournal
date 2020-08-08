package com.doctorplacid.room.groups;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.doctorplacid.room.lessons.Lesson;

import java.util.List;

public class GroupWithLessons {

    @Embedded
    private Group group;

    @Relation(parentColumn = "id", entityColumn = "group_id")
    private List<Lesson> lessons;

    public GroupWithLessons(Group group, List<Lesson> lessons) {
        this.group = group;
        this.lessons = lessons;
    }

    public Group getGroup() {
        return group;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}
