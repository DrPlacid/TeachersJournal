package com.doctorplacid.activity;

import androidx.annotation.NonNull;
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
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.adapter.LessonsAdapter;
import com.doctorplacid.adapter.NamesAdapter;
import com.doctorplacid.room.students.StudentWithGrades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TableActivity extends AppCompatActivity implements ITableActivityListener {

    private TeachersViewModel teachersViewModel;
    boolean canScroll = false;

    private static int GROUP_ID;

    private static Group thisGroup;
    private static Student tempStudent;

    private NamesAdapter namesAdapter;
    private LessonsAdapter lessonsAdapter;
    private GradesAdapter gradesAdapter;

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
        RecyclerView names = findViewById(R.id.names);
        RecyclerView sums = findViewById(R.id.sums);
        RecyclerView grades = findViewById(R.id.grades);
        RecyclerView lessons = findViewById(R.id.lessons);

        names.setLayoutManager(new LinearLayoutManager(TableActivity.this));
        sums.setLayoutManager(new LinearLayoutManager(TableActivity.this));
        grades.setLayoutManager(new LinearLayoutManager(TableActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return canScroll;
            }
        });
        lessons.setLayoutManager(new LinearLayoutManager(TableActivity.this,
                LinearLayoutManager.HORIZONTAL, false));

        grades.setHasFixedSize(true);
        names.setHasFixedSize(true);
        sums.setHasFixedSize(true);
        lessons.setHasFixedSize(true);

        namesAdapter = new NamesAdapter(this);
        SumsAdapter sumsAdapter = new SumsAdapter();
        lessonsAdapter = new LessonsAdapter(this, GROUP_ID, thisGroup.getLessons());

        names.setAdapter(namesAdapter);
        sums.setAdapter(sumsAdapter);
        lessons.setAdapter(lessonsAdapter);

        gradesAdapter = new GradesAdapter(TableActivity.this);
        grades.setAdapter(gradesAdapter);

        final LiveData<List<StudentWithGrades>> namesList = teachersViewModel.getAllStudents();
        final LiveData<List<Lesson>> lessonsList = teachersViewModel.getAllDates();

        namesList.observe(TableActivity.this, list -> {
            namesAdapter.submitList(list);
            sumsAdapter.submitList(list);
            gradesAdapter.submitList(list);
        });

        lessonsList.observe(TableActivity.this, list ->
                lessonsAdapter.setItems(list)
        );

        names.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                canScroll = true;
                grades.scrollBy(dx, dy);
                canScroll = false;

            }
        });

        lessons.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                gradesAdapter.scrollAllItems(dx, dy);
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
        tempStudent = namesAdapter.getItemAt(position);
        DialogDeleteStudent dialogDelete = new DialogDeleteStudent(tempStudent.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete student dialog");
    }

}