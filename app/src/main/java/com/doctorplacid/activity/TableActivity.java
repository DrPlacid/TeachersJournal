package com.doctorplacid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.TeachersViewModel;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.dialog.DialogAddStudent;
import com.doctorplacid.dialog.DialogDeleteStudent;
import com.doctorplacid.holder.CellHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.adapter.ColumnHeadersAdapter;
import com.doctorplacid.room.students.StudentWithGrades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TableActivity extends AppCompatActivity implements ITableActivityListener {

    private TeachersViewModel teachersViewModel;

    private static int GROUP_ID;
    private boolean gradeEdited = false;

    private static Group thisGroup;
    private static Student tempStudent;

    private ColumnHeadersAdapter columnHeadersAdapter;
    private TableAdapter tableAdapter;
    private RecyclerView.OnScrollListener scrollListener;

    private FloatingActionButton fabAdd;
    private FloatingActionButton fabOk;
    private RecyclerView lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        GROUP_ID = getIntent().getExtras().getInt("ID");

        teachersViewModel = ViewModelProviders.of(this).get(TeachersViewModel.class);

        thisGroup = teachersViewModel.retrieveGroup(GROUP_ID);

        teachersViewModel.initData(GROUP_ID);

        init();

        fabAdd = findViewById(R.id.fab_add_student);
        fabAdd.setOnClickListener(v -> openAddDialog());

        fabOk = findViewById(R.id.fab_ok);
    }

    private void init() {
        RecyclerView grades = findViewById(R.id.grades);
        lessons = findViewById(R.id.lessons);

        grades.setLayoutManager(new LinearLayoutManager(this));

        lessons.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        grades.setHasFixedSize(true);
        lessons.setHasFixedSize(true);

        columnHeadersAdapter = new ColumnHeadersAdapter(this);
        lessons.setAdapter(columnHeadersAdapter);

        tableAdapter = new TableAdapter(this, lessons);
        grades.setAdapter(tableAdapter);

        final LiveData<List<StudentWithGrades>> namesList = teachersViewModel.getAllStudents();
        namesList.observe(this, list -> tableAdapter.submitList(list));

        final LiveData<List<Lesson>> lessonsList = teachersViewModel.getAllLessons();
        lessonsList.observe(this, list -> columnHeadersAdapter.submitList(list));

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                tableAdapter.scrollAllItems(dx, dy);
            }
        };

        lessons.addOnScrollListener(scrollListener);
    }

    public void scroll(int dx, int dy) {
        lessons.removeOnScrollListener(scrollListener);
        lessons.scrollBy(dx, dy);
        lessons.addOnScrollListener(scrollListener);
    }

    @Override
    public void addStudent(String name) {
        Student student = new Student(name, GROUP_ID);
        teachersViewModel.insertStudent(student, thisGroup);
    }

    @Override
    public void deleteStudent() {
        teachersViewModel.deleteStudent(tempStudent);
        tempStudent = null;
    }

    @Override
    public void addDate(Lesson lesson) {
        teachersViewModel.updateLesson(lesson);
    }

    @Override
    public void deleteDate(int position) {
        Lesson lesson = columnHeadersAdapter.getItemAt(position);
        teachersViewModel.deleteDate(lesson);
    }

    @Override
    public void onGradeEdited(CellHolder holder) {
        if(!gradeEdited) {
            gradeEdited = true;
            holder.showEditText();
            fabOk.setVisibility(View.VISIBLE);
            fabOk.setOnClickListener(view -> {
                Grade grade = holder.showTextView();
                fabOk.setVisibility(View.INVISIBLE);
                gradeEdited = false;
                teachersViewModel.updateGrade(grade);
            });
        }
    }


    @Override
    public void openAddDialog() {
        DialogAddStudent dialogAddStudent = new DialogAddStudent();
        dialogAddStudent.show(getSupportFragmentManager(), "Add student dialog");
    }

    @Override
    public void openDeleteDialog(int position) {
        tempStudent = tableAdapter.getItemAt(position).getStudent();
        DialogDeleteStudent dialogDelete = new DialogDeleteStudent(tempStudent.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete student dialog");
    }



}