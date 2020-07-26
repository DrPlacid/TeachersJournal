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

import java.util.List;

public class TeachersViewModel extends AndroidViewModel {

    private GroupsRepository groupsRepository;
    private StudentsRepository studentsRepository;
    private LessonRepository lessonRepository;
    private GradeRepository gradeRepository;

    private LiveData<List<StudentWithGrades>> studentsWithGradesList;
    private LiveData<List<Group>> groupsList;
    private LiveData<List<Lesson>> lessonsList;

    public TeachersViewModel(@NonNull Application application) {
        super(application);
        groupsRepository = new GroupsRepository(application);
        studentsRepository = new StudentsRepository(application);
        lessonRepository = new LessonRepository(application);
        gradeRepository = new GradeRepository(application);

        groupsList = groupsRepository.retrieveAll();
    }

    public void initData(int groupId) {
        studentsWithGradesList = studentsRepository.retrieveAll(groupId);
        lessonsList = lessonRepository.retrieveAll(groupId);
    }

    public Group retrieveGroup(int id) {
        return groupsRepository.retrieve(id);
    }

    public void insertGroup(Group group) {
        groupsRepository.insert(group);
    }

    public void update(Group group) {
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

    public void updateStudent(Student student) {
        studentsRepository.update(student);
    }

    public void deleteStudent(Student student) {
        studentsRepository.delete(student);
    }

    public LiveData<List<StudentWithGrades>> getAllStudents(){
        return studentsWithGradesList;
    }


    public void updateLesson(Lesson lesson) {
        lessonRepository.update(lesson);
    }

    public void deleteDate(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return lessonsList;
    }

    public void updateGrade(Grade grade) {
        gradeRepository.update(grade);
    }


}
