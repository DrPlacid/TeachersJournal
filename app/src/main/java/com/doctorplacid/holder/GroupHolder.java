package com.doctorplacid.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.doctorplacid.IMainActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.room.groups.Group;

public class GroupHolder extends RecyclerView.ViewHolder {

    private TextView textView;

    public GroupHolder(@NonNull View itemView, @NonNull IMainActivityListener listener) {
        super(itemView);
        textView = itemView.findViewById(R.id.textViewGroup);
        textView.setOnClickListener(view -> listener.startTableActivity(getAdapterPosition()));
        textView.setOnLongClickListener(view -> {
            listener.openDeleteDialog(getAdapterPosition());
            return false;
        });
    }

    public void setEntity(Group group) {
        textView.setText(group.getName());
    }
}
