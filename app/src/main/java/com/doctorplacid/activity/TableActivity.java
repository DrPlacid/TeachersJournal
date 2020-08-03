package com.doctorplacid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class TableActivity extends AppCompatActivity implements ITableActivityListener {

    private static final int ADD_LESSON_BUTTON_ANIMATION_TIME = 200;
    private int lastUsed;

    private static int GROUP_ID;
    public static boolean currentlyEdited = false;
    public static boolean addLessonsButtonShown = false;

    private static Student tempStudent;

    private ColumnHeadersAdapter columnHeadersAdapter;
    private TableAdapter tableAdapter;
    private TeachersViewModel teachersViewModel;

    private FrameLayout littleExpandableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        GROUP_ID = Objects.requireNonNull(getIntent().getExtras()).getInt("ID");


        teachersViewModel = ViewModelProviders.of(this).get(TeachersViewModel.class);
        teachersViewModel.initData(GROUP_ID);
        Group thisGroup = teachersViewModel.retrieveGroup(GROUP_ID);
        initTable();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_student);
        fabAdd.setOnClickListener(v -> {
            openAddDialog();
        });

        littleExpandableLayout = findViewById(R.id.buttonFrame);
        Button buttonAddColumn = findViewById(R.id.buttonAddLesson);
        buttonAddColumn.setOnClickListener(view -> {
            try {
                teachersViewModel.insertColumn(GROUP_ID);
                initTable();
                collapseButton();
                int lessons = thisGroup.getLessons() + 1;
                thisGroup.setLessons(lessons);
                teachersViewModel.updateGroup(thisGroup);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(TableActivity.this, "Unsuccessful, try again", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void initTable() {
        RecyclerView table = findViewById(R.id.grades);
        RecyclerView topRow = findViewById(R.id.lessons);

        table.setLayoutManager(new LinearLayoutManager(this));
        topRow.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        columnHeadersAdapter = new ColumnHeadersAdapter(this);
        topRow.setAdapter(columnHeadersAdapter);

        tableAdapter = new TableAdapter(this);
        table.setAdapter(tableAdapter);

        teachersViewModel
                .getAllStudents()
                .observe(this, list -> tableAdapter.submitList(list));

        teachersViewModel
                .getAllLessons()
                .observe(this, list -> columnHeadersAdapter.submitList(list));
    }

    @Override
    public void addStudent(String name) {
        Student student = new Student(name, GROUP_ID);
        teachersViewModel.insertStudent(student, teachersViewModel.retrieveGroup(GROUP_ID));
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
    public void onGradeAmountEdited(CellHolder holder, EditText editText) {
            currentlyEdited = true;

            editText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, 0);

            FloatingActionButton fabOk = findViewById(R.id.fab_ok);
            fabOk.setVisibility(View.VISIBLE);
            fabOk.setOnClickListener(view -> {
                Grade temp = holder.updateGradeAmount();
                fabOk.setVisibility(View.INVISIBLE);
                if (getCurrentFocus() != null)
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);

                editText.clearFocus();
                if (temp != null)
                    teachersViewModel.updateGrade(temp);

                currentlyEdited = false;
            });
    }

    @Override
    public void onGradePresenceEdited(Grade grade,  int position) {
        teachersViewModel.updateGrade(grade);
        if (lastUsed < position) lastUsed = position;
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

    @Override
    public void expandButton() {
        if (addLessonsButtonShown) return;
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) littleExpandableLayout.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        littleExpandableLayout.measure(wrapContentMeasureSpec, matchParentMeasureSpec);
        final int targetWidth = littleExpandableLayout.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        littleExpandableLayout.getLayoutParams().width = 1;
        littleExpandableLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                littleExpandableLayout.getLayoutParams().width = interpolatedTime == 1
                        ? FrameLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetWidth * interpolatedTime);
                littleExpandableLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(ADD_LESSON_BUTTON_ANIMATION_TIME);
        littleExpandableLayout.startAnimation(a);
        addLessonsButtonShown = true;
    }

    @Override
    public void collapseButton() {
        if (!addLessonsButtonShown) return;
        final int initialWidth = littleExpandableLayout.getMeasuredWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    littleExpandableLayout.setVisibility(View.GONE);
                } else {
                    littleExpandableLayout.getLayoutParams().width = initialWidth - (int)(initialWidth * interpolatedTime);
                    littleExpandableLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(ADD_LESSON_BUTTON_ANIMATION_TIME);
        littleExpandableLayout.startAnimation(a);
        addLessonsButtonShown = false;
    }
}