package com.doctorplacid;

import android.app.Application;

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

    private Map<Integer, LiveData<List<StudentWithGrades>>> studentsWithGrades = new HashMap<>();
    private Map<Integer, LiveData<List<Lesson>>> lessons = new HashMap<>();

    public TeachersViewModel(@NonNull Application application) {
        super(application);
        groupsRepository = new GroupsRepository(application);
        studentsRepository = new StudentsRepository(application);
        lessonRepository = new LessonRepository(application);
        gradeRepository = new GradeRepository(application);

        groupsList = groupsRepository.retrieveAll();
    }

    public void initStudentsData(List<Group> groups) {
        for (Group group : groups) {
            int id = group.getId();
            lessons.put(id, lessonRepository.retrieveAll(id));
            studentsWithGrades.put(id, studentsRepository.retrieveAll(id));
        }
    }

    public void insertColumn(int groupId) throws ExecutionException, InterruptedException {

        List<Grade> grades = new ArrayList<>();

        for(int id : studentsRepository.getIds(groupId)) {
            grades.add(new Grade(id));
        }
        gradeRepository.insert(grades);

        lessonRepository.insert(new Lesson(groupId));
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

    public void insertStudent(Student student, Group group) {
        studentsRepository.insert(student, group);
    }

    public void deleteStudent(Student student) {
        studentsRepository.delete(student);
    }

    public LiveData<List<StudentWithGrades>> getAllStudents(int groupId){
        return studentsWithGrades.get(groupId);
    }

    public void updateLesson(Lesson lesson) {
        lessonRepository.update(lesson);
    }

    public void deleteDate(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public LiveData<List<Lesson>> getAllLessons(int groupId) {
        return lessons.get(groupId);
    }

    public void updateGrade(Grade grade) {
        gradeRepository.update(grade);
    }


}
