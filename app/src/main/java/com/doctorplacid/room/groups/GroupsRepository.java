package com.doctorplacid.room.groups;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.lessons.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GroupsRepository {

    private static GroupDAO groupDAO;
    private Handler handler;

    public GroupsRepository(Application application, Handler handler) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        groupDAO = database.groupDAO();
        this.handler = handler;
    }

    public Group retrieve(int id) {
        try {
            return new GroupRetrieveAsyncTask(groupDAO).execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Group group) {
        handler.post(new InsertRunnable(group));
    }

    public void update(Group group) {
        handler.post(new UpdateRunnable(group));
    }

    public void delete(Group group) {
        handler.post(new DeleteRunnable(group));
    }

    public LiveData<List<Group>> retrieveAll() {
        return groupDAO.retrieveAll();
    }


    private static class GroupRetrieveAsyncTask extends AsyncTask<Integer, Void, Group> {
        private GroupDAO groupDAO;

        public GroupRetrieveAsyncTask(GroupDAO groupDAO) {
            this.groupDAO = groupDAO;
        }

        @Override
        protected Group doInBackground(Integer... ids) {
            return groupDAO.retrieve(ids[0]);
        }
    }

    private static class InsertRunnable implements Runnable {
        Group group;

        public InsertRunnable(Group group) {
            this.group = group;
        }

        @Override
        public void run() {
            int id = (int) groupDAO.insert(group);
            List<Lesson> lessons = new ArrayList<>();

            for (int i = 0; i < group.getLessons(); i++) {
                Lesson lesson = new Lesson(id);
                lessons.add(lesson);
            }

            groupDAO.insertLessons(lessons);
        }
    }

    private static class UpdateRunnable implements Runnable {
        Group group;

        public UpdateRunnable(Group group) {
            this.group = group;
        }

        @Override
        public void run() {
            groupDAO.update(group);
        }
    }

    private static class DeleteRunnable implements Runnable {
        Group group;

        public DeleteRunnable(Group group) {
            this.group = group;
        }

        @Override
        public void run() {
            groupDAO.delete(group);
        }
    }

}
