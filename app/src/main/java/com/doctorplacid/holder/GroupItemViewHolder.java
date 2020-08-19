package com.doctorplacid.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.activity.DialogManager;
import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.activity.MainActivity;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.R;

public class GroupItemViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private Group group;

    public GroupItemViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        textView = itemView.findViewById(R.id.groupTextView);
        textView.setOnClickListener(view -> ((ITableListener) context).openTable(group.getId()));
        textView.setOnLongClickListener(view -> {
            DialogManager.openDeleteGroupDialog(group, context);
            return false;
        });
    }

    public void setData(Group group) {
        this.group = group;
        textView.setText(group.getName());
        textView.setLetterSpacing(MainActivity.LETTER_SPACING_NARROW);
    }

    public Group getGroup() {
        return this.group;
    }

}
