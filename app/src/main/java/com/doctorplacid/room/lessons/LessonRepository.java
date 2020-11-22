package com.doctorplacid.room.lessons;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;

import java.util.ArrayList;
import java.util.List;

public class LessonRepository {

    private static LessonDAO lessonDAO;
    private Handler handler;

    public LessonRepository(Application application, Handler handler) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        lessonDAO = database.dateDAO();
        this.handler = handler;
    }

    public void insert(Lesson lesson, List<Integer> studentIds) {
         handler.post(new InsertRunnable(lesson, studentIds));
    }

    public void update(Lesson lesson) {
        handler.post(new UpdateRunnable(lesson));
    }

    public void delete(Lesson lesson) {
        handler.post(new DeleteRunnable(lesson));
    }

    public LiveData<List<Lesson>> retrieveAll(int groupId) {
        return lessonDAO.retrieveAll(groupId);
    }

    private static class InsertRunnable implements Runnable {
        Lesson lesson;
        List<Integer> studentIds;

        public InsertRunnable(Lesson lesson, List<Integer> studentIds) {
            this.lesson = lesson;
            this.studentIds = studentIds;
        }

        @Override
        public void run() {
            int lessonId = (int) lessonDAO.insert(lesson);

            List<Grade> grades = new ArrayList<>();

            for(int studentId : studentIds) {
                grades.add(new Grade(studentId, lessonId));
            }

            lessonDAO.insertGrades(grades);
        }
    }

    private static class UpdateRunnable implements Runnable {
        Lesson lesson;

        public UpdateRunnable(Lesson lesson) {
            this.lesson = lesson;
        }

        @Override
        public void run() {
            lessonDAO.update(lesson);
        }
    }

    private static class DeleteRunnable implements Runnable {
        Lesson lesson;

        public DeleteRunnable(Lesson lesson) {
            this.lesson = lesson;
        }


        @Override
        public void run() {
            lessonDAO.delete(lesson);
        }
    }
}
