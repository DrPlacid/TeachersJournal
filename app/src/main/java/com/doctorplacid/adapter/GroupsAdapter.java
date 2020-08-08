package com.doctorplacid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.IMainActivityListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.GroupHolder;
import com.doctorplacid.room.groups.Group;


public class GroupsAdapter extends ListAdapter<Group, GroupHolder> {

    private IMainActivityListener listener;

    public GroupsAdapter(IMainActivityListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Group> DIFF_CALLBACK = new DiffUtil.ItemCallback<Group>() {
        @Override
        public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };


    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_group, parent, false);
        return new GroupHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Group tempGroup = getItem(position);
        holder.setEntity(tempGroup);
    }


    public Group getItemAt(int position) {
        return getItem(position);
    }
}
