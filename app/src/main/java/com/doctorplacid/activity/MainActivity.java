package com.doctorplacid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctorplacid.IMainActivityListener;
import com.doctorplacid.TeachersViewModel;
import com.doctorplacid.R;
import com.doctorplacid.adapter.GroupsAdapter;
import com.doctorplacid.dialog.DialogAddGroup;
import com.doctorplacid.dialog.DialogDeleteGroup;
import com.doctorplacid.room.groups.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainActivityListener {

    private TeachersViewModel teachersViewModel;
    private View coordinator;
    private GroupsAdapter adapter;

    private static Group tempGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinator = findViewById(R.id.coordinator);

        RecyclerView recyclerView = findViewById(R.id.groupsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new GroupsAdapter(this);
        recyclerView.setAdapter(adapter);

        teachersViewModel = ViewModelProviders.of(this).get(TeachersViewModel.class);
        final LiveData<List<Group>> list = teachersViewModel.getAllGroups();

        list.observe(this, groups -> adapter.submitList(groups));

        FloatingActionButton fabAddGroup = findViewById(R.id.fab_add_main);
        fabAddGroup.setOnClickListener(view -> openAddDialog());
    }

    @Override
    public void openAddDialog() {
        DialogAddGroup dialogAddGroup = new DialogAddGroup();
        dialogAddGroup.show(getSupportFragmentManager(), "Add group dialog");
    }

    @Override
    public void openDeleteDialog(int position) {
        tempGroup = adapter.getItemAt(position);
        DialogDeleteGroup dialogDelete = new DialogDeleteGroup(tempGroup.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete group dilog");
    }

    @Override
    public void startTableActivity(int position) {
        int id = teachersViewModel.getAllGroups()
                .getValue()
                .get(position)
                .getId();

        Intent intent = new Intent(this, TableActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    @Override
    public void add(String groupName) {
        Group group = new Group(groupName);
        teachersViewModel.insertGroup(group);
        Snackbar.make(coordinator,"New group " + groupName + " created", Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void delete() {
        teachersViewModel.deleteGroup(tempGroup);
        Snackbar.make(coordinator,"Group " + tempGroup.getName() + " deleted", Snackbar.LENGTH_SHORT)
                .show();
        tempGroup = null;
    }
}