package com.doctorplacid.holder;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.activity.MainActivity;
import com.doctorplacid.room.grades.Grade;
import com.doctorplacid.R;

public class CellViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private EditText editText;

    private Grade grade;

    public CellViewHolder(@NonNull View itemView, ITableListener listener) {
        super(itemView);

        textView = itemView.findViewById(R.id.gradeTextView);
        editText = itemView.findViewById(R.id.gradeEditText);

        textView.setOnClickListener(view -> {
            if (!MainActivity.anyCellCurrentlyEdited) {
                if (!grade.isPresent()) {
                    Grade newGrade = updateGradePresence();
                    listener.onGradePresenceEdited(newGrade, getAdapterPosition());
                } else {
                    int amount = grade.getAmount();
                    String text = (amount == 0) ? "" : String.valueOf(amount);
                    switchToEdit(text);
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
        switchToDisplay();
        if (newAmount.length() > 0) {
            int amount = Integer.parseInt(newAmount);
            Grade newGrade = new Grade(grade);
            newGrade.setAmount(amount);
            return newGrade;
        }
        return grade;
    }

    private void switchToEdit(String text) {
        textView.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.VISIBLE);
        editText.setText(text);
        editText.setSelection(text.length());
    }

    private void switchToDisplay() {
        textView.setVisibility(View.VISIBLE);
        editText.setVisibility(View.INVISIBLE);
    }

}
