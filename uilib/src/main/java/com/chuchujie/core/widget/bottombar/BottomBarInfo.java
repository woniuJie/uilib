package com.chuchujie.core.widget.bottombar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class BottomBarInfo {

    private Drawable iconSelected;

    private Drawable iconNormal;

    private int textColorSelected;

    private int textColorNormal;

    private int textSize;

    private int imageTextSpace;

    private String text;

    private Drawable mBottomBarBackground;

    public BottomBarInfo() {
    }

    public Drawable getIconSelected() {
        return iconSelected;
    }

    public void setIconSelected(Drawable iconSelected) {
        this.iconSelected = iconSelected;
    }

    public Drawable getIconNormal() {
        return iconNormal;
    }

    public void setIconNormal(Drawable iconNormal) {
        this.iconNormal = iconNormal;
    }


    public int getTextColorSelected() {
        return textColorSelected;
    }

    public void setTextColorSelected(int textColorSelected) {
        this.textColorSelected = textColorSelected;
    }

    public int getTextColorNormal() {
        return textColorNormal;
    }

    public void setTextColorNormal(int textColorNormal) {
        this.textColorNormal = textColorNormal;
    }

    public void setTextColorSelected(String textColorSelected) {
        this.textColorSelected = Color.parseColor(textColorSelected);
    }
    
    public void setTextColorNormal(String textColorNormal) {
        this.textColorNormal = Color.parseColor(textColorNormal);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getImageTextSpace() {
        return imageTextSpace;
    }

    public void setImageTextSpace(int imageTextSpace) {
        this.imageTextSpace = imageTextSpace;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getBottomBarBackground() {
        return mBottomBarBackground;
    }

    public void setBottomBarBackground(Drawable bottomBarBackground) {
        mBottomBarBackground = bottomBarBackground;
    }

    @Override
    public String toString() {
        return "BottomBarInfo{" +
                "iconSelected=" + iconSelected +
                ", iconNormal=" + iconNormal +
                ", textColorSelected='" + textColorSelected + '\'' +
                ", textColorNormal='" + textColorNormal + '\'' +
                ", textSize=" + textSize +
                ", imageTextSpace=" + imageTextSpace +
                ", text='" + text + '\'' +
                ", mBottomBarBackground=" + mBottomBarBackground +
                '}';
    }
}