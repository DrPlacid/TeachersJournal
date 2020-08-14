package com.doctorplacid.activity;

import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class RowsScrollManager {

    private Context context;

    private int mTouchedRvPosition;
    private int currentListSize;

    private List<RecyclerView> recyclerViews = new ArrayList<>();

    public RowsScrollManager(Context context) {
        this.context = context;
    }

    public void addRow(final RecyclerView recyclerView) {
        LinearLayoutManager lm =
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
                        int scrollRange = super.scrollHorizontallyBy(dx, recycler, state);
                        int overScroll = dx - scrollRange;
                        if (overScroll > 0) {
                            ((MainActivity) context).horizontalExpandFAB();
                        }
                        return scrollRange;
                    }
                };
        
        recyclerView.setLayoutManager(lm);
        recyclerView.addOnScrollListener(syncScrollListener);
        recyclerView.addOnItemTouchListener(syncTouchListener);

        recyclerViews.add(recyclerView);
        currentListSize = recyclerViews.size();
    }

    public void syncRowPosition(RecyclerView recyclerView) {
        int offsetDiff = recyclerViews.get(0).computeHorizontalScrollOffset() - recyclerView.computeHorizontalScrollOffset();
        if (offsetDiff != 0) {
            recyclerView.smoothScrollBy(offsetDiff, 0);
        }
    }

    public void syncAllRows() {
        int masterOffset = recyclerViews.get(0).computeHorizontalScrollOffset();

        List<RecyclerView> tempList =  recyclerViews.subList(1, currentListSize);
        for (RecyclerView recyclerView : tempList) {
            int diff = masterOffset - recyclerView.computeHorizontalScrollOffset();
            if (diff != 0) {
                recyclerView.scrollBy(diff, 0);
            }
        }
    }


    private RecyclerView.OnScrollListener syncScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dx < 0) {
                ((MainActivity) context).horizontalCollapseFAB();
            }
            if (recyclerViews.indexOf(recyclerView) == mTouchedRvPosition) {
                for (int i = 0; i < currentListSize; i++) {
                    if (i != mTouchedRvPosition) {
                        RecyclerView tempRecyclerView = recyclerViews.get(i);
                        tempRecyclerView.scrollBy(dx, dy);
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    };

    private RecyclerView.OnItemTouchListener syncTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            mTouchedRvPosition = recyclerViews.indexOf(rv);
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

}
