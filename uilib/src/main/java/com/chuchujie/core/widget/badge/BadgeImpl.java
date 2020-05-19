package com.chuchujie.core.widget.badge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.culiu.uilib.R;

/**
 * 勋章的实现类
 * Created by wangjing on 2017/12/22.
 */
public class BadgeImpl implements Badge, BadgeDelegate {

    protected int mColorBackground;
    protected int mColorBackgroundBorder;
    protected int mColorBadgeText;

    protected Drawable mDrawableBackground;
    protected Bitmap mBitmapClip;
    protected boolean mDrawableBackgroundClip;

    protected float mBackgroundBorderWidth;
    protected float mBadgeTextSize;
    protected float mBadgePadding;

    protected int mBadgeNumber;
    protected String mBadgeText;

    protected boolean mExact;
    protected boolean mShowShadow;

    protected int mBadgeGravity;
    protected float mGravityOffsetX;
    protected float mGravityOffsetY;

    protected int mDragQuadrant;

    protected RectF mBadgeTextRect;
    protected RectF mBadgeBackgroundRect;

    protected Paint.FontMetrics mBadgeTextFontMetrics;

    protected PointF mBadgeCenter;
    protected PointF mRowBadgeCenter;

    protected int mWidth;
    protected int mHeight;

    protected TextPaint mBadgeTextPaint;
    protected Paint mBadgeBackgroundPaint;
    protected Paint mBadgeBackgroundBorderPaint;


    private static final int DEFAULT_BADGE_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_BADGE_BACKGROUND_COLOR = 0xFFE84E40;

    /**
     * 绘制的宿主的View
     */
    private View mHostView;

    private Context mContext;

    public BadgeImpl(View view) {
        mHostView = view;
        mContext = mHostView.getContext();
    }

    @Override
    public void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // 关闭硬件加速
        mHostView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // 绘制文字和背景的范围
        mBadgeTextRect = new RectF();
        mBadgeBackgroundRect = new RectF();

        // 文字和背景的中心点
        mBadgeCenter = new PointF();
        mRowBadgeCenter = new PointF();

        // 绘制文字的画笔
        mBadgeTextPaint = new TextPaint();
        mBadgeTextPaint.setAntiAlias(true);
        mBadgeTextPaint.setSubpixelText(true);
        mBadgeTextPaint.setFakeBoldText(true);
        mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 绘制背景的画笔
        mBadgeBackgroundPaint = new Paint();
        mBadgeBackgroundPaint.setAntiAlias(true);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);
        mBadgeBackgroundBorderPaint = new Paint();
        mBadgeBackgroundBorderPaint.setAntiAlias(true);
        mBadgeBackgroundBorderPaint.setStyle(Paint.Style.STROKE);

        /**
         * 解析自定义属性
         */
        if (attrs == null) {
            initDefaultAttrs();
        } else {
            initAttrs(attrs, defStyleAttr, defStyleRes);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHostView.setTranslationZ(1000);
        }

        initPaints();
        measureText();
    }

    private void initAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.badge, defStyleAttr, defStyleRes);

        mBadgeText = typedArray.getString(R.styleable.badge_badge_text);
        mBadgeNumber = typedArray.getInt(R.styleable.badge_badge_number, 0);
        if (TextUtils.isEmpty(mBadgeText)) {
            mBadgeText = mBadgeNumber + "";
        }
        mColorBadgeText = typedArray.getColor(R.styleable.badge_badge_text_color,
                DEFAULT_BADGE_TEXT_COLOR);
        mBadgeTextSize = typedArray.getDimensionPixelOffset(R.styleable.badge_badge_text_size,
                PxUtils.dp2px(mContext, 11));
        mBadgePadding = typedArray.getDimensionPixelOffset(R.styleable.badge_badge_padding,
                PxUtils.dp2px(mContext, 5));
        mBadgeGravity = typedArray.getInt(R.styleable.badge_badge_gravity,
                Gravity.END | Gravity.TOP);
        mGravityOffsetX = typedArray.getDimensionPixelOffset(
                R.styleable.badge_badge_gravity_offset_x, PxUtils.dp2px(mContext, 1));
        mGravityOffsetY = typedArray.getDimensionPixelOffset(
                R.styleable.badge_badge_gravity_offset_y, PxUtils.dp2px(mContext, 1));
        mShowShadow = typedArray.getBoolean(R.styleable.badge_badge_show_shadow, false);
        mDragQuadrant = typedArray.getInt(R.styleable.badge_badge_shadow_quadrant, 1);
        mDrawableBackground = typedArray.getDrawable(R.styleable.badge_badge_background_drawable);
        mDrawableBackgroundClip = typedArray.getBoolean(
                R.styleable.badge_badge_background_drawable_clip, false);
        mBackgroundBorderWidth = typedArray.getDimensionPixelOffset(
                R.styleable.badge_badge_background_border_width, 0);
        mColorBackgroundBorder = typedArray.getColor(
                R.styleable.badge_badge_background_border_color, DEFAULT_BADGE_BACKGROUND_COLOR);
        mColorBackground = typedArray.getColor(
                R.styleable.badge_badge_background_color, DEFAULT_BADGE_BACKGROUND_COLOR);
        mExact = typedArray.getBoolean(R.styleable.badge_badge_exact, false);
        typedArray.recycle();
    }

    private void initDefaultAttrs() {
        // 背景色和文字颜色
        mColorBackground = DEFAULT_BADGE_BACKGROUND_COLOR;
        mColorBadgeText = DEFAULT_BADGE_TEXT_COLOR;

        mBadgeTextSize = PxUtils.dp2px(mContext, 11);
        mBadgePadding = PxUtils.dp2px(mContext, 5);

        mBadgeNumber = 0;

        mBadgeGravity = Gravity.END | Gravity.TOP;

        mGravityOffsetX = PxUtils.dp2px(mContext, 1);
        mGravityOffsetY = PxUtils.dp2px(mContext, 1);

        // 显示阴影
        mShowShadow = false;
        mDragQuadrant = 1;

        mDrawableBackgroundClip = false;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mHostView.isInEditMode())
            return;

        if (!TextUtils.isEmpty(mBadgeText)) {
            float badgeRadius = getBadgeCircleRadius();
            findBadgeCenter();

            canvas.save();
            drawBadge(canvas, mBadgeCenter, badgeRadius);
            canvas.restore();
        }
    }

    private void initPaints() {
        showShadowImpl(mShowShadow);
        mBadgeBackgroundPaint.setColor(mColorBackground);
        mBadgeBackgroundBorderPaint.setColor(mColorBackgroundBorder);
        mBadgeBackgroundBorderPaint.setStrokeWidth(mBackgroundBorderWidth);
        mBadgeTextPaint.setColor(mColorBadgeText);
        mBadgeTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void showShadowImpl(boolean showShadow) {
        int x = PxUtils.dp2px(mContext, 1);
        int y = PxUtils.dp2px(mContext, 1.5f);
        switch (mDragQuadrant) {
            case 1:
                x = PxUtils.dp2px(mContext, 1);
                y = PxUtils.dp2px(mContext, -1.5f);
                break;
            case 2:
                x = PxUtils.dp2px(mContext, -1);
                y = PxUtils.dp2px(mContext, -1.5f);
                break;
            case 3:
                x = PxUtils.dp2px(mContext, -1);
                y = PxUtils.dp2px(mContext, 1.5f);
                break;
            case 4:
                x = PxUtils.dp2px(mContext, 1);
                y = PxUtils.dp2px(mContext, 1.5f);
                break;
        }
        mBadgeBackgroundPaint.setShadowLayer(showShadow ? PxUtils.dp2px(mContext, 2f)
                : 0, x, y, 0x33000000);
    }

    private void drawBadge(Canvas canvas, PointF center, float radius) {
        if (center.x == -1000 && center.y == -1000) {
            return;
        }
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBadgeBackgroundRect.left = center.x - (int) radius;
            mBadgeBackgroundRect.top = center.y - (int) radius;
            mBadgeBackgroundRect.right = center.x + (int) radius;
            mBadgeBackgroundRect.bottom = center.y + (int) radius;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundBorderPaint);
                }
            }
        } else {
            mBadgeBackgroundRect.left = center.x - (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.top = center.y - (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            mBadgeBackgroundRect.right = center.x + (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.bottom = center.y + (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            radius = mBadgeBackgroundRect.height() / 2f;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundBorderPaint);
                }
            }
        }
        if (!mBadgeText.isEmpty()) {
            canvas.drawText(mBadgeText, center.x,
                    (mBadgeBackgroundRect.bottom + mBadgeBackgroundRect.top
                            - mBadgeTextFontMetrics.bottom - mBadgeTextFontMetrics.top) / 2f,
                    mBadgeTextPaint);
        }
    }

    private void drawBadgeBackground(Canvas canvas) {
        mBadgeBackgroundPaint.setShadowLayer(0, 0, 0, 0);
        int left = (int) mBadgeBackgroundRect.left;
        int top = (int) mBadgeBackgroundRect.top;
        int right = (int) mBadgeBackgroundRect.right;
        int bottom = (int) mBadgeBackgroundRect.bottom;
        if (mDrawableBackgroundClip) {
            right = left + mBitmapClip.getWidth();
            bottom = top + mBitmapClip.getHeight();
            canvas.saveLayer(left, top, right, bottom, null, Canvas.ALL_SAVE_FLAG);
        }
        mDrawableBackground.setBounds(left, top, right, bottom);
        mDrawableBackground.draw(canvas);
        if (mDrawableBackgroundClip) {
            mBadgeBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mBitmapClip, left, top, mBadgeBackgroundPaint);
            // canvas.restore();
            mBadgeBackgroundPaint.setXfermode(null);
            if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
                canvas.drawCircle(mBadgeBackgroundRect.centerX(), mBadgeBackgroundRect.centerY(),
                        mBadgeBackgroundRect.width() / 2f, mBadgeBackgroundBorderPaint);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect,
                        mBadgeBackgroundRect.height() / 2, mBadgeBackgroundRect.height() / 2,
                        mBadgeBackgroundBorderPaint);
            }
        } else {
            canvas.drawRect(mBadgeBackgroundRect, mBadgeBackgroundBorderPaint);
        }
    }

    private void createClipLayer() {
        if (mBadgeText == null) {
            return;
        }
        if (!mDrawableBackgroundClip) {
            return;
        }
        if (mBitmapClip != null && !mBitmapClip.isRecycled()) {
            mBitmapClip.recycle();
        }
        float radius = getBadgeCircleRadius();
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBitmapClip = Bitmap.createBitmap((int) radius * 2, (int) radius * 2,
                    Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            srcCanvas.drawCircle(srcCanvas.getWidth() / 2f, srcCanvas.getHeight() / 2f,
                    srcCanvas.getWidth() / 2f, mBadgeBackgroundPaint);
        } else {
            mBitmapClip = Bitmap.createBitmap((int) (mBadgeTextRect.width() + mBadgePadding * 2),
                    (int) (mBadgeTextRect.height() + mBadgePadding), Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                srcCanvas.drawRoundRect(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight(), srcCanvas.getHeight() / 2f,
                        srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            } else {
                srcCanvas.drawRoundRect(new RectF(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight()),
                        srcCanvas.getHeight() / 2f, srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            }
        }
    }

    /**
     * 计算圆圈的半径
     *
     * @return
     */
    private float getBadgeCircleRadius() {
        if (mBadgeText.isEmpty()) {
            return mBadgePadding;
        } else if (mBadgeText.length() == 1) {
            return mBadgeTextRect.height() > mBadgeTextRect.width() ?
                    mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f :
                    mBadgeTextRect.width() / 2f + mBadgePadding * 0.5f;
        } else {
            return mBadgeBackgroundRect.height() / 2f;
        }
    }

    /**
     * 找中点点
     */
    private void findBadgeCenter() {
        float rectWidth = mBadgeTextRect.height() > mBadgeTextRect.width() ?
                mBadgeTextRect.height() : mBadgeTextRect.width();
        switch (mBadgeGravity) {
            case Gravity.START | Gravity.TOP:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.START | Gravity.BOTTOM:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.END | Gravity.TOP:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.END | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.TOP:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.CENTER | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER | Gravity.START:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.END:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight / 2f;
                break;
        }
        initRowBadgeCenter();
    }

    private void measureText() {
        mBadgeTextRect.left = 0;
        mBadgeTextRect.top = 0;
        if (TextUtils.isEmpty(mBadgeText)) {
            mBadgeTextRect.right = 0;
            mBadgeTextRect.bottom = 0;
        } else {
            mBadgeTextPaint.setTextSize(mBadgeTextSize);
            mBadgeTextRect.right = mBadgeTextPaint.measureText(mBadgeText);
            mBadgeTextFontMetrics = mBadgeTextPaint.getFontMetrics();
            mBadgeTextRect.bottom = mBadgeTextFontMetrics.descent - mBadgeTextFontMetrics.ascent;
        }
        createClipLayer();
    }

    private void initRowBadgeCenter() {
        int[] screenPoint = new int[2];
        mHostView.getLocationOnScreen(screenPoint);
        mRowBadgeCenter.x = mBadgeCenter.x + screenPoint[0];
        mRowBadgeCenter.y = mBadgeCenter.y + screenPoint[1];
    }

    /**
     * @param badgeNumber equal to zero badge will be hidden, less than zero show dot
     */
    @Override
    public Badge setBadgeNumber(int badgeNumber) {
        mBadgeNumber = badgeNumber;
        if (mBadgeNumber < 0) {
            mBadgeText = "";
        } else if (mBadgeNumber > 99) {
            mBadgeText = mExact ? String.valueOf(mBadgeNumber) : "99+";
        } else if (mBadgeNumber > 0 && mBadgeNumber <= 99) {
            mBadgeText = String.valueOf(mBadgeNumber);
        } else if (mBadgeNumber == 0) {
            mBadgeText = null;
        }
        measureText();
        mHostView.invalidate();
        return this;
    }

    @Override
    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    @Override
    public Badge setBadgeText(String badgeText) {
        mBadgeText = badgeText;
        mBadgeNumber = 1;
        measureText();
        mHostView.invalidate();
        return this;
    }

    @Override
    public String getBadgeText() {
        return mBadgeText;
    }

    @Override
    public Badge setExactMode(boolean isExact) {
        mExact = isExact;
        if (mBadgeNumber > 99) {
            setBadgeNumber(mBadgeNumber);
        }
        return this;
    }

    @Override
    public boolean isExactMode() {
        return mExact;
    }

    @Override
    public Badge setShowShadow(boolean showShadow) {
        mShowShadow = showShadow;
        mHostView.invalidate();
        return this;
    }

    @Override
    public boolean isShowShadow() {
        return mShowShadow;
    }

    @Override
    public Badge setBadgeBackgroundColor(int color) {
        mColorBackground = color;
        if (mColorBackground == Color.TRANSPARENT) {
            mBadgeTextPaint.setXfermode(null);
        } else {
            mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }

        // update paint color
        mBadgeBackgroundPaint.setColor(mColorBackground);

        mHostView.invalidate();
        return this;
    }

    @Override
    public Badge stroke(int color, float width, boolean isDpValue) {
        mColorBackgroundBorder = color;
        mBackgroundBorderWidth = isDpValue ? PxUtils.dp2px(mContext, width) : width;

        // update paint color
        mBadgeBackgroundBorderPaint.setColor(mColorBackgroundBorder);
        mBadgeBackgroundBorderPaint.setStrokeWidth(mBackgroundBorderWidth);

        mHostView.invalidate();
        return this;
    }

    @Override
    public int getBadgeBackgroundColor() {
        return mColorBackground;
    }

    @Override
    public Badge setBadgeBackground(Drawable drawable) {
        return setBadgeBackground(drawable, false);
    }

    @Override
    public Badge setBadgeBackground(Drawable drawable, boolean clip) {
        mDrawableBackgroundClip = clip;
        mDrawableBackground = drawable;
        createClipLayer();
        mHostView.invalidate();
        return this;
    }

    @Override
    public Drawable getBadgeBackground() {
        return mDrawableBackground;
    }

    @Override
    public Badge setBadgeTextColor(int color) {
        mColorBadgeText = color;
        mHostView.invalidate();
        return this;
    }

    @Override
    public int getBadgeTextColor() {
        return mColorBadgeText;
    }

    @Override
    public Badge setBadgeTextSize(float size, boolean isSpValue) {
        mBadgeTextSize = isSpValue ? PxUtils.sp2px(mContext, size) : size;
        measureText();
        mHostView.invalidate();
        return this;
    }

    @Override
    public float getBadgeTextSize(boolean isSpValue) {
        return isSpValue ? PxUtils.px2sp(mContext, mBadgeTextSize) : mBadgeTextSize;
    }

    @Override
    public Badge setBadgePadding(float padding, boolean isDpValue) {
        mBadgePadding = isDpValue ? PxUtils.dp2px(mContext, padding) : padding;
        createClipLayer();
        mHostView.invalidate();
        return this;
    }

    @Override
    public float getBadgePadding(boolean isDpValue) {
        return isDpValue ? PxUtils.px2dp(mContext, mBadgePadding) : mBadgePadding;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM ,
     *                Gravity.CENTER , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ,
     *                Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END
     */
    @Override
    public Badge setBadgeGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER) ||
                gravity == (Gravity.CENTER | Gravity.TOP) ||
                gravity == (Gravity.CENTER | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER | Gravity.START) ||
                gravity == (Gravity.CENTER | Gravity.END)) {
            mBadgeGravity = gravity;
            mHostView.invalidate();
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER" +
                    " , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ," +
                    "Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END");
        }
        return this;
    }

    @Override
    public int getBadgeGravity() {
        return mBadgeGravity;
    }

    @Override
    public Badge setGravityOffset(float offset, boolean isDpValue) {
        return setGravityOffset(offset, offset, isDpValue);
    }

    @Override
    public Badge setGravityOffset(float offsetX, float offsetY, boolean isDpValue) {
        mGravityOffsetX = isDpValue ? PxUtils.dp2px(mContext, offsetX) : offsetX;
        mGravityOffsetY = isDpValue ? PxUtils.dp2px(mContext, offsetY) : offsetY;
        mHostView.invalidate();
        return this;
    }

    @Override
    public float getGravityOffsetX(boolean isDpValue) {
        return isDpValue ? PxUtils.px2dp(mContext, mGravityOffsetX) : mGravityOffsetX;
    }

    @Override
    public float getGravityOffsetY(boolean isDpValue) {
        return isDpValue ? PxUtils.px2dp(mContext, mGravityOffsetY) : mGravityOffsetY;
    }

}
