package com.doctorplacid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.doctorplacid.activity.ITableListener;
import com.doctorplacid.R;
import com.doctorplacid.holder.GroupItemViewHolder;
import com.doctorplacid.room.groups.Group;

public class GroupsNavigationAdapter extends ListAdapter<Group, GroupItemViewHolder> {

    ITableListener listener;

    public GroupsNavigationAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.listener = (ITableListener) context;
    }

    public static final DiffUtil.ItemCallback<Group> DIFF_CALLBACK = new DiffUtil.ItemCallback<Group>() {
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
    public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_group_item, parent, false);
        return new GroupItemViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
        Group group = getItem(position);
        holder.setData(group);
    }

    public Group getItemAt(int position) {
        return getItem(position);
    }

}
