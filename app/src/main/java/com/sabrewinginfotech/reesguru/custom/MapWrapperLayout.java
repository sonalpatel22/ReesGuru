package com.sabrewinginfotech.reesguru.custom;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Dashrath on 10/5/2017.
 */

public class MapWrapperLayout extends FrameLayout {
    private OnDragListener mOnDragListener;

    public MapWrapperLayout(Context context) {
        super(context);
    }

    public interface OnDragListener {
        public void onDrag(MotionEvent motionEvent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnDragListener != null) {
            mOnDragListener.onDrag(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }

}
