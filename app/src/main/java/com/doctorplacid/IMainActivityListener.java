package com.doctorplacid;

public interface IMainActivityListener {

    void openAddDialog(DataType dataType);
    void openDeleteDialog(int position);

    void startTableActivity(int position);
    void add(String name);
    void delete();

}
