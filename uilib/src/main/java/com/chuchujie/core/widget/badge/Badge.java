package com.chuchujie.core.widget.badge;

import android.graphics.drawable.Drawable;

/**
 * 操作勋章的基本方法
 */
public interface Badge {

    Badge setBadgeNumber(int badgeNum);

    int getBadgeNumber();

    Badge setBadgeText(String badgeText);

    String getBadgeText();

    Badge setExactMode(boolean isExact);

    boolean isExactMode();

    Badge setShowShadow(boolean showShadow);

    boolean isShowShadow();

    Badge setBadgeBackgroundColor(int color);

    Badge stroke(int color, float width, boolean isDpValue);

    int getBadgeBackgroundColor();

    Badge setBadgeBackground(Drawable drawable);

    Badge setBadgeBackground(Drawable drawable, boolean clip);

    Drawable getBadgeBackground();

    Badge setBadgeTextColor(int color);

    int getBadgeTextColor();

    Badge setBadgeTextSize(float size, boolean isSpValue);

    float getBadgeTextSize(boolean isSpValue);

    Badge setBadgePadding(float padding, boolean isDpValue);

    float getBadgePadding(boolean isDpValue);

    Badge setBadgeGravity(int gravity);

    int getBadgeGravity();

    Badge setGravityOffset(float offset, boolean isDpValue);

    Badge setGravityOffset(float offsetX, float offsetY, boolean isDpValue);

    float getGravityOffsetX(boolean isDpValue);

    float getGravityOffsetY(boolean isDpValue);

}