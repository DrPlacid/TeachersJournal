package com.doctorplacid.holder;

import android.content.Context;
import android.util.Log;
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
    private ITableListener listener;

    private int colorNotEdited;
    private int colorEdited;

    public CellViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        listener = (ITableListener) context;

        textView = itemView.findViewById(R.id.gradeTextView);
        editText = itemView.findViewById(R.id.gradeEditText);

        textView.setOnLongClickListener(view -> {
            if (grade != null) {
                ((ITableListener) context).onClearCell(grade);
            }
            return false;
        });

        textView.setOnClickListener(view -> {
            if (!MainActivity.anyCellCurrentlyEdited) {
                if (!grade.isPresence()) {
                    Grade newGrade = updateGradePresence();
                    Log.i("GRADEPRESENCE", listener.toString());
                    listener.onGradePresenceEdited(newGrade, getAdapterPosition());
                } else {
                    int amount = grade.getAmount();
                    String text = (amount == 0) ? "" : String.valueOf(amount);
                    switchToEdit(text);
                    ((ITableListener) context).onGradeAmountEdited(this, editText);
                }
            }
        });

        colorNotEdited = itemView.getResources().getColor(R.color.colorGray);
        colorEdited = itemView.getResources().getColor(R.color.colorDarkGray);
    }

    public void setData(Grade grade) {
        this.grade = grade;
        String text = grade.isPresence() ? String.valueOf(grade.getAmount()) : "";
        int textColor = (grade.getAmount() == 0) ? colorNotEdited : colorEdited;
        textView.setTextColor(textColor);
        textView.setText(text);
    }


    public Grade updateGradePresence() {
        Grade newGrade = new Grade(grade);
        newGrade.setPresence(true);
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
