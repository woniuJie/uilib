package com.chuchujie.core.widget.bottombar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuchujie.core.widget.badge.Badge;
import com.chuchujie.core.widget.badge.BadgeDelegate;
import com.chuchujie.core.widget.badge.BadgeImpl;
import com.chuchujie.core.widget.badge.PxUtils;

/**
 * Created by wangjing on 2017/12/23.
 */
public class BottomBarItemView extends LinearLayout {

    private ImageView mImageView;

    private TextView mTextView;

    private BadgeDelegate mBadgeDelegate;

    private boolean mChecked;

    private boolean mBroadcasting;

    private OnCheckedChangeWidgetListener mOnCheckedChangeWidgetListener;

    public BottomBarItemView(Context context) {
        super(context);
        initView();
        initBadgeDelegate(null, 0, 0);
    }

    public BottomBarItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initBadgeDelegate(attrs, 0, 0);
    }

    public BottomBarItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initBadgeDelegate(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomBarItemView(Context context, AttributeSet attrs,
                             int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        initBadgeDelegate(attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化当前View
     */
    private void initView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        mImageView = new ImageView(getContext());
        LinearLayout.LayoutParams ivParams = new LinearLayout
                .LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivParams.width = ivParams.height = PxUtils.dp2px(getContext(), 22);
        addView(mImageView, ivParams);

        mTextView = new TextView(getContext());
        mTextView.setSingleLine();
        LinearLayout.LayoutParams tvParams = new LinearLayout
                .LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(mTextView, tvParams);


//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
        // 不加以下这句代码，不会调用onDraw()方法
        setWillNotDraw(false);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

    }

    /**
     * 初始化
     *
     * @param attrs
     * @param defStyleAttr
     */
    private void initBadgeDelegate(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mBadgeDelegate = new BadgeImpl(this);
        mBadgeDelegate.init(attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBadgeDelegate != null) {
            mBadgeDelegate.onSizeChanged(w, h, oldw, oldh);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        /**
         * 兼容小于Android 6.0 版本的绘制
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mBadgeDelegate != null) {
                mBadgeDelegate.onDraw(canvas);
            }
        }
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        // 不能再onDraw中绘制，否则层级有问题
        if (mBadgeDelegate != null) {
            mBadgeDelegate.onDraw(canvas);
        }
    }

    public Badge getBadge() {
        return (Badge) mBadgeDelegate;
    }

    /**
     * 切换选中状态
     */
    public void toggle() {
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            mImageView.setSelected(checked);
            mTextView.setSelected(checked);
            if (mBroadcasting) {
                return;
            }
            mBroadcasting = true;
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
            mBroadcasting = false;
        }
    }

    public void setOnCheckedChangeWidgetListener(OnCheckedChangeWidgetListener listener) {
        this.mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }


    static class SavedState extends BaseSavedState {
        boolean checked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "BottomBarItemView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnCheckedChangeWidgetListener {

        void onCheckedChanged(BottomBarItemView bottomBarView, boolean isChecked);

    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    /**
     * 设置icon图片
     *
     * @param selected
     * @param normal
     */
    public void setImageDrawable(@DrawableRes int selected, @DrawableRes int normal) {
        StateListDrawable stateListDrawable = StateListUtils
                .createStateListDrawable(getContext(), selected, normal);
        mImageView.setImageDrawable(stateListDrawable);
    }

    /**
     * 设置icon图片
     *
     * @param selected
     * @param normal
     */
    public void setImageDrawable(Drawable selected, Drawable normal) {
        StateListDrawable stateListDrawable = StateListUtils
                .createStateListDrawable(selected, normal);
        mImageView.setImageDrawable(stateListDrawable);
    }

    /**
     * 设置文字颜色
     *
     * @param selected
     * @param normal
     */
    public void setTextColor(int selected, int normal) {
        ColorStateList colorStateList = StateListUtils
                .createColorStateList(selected, normal);
        mTextView.setTextColor(colorStateList);
    }

    /**
     * 设置文字颜色
     *
     * @param selected
     * @param normal
     */
    public void setTextColor(String selected, String normal) {
        ColorStateList colorStateList = StateListUtils
                .createColorStateList(selected, normal);
        mTextView.setTextColor(colorStateList);
    }

    /**
     * sp值
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        mTextView.setTextSize(textSize);
    }

    /**
     * 文字和图片之间的间距
     *
     * @param space
     */
    public void setImageTextSpace(float space) {
        mTextView.setPadding(0, PxUtils.dp2px(getContext(), space), 0, 0);
    }

    /**
     * 设置图片的大小
     *
     * @param width  dp值
     * @param height dp值
     */
    public void setImageSize(int width, int height) {
        int w = PxUtils.dp2px(getContext(), width);
        int h = PxUtils.dp2px(getContext(), height);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) mImageView.getLayoutParams();
        layoutParams.width = w;
        layoutParams.height = h;
        mImageView.setLayoutParams(layoutParams);
    }

}
