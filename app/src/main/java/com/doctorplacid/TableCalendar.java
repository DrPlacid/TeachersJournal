package com.doctorplacid;

public class TableCalendar {

    private String dd;
    private String mm;

    public TableCalendar() {
        CharSequence currentTime = android.text.format.DateFormat.format("dd-MM", new java.util.Date());
            dd = currentTime.toString().substring(0,2);
            mm = currentTime.toString().substring(3);
    }

    public String getDateTwoLines() {
        return dd + "\n" + mm;
    }

    public String getDateOneLine() {
        return dd + " : " + mm;
    }

}
