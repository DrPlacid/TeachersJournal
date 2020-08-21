package com.doctorplacid.room;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
        studentsWithGrades = studentsRepository.retrieveAll(groupId);
        lessons = lessonRepository.retrieveAll(groupId);
    }

    public void insertColumn(int groupId) throws ExecutionException, InterruptedException {
        Lesson lesson = new Lesson(groupId);
        List<Integer> studentIds = studentsRepository.getIds(groupId);
        lessonRepository.insertColumn(lesson, studentIds);
    }

    public Group retrieveGroup(int id) {
        return groupsRepository.retrieve(id);
    }

    public void insertGroup(Group group) {
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

    public void insertStudent(Student student, Group group, List<Lesson> lessons) {
        studentsRepository.insert(student, group, lessons);
    }

    public void deleteStudent(Student student) {
        studentsRepository.delete(student);
    }

    public LiveData<List<StudentWithGrades>> getAllStudents() {
        return studentsWithGrades;
    }

    public void updateLesson(Lesson lesson) {
        lessonRepository.update(lesson);
    }

    public void deleteLesson(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return lessons;
    }

    public void updateGrade(Grade grade) {
        gradeRepository.update(grade);
    }

}
