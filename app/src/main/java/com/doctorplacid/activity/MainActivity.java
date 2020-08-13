package com.doctorplacid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.doctorplacid.R;
import com.doctorplacid.TableCalendar;
import com.doctorplacid.dialog.DialogDeleteLesson;
import com.doctorplacid.room.TeachersViewModel;
import com.doctorplacid.adapter.GroupsNavigationAdapter;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.dialog.DialogAddGroup;
import com.doctorplacid.dialog.DialogAddStudent;
import com.doctorplacid.dialog.DialogDeleteGroup;
import com.doctorplacid.dialog.DialogDeleteStudent;
import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.adapter.ColumnHeadersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements ITableListener {

    private static final int BUTTON_EXPAND_ANIMATION_TIME = 400;

    private static int currentGroupId;
    public static boolean anyCellCurrentlyEdited = false;
    public static boolean addLessonsButtonShown = false;


    private static Student tempStudent;
    private static Lesson tempLesson;
    private static Group tempGroup;

    private static Group thisGroup;
    private ColumnHeadersAdapter columnHeadersAdapter;
    private TableAdapter tableAdapter;
    private TeachersViewModel teachersViewModel;
    private RecyclerScrollManager recyclerScrollManager;

    private DrawerLayout drawer;
    private FrameLayout topRightCornerExpandableLayout;
    private FrameLayout bottomRightCornerExpandableLayout;
    private CardView topRowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teachersViewModel = ViewModelProviders.of(this).get(TeachersViewModel.class);

        onInitNavigationPanel();

        FloatingActionButton fabAddStudent = findViewById(R.id.fab_add_student);
        fabAddStudent.setOnClickListener(view -> openAddStudentDialog());

        FloatingActionButton buttonAddColumn = findViewById(R.id.buttonAddLesson);
        buttonAddColumn.setOnClickListener(view -> onNewColumnAdded());

        ImageButton menu = findViewById(R.id.buttonOpenNavigationMenu);
        menu.setOnClickListener(view -> drawer.openDrawer(GravityCompat.START));
    }

    private void onInitNavigationPanel() {
        drawer = findViewById(R.id.drawer);

        topRowLayout = findViewById(R.id.topRow);

        RecyclerView groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupsNavigationAdapter groupsNavigationAdapter = new GroupsNavigationAdapter(this);
        groupsRecyclerView.setAdapter(groupsNavigationAdapter);

        teachersViewModel.getAllGroups().observe(this, list -> {
            groupsNavigationAdapter.submitList(list);
            teachersViewModel.initStudentsData(list);
            getSharedPreferenceLastUsedGroupId();
            if (currentGroupId != -666) {
                onInitTable();
            } else {
                topRowLayout.setVisibility(View.INVISIBLE);
                drawer.openDrawer(GravityCompat.START);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }
        });

        FloatingActionButton fabAddGroup = findViewById(R.id.buttonAddGroup);
        fabAddGroup.setOnClickListener(view -> openAddGroupDialog());
    }

    @Override
    public void onPreInitTable(int groupId) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        onClearTable();
        currentGroupId = groupId;
        saveSharedPreferenceLastUsedGroupId(currentGroupId);
        onInitTable();
        drawer.closeDrawer(GravityCompat.START);
    }

    public void onInitTable() {
        thisGroup = teachersViewModel.retrieveGroup(currentGroupId);

        topRightCornerExpandableLayout = findViewById(R.id.addColumnButtonFrame);
        bottomRightCornerExpandableLayout = findViewById(R.id.gradeOkButtonFrame);

        topRowLayout.setVisibility(View.VISIBLE);

        RecyclerView table = findViewById(R.id.grades);
        RecyclerView topRow = findViewById(R.id.lessons);

        table.setLayoutManager(new LinearLayoutManager(this));

        columnHeadersAdapter = new ColumnHeadersAdapter(this);
        topRow.setAdapter(columnHeadersAdapter);

        recyclerScrollManager = new RecyclerScrollManager(this, findViewById(R.id.coordinator));
        recyclerScrollManager.addRow(topRow);

        tableAdapter = new TableAdapter(this, recyclerScrollManager);
        table.setAdapter(tableAdapter);

        teachersViewModel.getAllStudents(currentGroupId).observe(this, list -> tableAdapter.submitList(list));
        teachersViewModel.getAllLessons(currentGroupId).observe(this, list -> columnHeadersAdapter.submitList(list));
    }

    private void onClearTable(){
        if(currentGroupId != -666) {
            teachersViewModel.getAllStudents(currentGroupId).removeObservers(this);
            teachersViewModel.getAllLessons(currentGroupId).removeObservers(this);
        }
        if(addLessonsButtonShown) {
            collapseAddColumnButton();
        }
    }


    @Override
    public void addStudent(String name) {
        Student student = new Student(name, currentGroupId);
        teachersViewModel.insertStudent(student,thisGroup, teachersViewModel.getAllLessons(currentGroupId).getValue());
    }

    @Override
    public void addGroup(String groupName) {
        Group group = new Group(groupName);
        teachersViewModel.insertGroup(group);
    }

    @Override
    public void deleteStudent() {
        teachersViewModel.deleteStudent(tempStudent);
        tempStudent = null;
    }

    @Override
    public void deleteGroup() {
        teachersViewModel.deleteGroup(tempGroup);
        if (tempGroup.getId() == currentGroupId) {
            saveSharedPreferenceLastUsedGroupId(-666);
        }
        tempGroup = null;
    }

    @Override
    public void deleteColumn() {
        teachersViewModel.deleteLesson(tempLesson);
        recyclerScrollManager.syncAllRows();
        int lessons = thisGroup.getLessons() - 1;
        thisGroup.setLessons(lessons);
        teachersViewModel.updateGroup(thisGroup);
        tempLesson = null;
    }


    @Override
    public void onGradeAmountEdited(CellViewHolder holder, EditText editText) {
            anyCellCurrentlyEdited = true;

            editText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, 0);

            FloatingActionButton fabOk = findViewById(R.id.fab_ok);
            expandGradeOkButton();
            fabOk.setOnClickListener(view -> {
                Grade temp = holder.updateGradeAmount();
                collapseGradeOkButton();
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                    editText.clearFocus();
                }
                if (temp != null) {
                    teachersViewModel.updateGrade(temp);
                }
                anyCellCurrentlyEdited = false;
            });
    }

    @Override
    public void onGradePresenceEdited(Grade grade, int position) {
        teachersViewModel.updateGrade(grade);
        Lesson lesson = columnHeadersAdapter.getItemAt(position);
        if (("").equals(lesson.getDate())) {
            TableCalendar calendar = new TableCalendar();
            String date = calendar.getDateTwoLines();
            Lesson newLesson = new Lesson(lesson);
            newLesson.setDate(date);
            teachersViewModel.updateLesson(newLesson);
        }
    }

    private void onNewColumnAdded() {
        try {
            teachersViewModel.insertColumn(currentGroupId);
            collapseAddColumnButton();
            int lessons = thisGroup.getLessons() + 1;
            thisGroup.setLessons(lessons);
            teachersViewModel.updateGroup(thisGroup);
        } catch (ExecutionException | InterruptedException e){
            Toast.makeText(this, "New column not inserted", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void openAddStudentDialog() {
        DialogAddStudent dialogAddStudent = new DialogAddStudent();
        dialogAddStudent.show(getSupportFragmentManager(), "Add student dialog");
    }

    public void openAddGroupDialog() {
        DialogAddGroup dialogAddGroup = new DialogAddGroup();
        dialogAddGroup.show(getSupportFragmentManager(), "Add group dialog");
    }

    @Override
    public void openDeleteStudentDialog(Student student) {
        tempStudent = student;
        DialogDeleteStudent dialogDelete = new DialogDeleteStudent(tempStudent.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete student dialog");
    }

    @Override
    public void openDeleteGroupDialog(Group group) {
        tempGroup = group;
        DialogDeleteGroup dialogDelete = new DialogDeleteGroup(tempGroup.getName());
        dialogDelete.show(getSupportFragmentManager(), "Delete group dialog");
    }

    @Override
    public void openDeleteColumnDialog(Lesson lesson) {
        tempLesson = lesson;
        DialogDeleteLesson dialogDelete = new DialogDeleteLesson();
        dialogDelete.show(getSupportFragmentManager(), "Delete column dialog");
    }

    /**
     * Methods to animate expandable buttons
     */
    public void expandAddColumnButton() {
        if (addLessonsButtonShown) return;
        int parentWidth = View.MeasureSpec.makeMeasureSpec((topRightCornerExpandableLayout.getChildAt(0)).getWidth(), View.MeasureSpec.EXACTLY);
        int parentHeight = View.MeasureSpec.makeMeasureSpec((topRightCornerExpandableLayout.getChildAt(0)).getHeight(), View.MeasureSpec.EXACTLY);
        topRightCornerExpandableLayout.measure(parentWidth, parentHeight);
        final int targetWidth = topRightCornerExpandableLayout.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        topRightCornerExpandableLayout.getLayoutParams().width = 1;
        topRightCornerExpandableLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                topRightCornerExpandableLayout.getLayoutParams().width = interpolatedTime == 1
                        ? FrameLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetWidth * interpolatedTime);
                topRightCornerExpandableLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(BUTTON_EXPAND_ANIMATION_TIME);
        topRightCornerExpandableLayout.startAnimation(a);
        addLessonsButtonShown = true;
    }

    public void expandGradeOkButton() {
        int parentWidth = View.MeasureSpec.makeMeasureSpec(((View) bottomRightCornerExpandableLayout.getChildAt(0)).getWidth(), View.MeasureSpec.EXACTLY);
        int parentHeight = View.MeasureSpec.makeMeasureSpec(((View) bottomRightCornerExpandableLayout.getChildAt(0)).getHeight(), View.MeasureSpec.EXACTLY);
        bottomRightCornerExpandableLayout.measure(parentWidth, parentHeight);
        final int targetHeight = bottomRightCornerExpandableLayout.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        bottomRightCornerExpandableLayout.getLayoutParams().height = 1;
        bottomRightCornerExpandableLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                bottomRightCornerExpandableLayout.getLayoutParams().height = interpolatedTime == 1
                        ? FrameLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                bottomRightCornerExpandableLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(BUTTON_EXPAND_ANIMATION_TIME);
        bottomRightCornerExpandableLayout.startAnimation(a);
    }

    public void collapseAddColumnButton() {
        if (!addLessonsButtonShown) return;
        final int initialWidth = topRightCornerExpandableLayout.getMeasuredWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    topRightCornerExpandableLayout.setVisibility(View.GONE);
                } else {
                    topRightCornerExpandableLayout.getLayoutParams().width = initialWidth - (int)(initialWidth * interpolatedTime);
                    topRightCornerExpandableLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(BUTTON_EXPAND_ANIMATION_TIME);
        topRightCornerExpandableLayout.startAnimation(a);
        addLessonsButtonShown = false;
    }

    public void collapseGradeOkButton() {
        final int initialHeight = bottomRightCornerExpandableLayout.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    bottomRightCornerExpandableLayout.setVisibility(View.GONE);
                } else {
                    bottomRightCornerExpandableLayout.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    bottomRightCornerExpandableLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(BUTTON_EXPAND_ANIMATION_TIME);
        bottomRightCornerExpandableLayout.startAnimation(a);
    }

    /**
     * Methods to save data in shared preferences
     */
    void getSharedPreferenceLastUsedGroupId() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        currentGroupId = sPref.getInt("LAST_USED", -666);
    }

    void saveSharedPreferenceLastUsedGroupId(int groupId) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("LAST_USED", groupId);
        ed.apply();
    }

}