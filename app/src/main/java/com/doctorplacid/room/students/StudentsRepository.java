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
    private GradeRepository gradeRepository;

    public StudentsRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        studentDAO = database.studentDAO();
        gradeRepository = new GradeRepository(application);
    }

    public void insert(Student student, int lessons) {
        AsyncTask asyncTask = new StudentInsertAsyncTask(studentDAO).execute(student);
        int newId = 0;
        String name = student.getName();
        AsyncTask getNewId = new GetNewIdAsyncTask(studentDAO).execute(name);
        try {
            newId = (int) getNewId.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        List<Grade> grades = new ArrayList<>();

        for (int i = 1; i < lessons; i++)
            grades.add(new Grade(newId));

         */

        gradeRepository.insert(newId);
    }

    public void update(Student student) {
        new StudentsRepository.StudentUpdateAsyncTask(studentDAO).execute(student);
    }

    public void delete(Student student) {
        new StudentsRepository.StudentDeleteAsyncTask(studentDAO).execute(student);
    }

    public LiveData<List<Student>> retrieveAll(int groupId) {
        return studentDAO.retrieveAll(groupId);
    }

    private static class StudentInsertAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDAO studentDAO;


        public StudentInsertAsyncTask(StudentDAO studentDAO) {
            this.studentDAO = studentDAO;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDAO.insert(students[0]);
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

    private static class GetNewIdAsyncTask extends AsyncTask<String, Void, Integer> {
        private StudentDAO studentDAO;


        public GetNewIdAsyncTask(StudentDAO studentDAO) {
            this.studentDAO = studentDAO;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return studentDAO.getIdByName(strings[0]).getStudentId();
        }

    }

}
