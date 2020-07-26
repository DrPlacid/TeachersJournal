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

    public void update(Lesson lesson) {
        new DateUpdateAsyncTask(lessonDAO).execute(lesson);
    }

    public void delete(Lesson lesson) {
        new LessonRepository.DateDeleteAsyncTask(lessonDAO).execute(lesson);
    }

    public LiveData<List<Lesson>> retrieveAll(int groupId) {
        return lessonDAO.retrieveAll(groupId);
    }

    private static class DateUpdateAsyncTask extends AsyncTask<Lesson, Void, Void> {
        private LessonDAO lessonDAO;

        public DateUpdateAsyncTask(LessonDAO lessonDAO) {
            this.lessonDAO = lessonDAO;
        }

        @Override
        protected Void doInBackground(Lesson... lessons) {
            lessonDAO.update(lessons[0]);
            return null;
        }
    }

    private static class DateDeleteAsyncTask extends AsyncTask<Lesson, Void, Void> {
        private LessonDAO lessonDAO;

        public DateDeleteAsyncTask(LessonDAO lessonDAO) {
            this.lessonDAO = lessonDAO;
        }

        @Override
        protected Void doInBackground(Lesson... lessons) {
            lessonDAO.delete(lessons[0]);
            return null;
        }
    }
}
