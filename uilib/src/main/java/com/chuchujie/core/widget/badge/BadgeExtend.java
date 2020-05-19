package com.chuchujie.core.widget.badge;

import android.graphics.PointF;
import android.view.View;

/**
 * 勋章的扩展
 */
public interface BadgeExtend extends Badge {

    boolean isDraggable();

    BadgeExtend setOnDragStateChangedListener(OnDragStateChangedListener l);

    PointF getDragCenter();

    BadgeExtend bindTarget(View view);

    View getTargetView();

    void hide(boolean animate);

    interface OnDragStateChangedListener {
        int STATE_START = 1;
        int STATE_DRAGGING = 2;
        int STATE_DRAGGING_OUT_OF_RANGE = 3;
        int STATE_CANCELED = 4;
        int STATE_SUCCEED = 5;

        void onDragStateChanged(int dragState, BadgeExtend badge, View targetView);
    }
} 