package com.chuchujie.core.widget.badge;

import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * 代理View的一些方法到Badge中
 * <p>
 * Created by wangjing on 2017/12/22.
 */
public interface BadgeDelegate {

    /**
     * 初始化属性
     *
     * @param attrs
     * @param defStyleAttr
     */
    void init(AttributeSet attrs, int defStyleAttr, int defStyleRes);

    /**
     * view的宽高发送变化
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    void onSizeChanged(int w, int h, int oldw, int oldh);

    /**
     * 绘制view
     *
     * @param canvas
     */
    void onDraw(Canvas canvas);

}
