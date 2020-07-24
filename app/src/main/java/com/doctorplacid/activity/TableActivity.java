package com.doctorplacid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.TeachersViewModel;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.dialog.DialogAddStudent;
import com.doctorplacid.dialog.DialogDeleteStudent;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.adapter.LessonsAdapter;
import com.doctorplacid.room.students.StudentWithGrades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TableActivity extends AppCompatActivity implements ITableActivityListener {

    private TeachersViewModel teachersViewModel;

    private static int GROUP_ID;

    private static Group thisGroup;
    private static Student tempStudent;

    private LessonsAdapter lessonsAdapter;
    private RowAdapter rowAdapter;

    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        GROUP_ID = getIntent().getExtras().getInt("ID");

        teachersViewModel = ViewModelProviders.of(TableActivity.this).get(TeachersViewModel.class);

        thisGroup = teachersViewModel.retrieveGroup(GROUP_ID);
        teachersViewModel.initData(GROUP_ID);

        init();

        fabAdd = findViewById(R.id.fab_add_table);
        fabAdd.setOnClickListener(v -> openAddDialog());
    }

    private void init() {
        RecyclerView grades = findViewById(R.id.grades);
        RecyclerView lessons = findViewById(R.id.lessons);

        grades.setLayoutManager(new LinearLayoutManager(TableActivity.this));

        lessons.setLayoutManager(new LinearLayoutManager(TableActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        grades.setHasFixedSize(true);
        lessons.setHasFixedSize(true);

        lessonsAdapter = new LessonsAdapter(this, GROUP_ID, thisGroup.getLessons());
        lessons.setAdapter(lessonsAdapter);

        rowAdapter = new RowAdapter(TableActivity.this);
        grades.setAdapter(rowAdapter);

        final LiveData<List<StudentWithGrades>> namesList = teachersViewModel.getAllStudents();
        final LiveData<List<Lesson>> lessonsList = teachersViewModel.getAllDates();

        namesList.observe(TableActivity.this, list -> {
            rowAdapter.submitList(list);
        });

        lessonsList.observe(TableActivity.this, list ->
                lessonsAdapter.setItems(list)
        );

        lessons.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                rowAdapter.scrollAllItems(dx, dy);
            }
        });
    }

    @Override
    public void addStudent(String name) {
        Student student = new Student(name, GROUP_ID);
        teachersViewModel.insertStudent(student, thisGroup.getLessons());
    }

    @Override
    public void deleteStudent() {
        teachersViewModel.deleteStudent(tempStudent);
        tempStudent = null;
    }

    @Override
    public void addDate(String name) {
        Lesson lesson = new Lesson(name, GROUP_ID);
        teachersViewModel.insertDate(lesson);
    }

    @Override
    public void deleteDate(int position) {
        Lesson lesson = lessonsAdapter.getItemAt(position);
        teachersViewModel.deleteDate(lesson);
    }

    @Override
    public void openAddDialog() {
        DialogAddStudent dialogAddStudent = new DialogAddStudent();
        dialogAddStudent.show(getSupportFragmentManager(), "Add student dialog");
    }

    @Override
    public void openDeleteDialog(int position) {
        tempStudent = rowAdapter.getItemAt(position).getStudent();
        DialogDeleteStudent dialogDelete = new DialogDeleteStudent(tempStudent.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete student dialog");
    }

}