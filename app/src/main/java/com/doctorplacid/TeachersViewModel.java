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

    private LiveData<List<Group>> groupsList;
    private LiveData<List<StudentWithGrades>> studentsList;
    private LiveData<List<Lesson>> lessonsList;

    public TeachersViewModel(@NonNull Application application) {
        super(application);
        groupsRepository = new GroupsRepository(application);
        studentsRepository = new StudentsRepository(application);
        lessonRepository = new LessonRepository(application);

        groupsList = groupsRepository.retrieveAll();
    }

    public void initData(int groupId) {
        studentsList = studentsRepository.retrieveAll(groupId);
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



    public boolean insertStudent(Student student, int lessons) {
        return studentsRepository.insert(student, lessons);
    }

    public void updateStudent(Student student) {
        studentsRepository.update(student);
    }

    public void deleteStudent(Student student) {
        studentsRepository.delete(student);
    }

    public LiveData<List<StudentWithGrades>> getAllStudents(){
        return studentsList;
    }



    public void insertDate(Lesson lesson) {
        lessonRepository.insert(lesson);
    }

    public void deleteDate(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public LiveData<List<Lesson>> getAllDates() {
        return lessonsList;
    }


}
