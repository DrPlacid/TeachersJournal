package com.doctorplacid.room.grades;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.doctorplacid.room.TeachersDatabase;

import java.util.ArrayList;
import java.util.List;

public class GradeRepository {

    private static GradeDAO gradeDAO;
    private Handler handler;

    public GradeRepository(Application application, Handler handler) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        gradeDAO = database.gradeDAO();
        this.handler = handler;
    }

    public void insert(List<Grade> grades) {
        handler.post(new InsertRunnable(grades));
    }

    public void update(Grade grade) {
        handler.post(new UpdateRunnable(grade));
    }


    private static class InsertRunnable implements Runnable {
        List<Grade> grades;

        public InsertRunnable(List<Grade> grades) {
            this.grades = grades;
        }

        @Override
        public void run() {
            gradeDAO.insert(grades);
        }
    }

    private static class UpdateRunnable implements Runnable {
        Grade grade;

        public UpdateRunnable(Grade grade) {
            this.grade = grade;
        }

        @Override
        public void run() {
            gradeDAO.update(grade);
        }
    }

}
