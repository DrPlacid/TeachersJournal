package com.doctorplacid;

public interface ITableActivityListener {

    void openAddDialog();
    void openDeleteDialog(int position);

    void addStudent(String name);
    void deleteStudent();

    void addDate(String date);
    void deleteDate(int position);

}
