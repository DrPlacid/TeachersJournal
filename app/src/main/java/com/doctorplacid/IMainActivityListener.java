package com.doctorplacid;

public interface IMainActivityListener {

    void openAddDialog();
    void openDeleteDialog(int position);

    void startTableActivity(int position);
    void add(String name);
    void delete();

}
