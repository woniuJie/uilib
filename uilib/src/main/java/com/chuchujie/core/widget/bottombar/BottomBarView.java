package com.chuchujie.core.widget.bottombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by wangjing on 2017/12/23.
 */
public class BottomBarView extends LinearLayout {

    private int mCheckedId = -1;
    private BottomBarItemView.OnCheckedChangeWidgetListener mChildOnCheckedChangeListener;
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private SparseIntArray mPositionWithId = new SparseIntArray();


    public BottomBarView(@NonNull Context context) {
        super(context);
        initView();
    }

    public BottomBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BottomBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomBarView(@NonNull Context context, @Nullable AttributeSet attrs,
                         int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * 填充展示数据
     *
     * @param bottomBarInfoList
     */
    public void setUpBottomView(List<BottomBarInfo> bottomBarInfoList) {
        setUpBottomView(bottomBarInfoList, 0);
    }

    /**
     * 填充展示数据
     *
     * @param bottomBarInfoList
     * @param index             传入非法数值时，选中第一个item
     */
    public void setUpBottomView(List<BottomBarInfo> bottomBarInfoList, int index) {
        if (bottomBarInfoList == null)
            return;
        removeAllViews();
        mPositionWithId.clear();
        for (int i = 0; i < bottomBarInfoList.size(); i++) {
            BottomBarInfo bottomBarInfo = bottomBarInfoList.get(i);
            View view = createBottomBarItem(bottomBarInfo);
            int viewId = ViewIdUtils.generateViewId();
            mPositionWithId.put(viewId, i);
            view.setId(viewId);
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            addView(view, params);
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= getChildCount()) {
            index = 0;
        }
        if (bottomBarInfoList.get(0).getBottomBarBackground() != null) {
            setBackground(bottomBarInfoList.get(0).getBottomBarBackground());
        }
        setCheckedStateForView(getChildView(index).getId(), true);
    }

    private View createBottomBarItem(BottomBarInfo bottomBarInfo) {
        BottomBarItemView barItemView = new BottomBarItemView(getContext());
        barItemView.setText(bottomBarInfo.getText());
        barItemView.setTextSize(bottomBarInfo.getTextSize());
        barItemView.setImageTextSpace(bottomBarInfo.getImageTextSpace());
        barItemView.setImageDrawable(bottomBarInfo.getIconSelected(), bottomBarInfo.getIconNormal());
        barItemView.setTextColor(bottomBarInfo.getTextColorSelected(), bottomBarInfo.getTextColorNormal());
        return barItemView;
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }


//    @Override
//    public void addView(View child, int index, ViewGroup.LayoutParams params) {
//        if (child instanceof BottomBarItemView) {
//            final BottomBarItemView button = (BottomBarItemView) child;
//            if (button.isChecked()) {
//                mProtectFromCheckedChange = true;
//                if (mCheckedId != -1) {
//                    setCheckedStateForView(mCheckedId, false);
//                }
//                mProtectFromCheckedChange = false;
//                setCheckedId(button.getId());
//            }
//        }
//        super.addView(child);
//    }

    /**
     * 获取第几个Index的BottomBarItemView
     *
     * @param index
     * @return
     */
    public BottomBarItemView getChildView(int index) {
        return (BottomBarItemView) getChildAt(index);
    }

    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId,
                    mPositionWithId.get(mCheckedId, 0));
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof BottomBarItemView) {
            ((BottomBarItemView) checkedView).setChecked(checked);
        }
    }

    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    public void clearCheck() {
        check(-1);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public BottomBarView.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new BottomBarView.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof BottomBarView.LayoutParams;
    }

    @Override
    protected BottomBarView.LayoutParams generateDefaultLayoutParams() {
        return new BottomBarView.LayoutParams(BottomBarView.LayoutParams.WRAP_CONTENT,
                BottomBarView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return BottomBarView.class.getName();
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }


        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(BottomBarView bottomBarView, @IdRes int checkedId, int position);

    }

    private class CheckedStateTracker implements BottomBarItemView.OnCheckedChangeWidgetListener {
        public void onCheckedChanged(BottomBarItemView buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            setCheckedId(buttonView.getId());
        }
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        public void onChildViewAdded(View parent, View child) {
            if (parent == BottomBarView.this && child instanceof BottomBarItemView) {
                ((BottomBarItemView) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == BottomBarView.this && child instanceof BottomBarItemView) {
                ((BottomBarItemView) child).setOnCheckedChangeWidgetListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

}
