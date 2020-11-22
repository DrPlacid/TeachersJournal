package com.doctorplacid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doctorplacid.R;
import com.doctorplacid.TeachersViewModel;
import com.doctorplacid.adapter.GroupsNavigationAdapter;
import com.doctorplacid.adapter.TableAdapter;
import com.doctorplacid.holder.CellViewHolder;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.room.lessons.Lesson;
import com.doctorplacid.room.students.Student;
import com.doctorplacid.adapter.ColumnHeadersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ITableListener {

    public static final float LETTER_SPACING_NARROW = .05f;

    private static int currentGroupId;

    public static boolean anyCellCurrentlyEdited = false;
    public static boolean newColumnAdded = false;
    public static boolean newStudentAdded = false;

    private ColumnHeadersAdapter columnHeadersAdapter;
    private TableAdapter tableAdapter;
    private TeachersViewModel teachersViewModel;
    private RowSyncManager rowSyncManager;

    private DrawerLayout drawer;
    private CoordinatorLayout coordinator;
    private FrameLayout topRightCornerExpandableLayout;
    private FrameLayout bottomRightCornerExpandableLayout;
    private FrameLayout bottomLeftCornerExpandableLayout;
    private FrameLayout navigationPanelExpandableLayout;
    private RelativeLayout topRowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teachersViewModel = ViewModelProviders.of(MainActivity.this).get(TeachersViewModel.class);

        onInitViews();
        onInitNavigationPanel();

        currentGroupId = getPreferences(MODE_PRIVATE).getInt("LAST_USED", -404);
        if (currentGroupId != -404) {
            onInitTable();
        } else {
            openAndFixNavigationPanel();
        }

        onInitClickListeners();
    }

    private void onInitViews() {
        drawer = findViewById(R.id.drawer);
        coordinator = findViewById(R.id.coordinator);
        topRowLayout = findViewById(R.id.topRow);
        topRightCornerExpandableLayout = findViewById(R.id.addColumnButtonFrame);
        bottomRightCornerExpandableLayout = findViewById(R.id.gradeOkButtonFrame);
        bottomLeftCornerExpandableLayout = findViewById(R.id.addStudentButtonFrame);
        navigationPanelExpandableLayout = findViewById(R.id.navigationPanelExpandableLayout);
    }

    private void onInitClickListeners() {
        FloatingActionButton fabAddStudent = findViewById(R.id.fab_add_student);
        fabAddStudent.setOnClickListener(view -> DialogManager.openAddStudentDialog(this));

        FloatingActionButton buttonAddColumn = findViewById(R.id.buttonAddLesson);
        buttonAddColumn.setOnClickListener(view -> addColumn());

        ImageButton menu = findViewById(R.id.buttonOpenNavigationMenu);
        menu.setOnClickListener(view -> drawer.openDrawer(GravityCompat.START));

        FloatingActionButton fabAddGroup = findViewById(R.id.buttonAddGroup);
        fabAddGroup.setOnClickListener(view -> DialogManager.openAddGroupDialog(this));

        ImageButton infoHints = findViewById(R.id.buttonInfo);
        infoHints.setOnClickListener(view -> AnimationManager.verticalExpand(navigationPanelExpandableLayout));

        ImageButton closeHints = findViewById(R.id.buttonCloseHints);
        closeHints.setOnClickListener(view -> AnimationManager.verticalCollapse(navigationPanelExpandableLayout));

        ConstraintLayout navigationPanel = findViewById(R.id.navigationPanelLayout);
        navigationPanel.setOnClickListener(view -> AnimationManager.verticalCollapse(navigationPanelExpandableLayout));
    }

    private void onInitNavigationPanel() {
        TextView navigationHeaderText = findViewById(R.id.navigation_header_text);
        navigationHeaderText.setLetterSpacing(LETTER_SPACING_NARROW);

        RecyclerView groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupsNavigationAdapter groupsNavigationAdapter = new GroupsNavigationAdapter(this);
        groupsRecyclerView.setAdapter(groupsNavigationAdapter);

        teachersViewModel.getAllGroups().observe(this, list -> groupsNavigationAdapter.submitList(list));
    }

    @Override
    public void openTableFromNavigationPanel(int groupId) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (currentGroupId != groupId) {
            onClearTable();
            currentGroupId = groupId;
            onInitTable();
        }
        new Handler().postDelayed(() -> drawer.closeDrawer(GravityCompat.START, true), 100);
    }

    public void onInitTable() {
        teachersViewModel.initDataSet(currentGroupId);
        String name = teachersViewModel.getCurrentGroupName();

        LayoutTransition layoutTransition = ((ViewGroup) findViewById(R.id.linear)).getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        topRowLayout.setVisibility(View.VISIBLE);
        RecyclerView table = findViewById(R.id.grades);
        RecyclerView topRow = findViewById(R.id.lessons);
        table.setHasFixedSize(true);

        table.setLayoutManager(new LinearLayoutManager(this));

        columnHeadersAdapter = new ColumnHeadersAdapter(this);
        topRow.setAdapter(columnHeadersAdapter);

        rowSyncManager = new RowSyncManager(MainActivity.this);
        rowSyncManager.addRow(topRow);

        tableAdapter = new TableAdapter(this, rowSyncManager);
        table.setAdapter(tableAdapter);

        TextView groupNameText = findViewById(R.id.textViewGroupName);
        groupNameText.setText(name);
        groupNameText.setLetterSpacing(LETTER_SPACING_NARROW);

        teachersViewModel.getAllLessons().observe(MainActivity.this, list -> columnHeadersAdapter.submitList(list));
        teachersViewModel.getAllStudents().observe(MainActivity.this, list -> {
            tableAdapter.submitList(list);
            if (newColumnAdded) {
                rowSyncManager.scrollAllByOneCell();
                newColumnAdded = false;
            }
            if (newStudentAdded) {
                rowSyncManager.syncAllRowsByTop();
                newStudentAdded = false;
            }
        });

        table.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    AnimationManager.verticalExpand(bottomLeftCornerExpandableLayout);
                }
                if (dy > 0) {
                    AnimationManager.verticalCollapse(bottomLeftCornerExpandableLayout);
                }
            }
        });

        coordinator.addOnLayoutChangeListener(
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                AnimationManager.verticalCollapse(bottomLeftCornerExpandableLayout);
            }
            if (bottom > oldBottom) {
                AnimationManager.verticalExpand(bottomLeftCornerExpandableLayout);
            }
            rowSyncManager.syncAllRowsByTop();
        });

        saveSharedPreferenceLastUsedGroupId(currentGroupId);
    }

    private void onClearTable() {
        if (currentGroupId != -404) {
            teachersViewModel.getAllStudents().removeObservers(MainActivity.this);
            teachersViewModel.getAllLessons().removeObservers(MainActivity.this);
        }
        if (AnimationManager.addColumnButtonShown) {
            collapseAddColumnFAB();
        }
    }

    private void saveSharedPreferenceLastUsedGroupId(int groupId) {
        getPreferences(MODE_PRIVATE).edit()
                .putInt("LAST_USED", groupId)
                .apply();
    }

    /**
     * Methods to edit table
     */
    @Override
    public void addStudent(String name) {
        teachersViewModel.insertStudent(name);
        newStudentAdded = true;
    }

    @Override
    public void addGroup(String groupName) {
        teachersViewModel.insertGroup(groupName);
    }

    private void addColumn() {
        newColumnAdded = teachersViewModel.insertColumn();
    }

    @Override
    public void deleteStudent(Student student) {
        teachersViewModel.deleteStudent(student);
    }

    @Override
    public void deleteGroup(Group group) {
        teachersViewModel.deleteGroup(group);
        if (group.getId() == currentGroupId) {
            saveSharedPreferenceLastUsedGroupId(-404);
            topRowLayout.setVisibility(View.INVISIBLE);
            openAndFixNavigationPanel();
        }
    }

    @Override
    public void deleteColumn(Lesson lesson) {
        teachersViewModel.deleteColumn(lesson);
    }

    @Override
    public void gradeAmountEdited(CellViewHolder holder, EditText editText) {
        anyCellCurrentlyEdited = true;

        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
        AnimationManager.verticalExpand(bottomRightCornerExpandableLayout);

        FloatingActionButton fabOk = findViewById(R.id.fab_ok);
        fabOk.setOnClickListener(view -> {
            Grade temp = holder.updateGradeAmount();
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                editText.clearFocus();
            }
            if (temp != null) {
                teachersViewModel.updateGrade(temp);
            }
            new Handler().postDelayed(() -> AnimationManager.verticalCollapse(bottomRightCornerExpandableLayout), 200);
            anyCellCurrentlyEdited = false;
        });
    }

    @Override
    public void gradePresenceEdited(Grade grade, int position) {
        anyCellCurrentlyEdited = true;
        teachersViewModel.updateGrade(grade);
        Lesson lesson = columnHeadersAdapter.getItemAt(position);
        if (("").equals(lesson.getDay()) && ("").equals(lesson.getMonth())) {
            teachersViewModel.updateLesson(lesson);
        }
        anyCellCurrentlyEdited = false;
    }

    @Override
    public void clearCell(Grade grade) {
        anyCellCurrentlyEdited = true;
        teachersViewModel.clearCell(grade);
        anyCellCurrentlyEdited = false;
    }

    /**
     * UI - specific methods
     */
    private void openAndFixNavigationPanel() {
        topRowLayout.setVisibility(View.INVISIBLE);
        drawer.openDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    public void expandAddColumnFAB() {
        AnimationManager.horizontalExpand(topRightCornerExpandableLayout);
    }

    public void collapseAddColumnFAB() {
        AnimationManager.horizontalCollapse(topRightCornerExpandableLayout);
    }

}