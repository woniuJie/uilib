package com.chuchujie.core.widget.radiobutton;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.chuchujie.core.widget.badge.Badge;
import com.chuchujie.core.widget.badge.BadgeDelegate;
import com.chuchujie.core.widget.badge.BadgeImpl;
import com.culiu.core.fonts.CustomRadioButton;

/**
 * 采用组合的形式封装，展现类似购物车数字的view
 * <p>
 * 参考：https://github.com/qstumn/BadgeView 进行简化，采取更合理的形式进行封装，避免集成使用继承
 * <p>
 * Created by wangjing on 2017/12/21.
 */
public class BadgeRadioButton extends CustomRadioButton {

    private BadgeDelegate mBadgeDelegate;

    public BadgeRadioButton(Context context) {
        super(context);
        initBadgeDelegate(null, 0, 0);
    }

    public BadgeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBadgeDelegate(attrs, 0, 0);
    }

    public BadgeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBadgeDelegate(attrs, defStyleAttr, 0);
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
