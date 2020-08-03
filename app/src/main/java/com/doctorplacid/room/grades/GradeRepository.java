package com.doctorplacid.room.grades;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.doctorplacid.room.TeachersDatabase;

import java.util.ArrayList;
import java.util.List;

public class GradeRepository {

    private GradeDAO gradeDAO;

    public GradeRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        gradeDAO = database.gradeDAO();
    }

    public void insert(List<Grade> grades) {
        new GradesInsertAsyncTask(gradeDAO).execute(grades);
    }

    public void update(Grade grade) {
        new GradeRepository.GradeUpdateAsyncTask(gradeDAO).execute(grade);
    }

    public void delete(Grade grade) {
        new GradeRepository.GradeDeleteAsyncTask(gradeDAO).execute(grade);
    }


    private static class GradesInsertAsyncTask extends AsyncTask<List<Grade>, Void, Void> {
        private GradeDAO gradeDAO;

        public GradesInsertAsyncTask(GradeDAO gradeDAO) {
            this.gradeDAO = gradeDAO;
        }

        @Override
        protected Void doInBackground(List<Grade>... grades) {
            gradeDAO.insert(grades[0]);
            return null;
        }
    }

    private static class GradeUpdateAsyncTask extends AsyncTask<Grade, Void, Void> {
        private GradeDAO gradeDAO;

        public GradeUpdateAsyncTask(GradeDAO gradeDAO) {
            this.gradeDAO = gradeDAO;
        }

        @Override
        protected Void doInBackground(Grade... grades) {
            gradeDAO.update(grades[0]);
            return null;
        }
    }

    private static class GradeDeleteAsyncTask extends AsyncTask<Grade, Void, Void> {
        private GradeDAO gradeDAO;

        public GradeDeleteAsyncTask(GradeDAO gradeDAO) {
            this.gradeDAO = gradeDAO;
        }

        @Override
        protected Void doInBackground(Grade... grades) {
            gradeDAO.delete(grades[0]);
            return null;
        }
    }

}
