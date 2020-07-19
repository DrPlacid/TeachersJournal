package com.doctorplacid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.TeachersViewModel;
import com.doctorplacid.adapter.GradesAdapter;
import com.doctorplacid.adapter.SumsAdapter;
import com.doctorplacid.dialog.DialogAddStudent;
import com.doctorplacid.dialog.DialogDeleteStudent;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.adapter.LessonsAdapter;
import com.doctorplacid.adapter.NamesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TableActivity extends AppCompatActivity implements ITableActivityListener {

    private TeachersViewModel teachersViewModel;

    private static int GROUP_ID;

    private static Group thisGroup;
    private static Student tempStudent;

    TextView tv;
    private NamesAdapter namesAdapter;
    private LessonsAdapter lessonsAdapter;
    private GradesAdapter gradesAdapter;

    LiveData<List<Student>> namesList;

    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        GROUP_ID = getIntent().getExtras().getInt("ID");

        teachersViewModel = ViewModelProviders.of(TableActivity.this).get(TeachersViewModel.class);

        tv = findViewById(R.id.textView2);

        thisGroup = teachersViewModel.retrieveGroup(GROUP_ID);
        teachersViewModel.initData(GROUP_ID);

        initStudents();
        initLessons();
        initGrades();

        fabAdd = findViewById(R.id.fab_add_table);
        fabAdd.setOnClickListener(v -> openAddDialog());
    }

    private void initStudents() {
        RecyclerView names = findViewById(R.id.names);
        RecyclerView sums = findViewById(R.id.sums);

        names.setLayoutManager(new LinearLayoutManager(TableActivity.this));
        sums.setLayoutManager(new LinearLayoutManager(TableActivity.this));

        names.setHasFixedSize(true);
        sums.setHasFixedSize(true);

        namesAdapter = new NamesAdapter(this);
        SumsAdapter sumsAdapter = new SumsAdapter();

        names.setAdapter(namesAdapter);
        sums.setAdapter(sumsAdapter);

        namesList = teachersViewModel.getAllStudents();
        namesList.observe(TableActivity.this, list -> {
            namesAdapter.setItems(list);
            sumsAdapter.setItems(list);
        });
    }

    private void initLessons() {
        int lessons = thisGroup.getLessons();
        RecyclerView dates = findViewById(R.id.lessons);

        dates.setLayoutManager(new LinearLayoutManager(TableActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        dates.setHasFixedSize(true);

        lessonsAdapter = new LessonsAdapter(this, GROUP_ID, lessons);
        dates.setAdapter(lessonsAdapter);

        final LiveData<List<Lesson>> lessonsList = teachersViewModel.getAllDates();
        lessonsList.observe(TableActivity.this, list -> lessonsAdapter.setItems(list));
    }

    private void initGrades() {
        RecyclerView grades = findViewById(R.id.grades);

        grades.setLayoutManager(new LinearLayoutManager(TableActivity.this));
        grades.setHasFixedSize(true);

        gradesAdapter = new GradesAdapter();
        grades.setAdapter(gradesAdapter);

        final LiveData<List<Grade>> namesList = teachersViewModel.getGrades(0);
        namesList.observe(TableActivity.this, list -> gradesAdapter.setItems(list));
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
        tempStudent = namesAdapter.getItemAt(position);
        DialogDeleteStudent dialogDelete = new DialogDeleteStudent(tempStudent.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete student dialog");
    }

}