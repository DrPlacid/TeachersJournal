package com.doctorplacid.room.groups;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.doctorplacid.room.TeachersDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GroupsRepository {

    private GroupDAO groupDAO;
    private LiveData<List<Group>> groupsLiveData;

    public GroupsRepository(Application application) {
        TeachersDatabase database = TeachersDatabase.getInstance(application);
        groupDAO = database.groupDAO();
        groupsLiveData = groupDAO.retrieveAll();
    }

    public Group retrieve(int id) {
        try {
            return new GroupRetrieveAsyncTask(groupDAO).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Group group) {
        new GroupInsertAsyncTask(groupDAO).execute(group);
    }

    public void update(Group group) {
        new GroupUpdateAsyncTask(groupDAO).execute(group);
    }

    public void delete(Group group) {
        new GroupDeleteAsyncTask(groupDAO).execute(group);
    }

    public LiveData<List<Group>> retrieveAll() {
        return groupsLiveData;
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

        @Override
        protected void onPostExecute(Group group) {
            super.onPostExecute(group);
        }
    }

    private static class GroupInsertAsyncTask extends AsyncTask<Group, Void, Void> {
        private GroupDAO groupDAO;

        public GroupInsertAsyncTask(GroupDAO groupDAO) {
            this.groupDAO = groupDAO;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            groupDAO.insert(groups[0]);
            return null;
        }
    }

    private static class GroupUpdateAsyncTask extends AsyncTask<Group, Void, Void> {
        private GroupDAO groupDAO;

        public GroupUpdateAsyncTask(GroupDAO groupDAO) {
            this.groupDAO = groupDAO;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            groupDAO.update(groups[0]);
            return null;
        }
    }

    private static class GroupDeleteAsyncTask extends AsyncTask<Group, Void, Void> {
        private GroupDAO groupDAO;

        public GroupDeleteAsyncTask(GroupDAO groupDAO) {
            this.groupDAO = groupDAO;
        }

        @Override
        protected Void doInBackground(Group... groups) {
            groupDAO.delete(groups[0]);
            return null;
        }
    }
}
