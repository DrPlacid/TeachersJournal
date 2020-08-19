package com.doctorplacid.activity;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class AnimationManager {

    private static final int BUTTON_ANIMATION_TIME = 400;

    public static boolean addColumnButtonShown = false;

    public static void horizontalExpand(final FrameLayout expandableLayout) {
        if (addColumnButtonShown) return;
        int parentWidth = View.MeasureSpec.makeMeasureSpec((expandableLayout.getChildAt(0)).getWidth(), View.MeasureSpec.EXACTLY);
        int parentHeight = View.MeasureSpec.makeMeasureSpec((expandableLayout.getChildAt(0)).getHeight(), View.MeasureSpec.EXACTLY);
        expandableLayout.measure(parentWidth, parentHeight);
        final int targetWidth = expandableLayout.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        expandableLayout.getLayoutParams().width = 1;
        expandableLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                expandableLayout.getLayoutParams().width = interpolatedTime == 1
                        ? FrameLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetWidth * interpolatedTime);
                expandableLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(BUTTON_ANIMATION_TIME);
        expandableLayout.startAnimation(a);
        addColumnButtonShown = true;
    }

    public static void verticalExpand(final FrameLayout expandableLayout) {
        int parentWidth = View.MeasureSpec.makeMeasureSpec((expandableLayout.getChildAt(0)).getWidth(), View.MeasureSpec.EXACTLY);
        int parentHeight = View.MeasureSpec.makeMeasureSpec((expandableLayout.getChildAt(0)).getHeight(), View.MeasureSpec.EXACTLY);
        expandableLayout.measure(parentWidth, parentHeight);
        final int targetHeight = expandableLayout.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        expandableLayout.getLayoutParams().height = 1;
        expandableLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                expandableLayout.getLayoutParams().height = interpolatedTime == 1
                        ? FrameLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                expandableLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(BUTTON_ANIMATION_TIME);
        expandableLayout.startAnimation(a);
    }

    public static void horizontalCollapse(final FrameLayout expandableLayout) {
        if (!addColumnButtonShown) return;
        final int initialWidth = expandableLayout.getMeasuredWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    expandableLayout.setVisibility(View.GONE);
                } else {
                    expandableLayout.getLayoutParams().width = initialWidth - (int)(initialWidth * interpolatedTime);
                    expandableLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(BUTTON_ANIMATION_TIME);
        expandableLayout.startAnimation(a);
        addColumnButtonShown = false;
    }

    public static void verticalCollapse(final FrameLayout expandableLayout) {
        final int initialHeight = expandableLayout.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    expandableLayout.setVisibility(View.GONE);
                } else {
                    expandableLayout.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    expandableLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(BUTTON_ANIMATION_TIME);
        expandableLayout.startAnimation(a);
    }
}
