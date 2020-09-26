package com.example.first.mainScreen.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class OnSwipeListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private int deltaRight = 0, deltaLeft = 0;
    private static final long SPEED_SWIPE = 300L;
    private static final int MIN_DELTA_SWIPE = 150;

    private View view;
    private Listener mListener;

    interface Listener {
        void swipeLeft();
        void swipeRight();
    }

    OnSwipeListener(Context context, View view, Listener listener) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.view = view;
        this.mListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                if ((deltaLeft < MIN_DELTA_SWIPE) && (deltaRight < MIN_DELTA_SWIPE)) {
                    onNotSwipe();
                }
                else if (deltaLeft < -MIN_DELTA_SWIPE){
                    onSwipeLeft();
                }
                else {
                    onSwipeRight();
                }
                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    private void onNotSwipe() {
        LinearLayout.LayoutParams linearLay = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLay.topMargin = view.getTop();
        deltaLeft = 0;
        deltaRight = 0;
        linearLay.leftMargin = deltaLeft;
        linearLay.rightMargin = deltaRight;

        view.setLayoutParams(linearLay);
    }

    private void onSwipeLeft() {
        final AnimatorSet all = new AnimatorSet();
        all.playSequentially(ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, -1000).setDuration(SPEED_SWIPE));
        all.start();

        view = null;

        mListener.swipeLeft();
    }

    private void onSwipeRight() {
        final AnimatorSet all = new AnimatorSet();
        all.playSequentially(ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, 1000).setDuration(SPEED_SWIPE));
        all.start();

        view = null;

        mListener.swipeRight();
    }

    final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            if (diffX > 0) {
                onScrollRight(Math.abs(diffX));
            } else {
                onScrollLeft(Math.abs(diffX));
            }

            return true;
        }

    }

    private void onScrollRight(float diffX) {
        LinearLayout.LayoutParams linearLay = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLay.topMargin = view.getTop();
        deltaLeft += diffX;
        deltaRight -= diffX;
        linearLay.leftMargin = deltaLeft;
        linearLay.rightMargin = deltaRight;

        view.setLayoutParams(linearLay);
    }

    private void onScrollLeft(float diffX) {
        LinearLayout.LayoutParams linearLay = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLay.topMargin = view.getTop();
        deltaLeft -= diffX;
        deltaRight += diffX;
        linearLay.leftMargin = deltaLeft;
        linearLay.rightMargin = deltaRight;

        view.setLayoutParams(linearLay);
    }

}