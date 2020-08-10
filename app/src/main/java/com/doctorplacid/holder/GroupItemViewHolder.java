package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.ITableListener;
import com.doctorplacid.room.groups.Group;
import com.doctorplacid.R;

public class GroupItemViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private Group group;

    public GroupItemViewHolder(@NonNull View itemView, ITableListener listener) {
        super(itemView);
        textView = itemView.findViewById(R.id.groupTextView);
        textView.setOnClickListener(view -> listener.onPreInitTable(group.getId()));
    }

    public void setData(Group group) {
        this.group = group;
        textView.setText(group.getName());
    }

}
