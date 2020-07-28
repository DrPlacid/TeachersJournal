package com.doctorplacid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
    public static boolean edited = false;

    private static Group thisGroup;
    private static Student tempStudent;

    private ColumnHeadersAdapter columnHeadersAdapter;
    private TableAdapter tableAdapter;
    private RecyclerView.OnScrollListener scrollListener;

    private FloatingActionButton fabAdd;
    private RecyclerView topRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        GROUP_ID = getIntent().getExtras().getInt("ID");

        teachersViewModel = ViewModelProviders.of(this).get(TeachersViewModel.class);

        thisGroup = teachersViewModel.retrieveGroup(GROUP_ID);

        teachersViewModel.initData(GROUP_ID);

        onCreateTable();

        fabAdd = findViewById(R.id.fab_add_student);
        fabAdd.setOnClickListener(v -> openAddDialog());

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                tableAdapter.scrollAllItems(dx, dy);
            }
        };
        topRow.addOnScrollListener(scrollListener);
    }

    private void onCreateTable() {
        RecyclerView table = findViewById(R.id.grades);
        topRow = findViewById(R.id.lessons);

        table.setLayoutManager(new LinearLayoutManager(this));

        topRow.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        table.setHasFixedSize(true);
        topRow.setHasFixedSize(true);

        columnHeadersAdapter = new ColumnHeadersAdapter(this);
        topRow.setAdapter(columnHeadersAdapter);

        tableAdapter = new TableAdapter(this);
        table.setAdapter(tableAdapter);

        final LiveData<List<StudentWithGrades>> namesList = teachersViewModel.getAllStudents();
        namesList.observe(this, list -> tableAdapter.submitList(list));

        final LiveData<List<Lesson>> lessonsList = teachersViewModel.getAllLessons();
        lessonsList.observe(this, list -> columnHeadersAdapter.submitList(list));

        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) table.getItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
    }

    public void scroll(int dx, int dy) {
        topRow.removeOnScrollListener(scrollListener);
        topRow.scrollBy(dx, dy);
        topRow.addOnScrollListener(scrollListener);
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
    public void onGradeEdited(CellHolder holder, EditText editText) {
            edited = true;
            editText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, 0);
            FloatingActionButton fabOk = findViewById(R.id.fab_ok);
            fabOk.setVisibility(View.VISIBLE);
            fabOk.setOnClickListener(view -> {
                Grade temp = holder.updateGrade();
                teachersViewModel.updateGrade(temp);
                fabOk.setVisibility(View.INVISIBLE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                }
                editText.clearFocus();
                edited = false;
            });
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