package com.doctorplacid.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableActivityListener;
import com.doctorplacid.activity.TableActivity;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.R;

public class CellHolder extends RecyclerView.ViewHolder {

    private static final int DIRECTION_LEFT = -1;
    private static final int DIRECTION_RIGHT = 1;

    private TextView textView;
    private EditText editText;
    private HorizontalScrollView scrollView;
    private LinearLayout linearLayout;

    private Grade grade;

    public CellHolder(@NonNull View itemView, ITableActivityListener listener) {
        super(itemView);
        textView = itemView.findViewById(R.id.gradeTextView);
        editText = itemView.findViewById(R.id.gradeEditText);
        scrollView = itemView.findViewById(R.id.scrollCell);
        linearLayout = itemView.findViewById(R.id.linear);

        textView.setOnClickListener(view -> {
            if (!TableActivity.currentlyEdited) {
                String text = String.valueOf(grade.getAmount());
                switchView(CellHolder.DIRECTION_RIGHT, text);
                listener.onGradeEdited(this, editText);
            }
        });
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
        textView.setText(String.valueOf(grade.getAmount()));
    }


    public Grade updateGrade() {
        String newAmount = editText.getText().toString().trim();
        if (newAmount.length() > 0) {
            int amount = Integer.parseInt(newAmount);
            Grade newGrade = new Grade(grade, amount);
            switchView(CellHolder.DIRECTION_LEFT, "");
            return newGrade;
        }
        return grade;
    }

    private void switchView(int direction, String text) {
        editText.setText(text);
        int vLeft = linearLayout.getLeft();
        int vRight = linearLayout.getRight();
        int sWidth = scrollView.getWidth();
        scrollView.scrollTo(direction*(vLeft + vRight - sWidth), 0);

        editText.setSelection(text.length());
    }

}
