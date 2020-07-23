package com.doctorplacid.holder;

import com.doctorplacid.R;
import com.doctorplacid.adapter.RowAdapter;
import com.doctorplacid.room.grades.Grade;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RowGradesHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;
    private boolean isScrollEnabled;

    public RowGradesHolder(@NonNull View itemView, Context context) {
        super(itemView);
        isScrollEnabled = false;
        recyclerView = itemView.findViewById(R.id.RecyclerViewRow);

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false ){
            @Override
            public boolean canScrollHorizontally() {
                return isScrollEnabled;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    public void setList(List<Grade> grades) {
        RowAdapter adapter = new RowAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItems(grades);
    }

    public void scroll(int dx, int dy) {
        this.isScrollEnabled = true;
        recyclerView.scrollBy(dx, dy);
        Log.i("SCROLLTAG", "scrolled");
        this.isScrollEnabled = false;
    }

}
