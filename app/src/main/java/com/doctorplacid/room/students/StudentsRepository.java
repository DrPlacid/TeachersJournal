package com.doctorplacid.room.students;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentsRepository {

    private static StudentDAO studentDAO;
    private Handler handler;

    public StudentsRepository(Application application, Handler handler) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        studentDAO = database.studentDAO();
        this.handler = handler;
    }

    public void insert(Student student, Group group, List<Lesson> lessons) {
        handler.post(new InsertRunnable(student, group, lessons));
    }

    public List<Integer> getIds(int groupId) throws ExecutionException, InterruptedException {
        return new StudentGetIdsAsyncTask(studentDAO).execute(groupId).get();
    }

    public void delete(Student student) {
        handler.post(new DeleteRunnable(student));
    }

    public LiveData<List<StudentWithGrades>> retrieveAll(int groupId) {
        return studentDAO.getStudentsWithGrades(groupId);
    }

    private static class InsertRunnable implements Runnable {
        Student student;
        Group group;
        List<Lesson> lessons;

        public InsertRunnable(Student student, Group group, List<Lesson> lessons) {
            this.student = student;
            this.group = group;
            this.lessons = lessons;
        }

        @Override
        public void run() {
            List<Grade> grades = new ArrayList<>();

            int groupId = (int) studentDAO.insertStudent(student);

            for (int i = 0; i < group.getLessons(); i++) {
                int lessonId = lessons.get(i).getId();
                grades.add(new Grade(groupId, lessonId));
            }

            studentDAO.insertGrades(grades);
        }
    }

    private static class DeleteRunnable implements Runnable {
        Student student;

        public DeleteRunnable(Student student) {
            this.student = student;
        }


        @Override
        public void run() {
            studentDAO.delete(student);
        }
    }


    private static class StudentGetIdsAsyncTask extends AsyncTask<Integer, Void, List<Integer>> {
        private StudentDAO studentDAO;

        public StudentGetIdsAsyncTask(StudentDAO studentDAO) {
            this.studentDAO = studentDAO;
        }

        @Override
        protected List<Integer> doInBackground(Integer... integers) {
            return studentDAO.getIds(integers[0]);
        }
    }

}
