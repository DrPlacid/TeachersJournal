package com.doctorplacid.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableListener;
import com.doctorplacid.MainActivity;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.R;

public class CellViewHolder extends RecyclerView.ViewHolder {

    private static final int DIRECTION_LEFT = -1;
    private static final int DIRECTION_RIGHT = 1;

    private TextView textView;
    private EditText editText;
    private HorizontalScrollView scrollView;
    private LinearLayout linearLayout;

    private Grade grade;

    public CellViewHolder(@NonNull View itemView, ITableListener listener) {
        super(itemView);

        textView = itemView.findViewById(R.id.gradeTextView);
        editText = itemView.findViewById(R.id.gradeEditText);
        scrollView = itemView.findViewById(R.id.scrollCell);
        linearLayout = itemView.findViewById(R.id.linear);

        textView.setOnClickListener(view -> {
            if (!MainActivity.currentlyEdited) {
                if (!grade.isPresent()) {
                    Grade newGrade = updateGradePresence();
                    listener.onGradePresenceEdited(newGrade, getAdapterPosition());
                } else {
                    String text = String.valueOf(grade.getAmount());
                    switchToEdit(CellViewHolder.DIRECTION_RIGHT, text);
                    listener.onGradeAmountEdited(this, editText);
                }
            }
        });
    }

    public void setEntity(Grade grade) {
        this.grade = grade;
        String text = grade.isPresent() ? String.valueOf(grade.getAmount()) : "";
        textView.setText(text);
    }


    public Grade updateGradePresence() {
        Grade newGrade = new Grade(grade);
        newGrade.setPresent(true);
        return newGrade;
    }

    public Grade updateGradeAmount() {
        String newAmount = editText.getText().toString().trim();
        if (newAmount.length() > 0) {
            int amount = Integer.parseInt(newAmount);
            Grade newGrade = new Grade(grade);
            newGrade.setAmount(amount);
            switchToEdit(CellViewHolder.DIRECTION_LEFT, "");
            return newGrade;
        }
        return grade;
    }

    private void switchToEdit(int direction, String text) {
        editText.setText(text);
        int vLeft = linearLayout.getLeft();
        int vRight = linearLayout.getRight();
        int sWidth = scrollView.getWidth();
        scrollView.scrollTo(direction*(vLeft + vRight - sWidth), 0);

        editText.setSelection(text.length());
    }

}
