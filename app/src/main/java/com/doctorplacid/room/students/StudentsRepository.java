package com.doctorplacid.room.students;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.grades.GradeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentsRepository {

    private StudentDAO studentDAO;

    public StudentsRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        studentDAO = database.studentDAO();
    }

    public void insert(Student student, int lessons) {
        new StudentInsertAsyncTask(studentDAO, lessons).execute(student);
    }

    public void update(Student student) {
        new StudentsRepository.StudentUpdateAsyncTask(studentDAO).execute(student);
    }

    public void delete(Student student) {
        new StudentsRepository.StudentDeleteAsyncTask(studentDAO).execute(student);
    }

    public LiveData<List<StudentWithGrades>> retrieveAll(int groupId) {
        return studentDAO.getStudentsWithGrades(groupId);
    }

    private static class StudentInsertAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDAO studentDAO;
        private int lessons;


        public StudentInsertAsyncTask(StudentDAO studentDAO, int lessons) {
            this.studentDAO = studentDAO;
            this.lessons = lessons;
        }

        @Override
        protected Void doInBackground(Student... students) {
            StudentWithGrades studentWithGrades = new StudentWithGrades(students[0]);
            studentDAO.insertNewStudentWithGrades(studentWithGrades, lessons);
            return null;
        }

    }

    private static class StudentUpdateAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDAO studentDAO;

        public StudentUpdateAsyncTask(StudentDAO studentDAO) {
            this.studentDAO = studentDAO;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDAO.update(students[0]);
            return null;
        }
    }

    private static class StudentDeleteAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDAO studentDAO;

        public StudentDeleteAsyncTask(StudentDAO studentDAO) {
            this.studentDAO = studentDAO;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDAO.delete(students[0]);
            return null;
        }
    }

}
