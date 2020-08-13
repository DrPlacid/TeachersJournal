package com.doctorplacid.room.lessons;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LessonRepository {

    private LessonDAO lessonDAO;

    public LessonRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        lessonDAO = database.dateDAO();
    }

    public int insert(Lesson lesson) {
        try {
            return new LessonInsertAsyncTask(lessonDAO).execute(lesson).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -666;
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


    private static class LessonInsertAsyncTask extends AsyncTask<Lesson, Void, Integer> {
        private LessonDAO lessonDAO;

        public LessonInsertAsyncTask(LessonDAO lessonDAO) {
            this.lessonDAO = lessonDAO;
        }

        @Override
        protected Integer doInBackground(Lesson... lessons) {
            return (int) lessonDAO.insert(lessons[0]);
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
