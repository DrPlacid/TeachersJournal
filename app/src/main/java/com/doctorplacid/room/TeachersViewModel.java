package com.doctorplacid.room;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.doctorplacid.TableCalendar;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.grades.GradeRepository;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.lessons.LessonRepository;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.groups.GroupsRepository;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.room.students.StudentWithGrades;
import com.doctorplacid.room.students.StudentsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TeachersViewModel extends AndroidViewModel {

    private GroupsRepository groupsRepository;
    private StudentsRepository studentsRepository;
    private LessonRepository lessonRepository;
    private GradeRepository gradeRepository;

    private LiveData<List<Group>> groupsList;
    private Group currentGroup;

    private LiveData<List<StudentWithGrades>> studentsWithGrades;
    private LiveData<List<Lesson>> lessons;

    public TeachersViewModel(@NonNull Application application) {
        super(application);
        groupsRepository = new GroupsRepository(application);
        studentsRepository = new StudentsRepository(application);
        lessonRepository = new LessonRepository(application);
        gradeRepository = new GradeRepository(application);

        groupsList = groupsRepository.retrieveAll();
    }

    public void initDataSet(int groupId) {
        currentGroup = groupsRepository.retrieve(groupId);
        studentsWithGrades = studentsRepository.retrieveAll(groupId);
        lessons = lessonRepository.retrieveAll(groupId);
    }

    public String getCurrentGroupName() {
        return currentGroup.getName().replaceAll(" ", "");
    }

    public boolean insertColumn() {
        int groupId = currentGroup.getId();
        Lesson lesson = new Lesson(groupId);

        try {
            List<Integer> studentIds = studentsRepository.getIds(groupId);
            lessonRepository.insertColumn(lesson, studentIds);
            int lessons = currentGroup.getLessons() + 1;
            currentGroup.setLessons(lessons);
            updateGroup(currentGroup);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }

    public void deleteColumn(Lesson lesson) {
        lessonRepository.delete(lesson);
        int lessons = currentGroup.getLessons() - 1;
        currentGroup.setLessons(lessons);
        updateGroup(currentGroup);
    }


    public void insertGroup(String name) {
        Group group = new Group(name);
        groupsRepository.insert(group);
    }

    public void updateGroup(Group group) {
        groupsRepository.update(group);
    }

    public void deleteGroup(Group group) {
        groupsRepository.delete(group);
    }

    public LiveData<List<Group>> getAllGroups(){
        return groupsList;
    }

    public void insertStudent(String name) {
        List<Lesson> list = lessons.getValue();
        int groupId = currentGroup.getId();
        Student student = new Student(name, groupId);
        studentsRepository.insert(student, currentGroup, list);
    }

    public void deleteStudent(Student student) {
        studentsRepository.delete(student);
    }

    public LiveData<List<StudentWithGrades>> getAllStudents() {
        return studentsWithGrades;
    }

    public void updateLesson(Lesson lesson) {
        TableCalendar calendar = new TableCalendar();
        String day = calendar.getDay();
        String month = calendar.detMonth();
        Lesson newLesson = new Lesson(lesson);
        newLesson.setDay(day);
        newLesson.setMonth(month);
        lessonRepository.update(newLesson);
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return lessons;
    }

    public void clearCell(Grade grade) {
        Grade temp = new Grade(grade);
        temp.setAmount(0);
        temp.setPresence(false);
        gradeRepository.update(temp);
    }

}
