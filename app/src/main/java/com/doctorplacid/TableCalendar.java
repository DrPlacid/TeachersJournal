package com.doctorplacid;

public class TableCalendar {

    private String dd;
    private String mm;

    public TableCalendar() {
        CharSequence currentDate = android.text.format.DateFormat.format("dd-MM", new java.util.Date());
        dd = currentDate.toString().substring(0,2);
        mm = currentDate.toString().substring(3);
    }

    public String getDateTwoLines() {
        return dd + "\n" + mm;
    }
}
