package com.chuchujie.core.widget.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.chuchujie.core.widget.badge.Badge;
import com.chuchujie.core.widget.badge.BadgeDelegate;
import com.chuchujie.core.widget.badge.BadgeImpl;

/**
 * Created by wangjing on 2017/12/22.
 */
public class BadgeImageView extends ImageView {

    private BadgeDelegate mBadgeDelegate;

    public BadgeImageView(Context context) {
        super(context);
        initBadgeDelegate(null, 0, 0);
    }

    public BadgeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBadgeDelegate(attrs, 0, 0);
    }

    public BadgeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBadgeDelegate(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BadgeImageView(Context context, @Nullable AttributeSet attrs,
                          int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBadgeDelegate(attrs, defStyleAttr, defStyleRes);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBadgeDelegate != null) {
            mBadgeDelegate.onDraw(canvas);
        }
    }

    public Badge getBadge() {
        return (Badge) mBadgeDelegate;
    }

}
