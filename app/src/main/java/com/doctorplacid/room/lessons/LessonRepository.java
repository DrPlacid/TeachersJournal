package com.doctorplacid.room.lessons;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;

import java.util.List;

public class LessonRepository {

    private LessonDAO lessonDAO;

    public LessonRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        lessonDAO = database.dateDAO();
    }

    public void insert(Lesson lesson) {
        new LessonInsertAsyncTask(lessonDAO).execute(lesson);
    }

    public void update(Lesson lesson) {
        new LessonUpdateAsyncTask(lessonDAO).execute(lesson);
    }

    public void delete(Lesson lesson) {
        new LessonDeleteAsyncTask(lessonDAO).execute(lesson);
    }

    public LiveData<List<Lesson>> retrieveAll(int groupId) {
        return lessonDAO.retrieveAll(groupId);
    }


    private static class LessonInsertAsyncTask extends AsyncTask<Lesson, Void, Void> {
        private LessonDAO lessonDAO;

        public LessonInsertAsyncTask(LessonDAO lessonDAO) {
            this.lessonDAO = lessonDAO;
        }

        @Override
        protected Void doInBackground(Lesson... lessons) {
            lessonDAO.insert(lessons[0]);
            return null;
        }
    }

    private static class LessonUpdateAsyncTask extends AsyncTask<Lesson, Void, Void> {
        private LessonDAO lessonDAO;

        public LessonUpdateAsyncTask(LessonDAO lessonDAO) {
            this.lessonDAO = lessonDAO;
        }

        @Override
        protected Void doInBackground(Lesson... lessons) {
            lessonDAO.update(lessons[0]);
            return null;
        }
    }

    private static class LessonDeleteAsyncTask extends AsyncTask<Lesson, Void, Void> {
        private LessonDAO lessonDAO;

        public LessonDeleteAsyncTask(LessonDAO lessonDAO) {
            this.lessonDAO = lessonDAO;
        }

        @Override
        protected Void doInBackground(Lesson... lessons) {
            lessonDAO.delete(lessons[0]);
            return null;
        }
    }
}
