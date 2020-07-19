package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doctorplacid.IMainActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.GroupHolder;
import com.doctorplacid.room.groups.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupHolder> {

    private IMainActivityListener listener;
    private List<Group> groups = new ArrayList<>();

    public GroupsAdapter(IMainActivityListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_group, parent, false);
        return new GroupHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Group tempGroup = groups.get(position);
        holder.setText(tempGroup);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setItems(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    public Group getItemAt(int position) {
        return groups.get(position);
    }
}
