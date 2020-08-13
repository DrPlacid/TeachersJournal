package com.doctorplacid.room.students;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentsRepository {

    private StudentDAO studentDAO;

    public StudentsRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        studentDAO = database.studentDAO();
    }

    public void insert(Student student, Group group, List<Lesson> lessons) {
        new StudentInsertAsyncTask(studentDAO, group, lessons).execute(student);
    }

    public List<Integer> getIds(int groupId) throws ExecutionException, InterruptedException {
        return new StudentGetIdsAsyncTask(studentDAO).execute(groupId).get();
    }

    public void delete(Student student) {
        new StudentsRepository.StudentDeleteAsyncTask(studentDAO).execute(student);
    }

    public LiveData<List<StudentWithGrades>> retrieveAll(int groupId) {
        return studentDAO.getStudentsWithGrades(groupId);
    }

    private static class StudentInsertAsyncTask extends AsyncTask<Student, Void, Void> {
        private StudentDAO studentDAO;
        private Group group;
        private List<Lesson> lessons;

        public StudentInsertAsyncTask(StudentDAO studentDAO, Group group, List<Lesson> lessons) {
            this.studentDAO = studentDAO;
            this.group = group;
            this.lessons = lessons;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDAO.insertNewStudentWithGrades(students[0], group, lessons);
            return null;
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
