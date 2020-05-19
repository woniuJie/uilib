package com.chuchujie.core.widget.bottombar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by wangjing on 2017/12/24.
 */
public class StateListUtils {

    public static ColorStateList createColorStateList(int selected, int normal) {
        int[] colors = new int[]{selected, normal};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static ColorStateList createColorStateList(String selected, String normal) {
        int[] colors = new int[]{Color.parseColor(selected), Color.parseColor(normal)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static ColorStateList createColorStateList(String selected, String pressed, String normal) {
        int[] colors = new int[]{Color.parseColor(selected), Color.parseColor(pressed), Color.parseColor(normal)};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static StateListDrawable createStateListDrawable(Context context, int selected, int normal) {
        StateListDrawable result = new StateListDrawable();
        result.addState(new int[]{android.R.attr.state_selected}, context.getResources().getDrawable(selected));
        result.addState(new int[]{android.R.attr.state_checked}, context.getResources().getDrawable(selected));
        result.addState(new int[]{}, context.getResources().getDrawable(normal));
        return result;
    }

    public static StateListDrawable createStateListDrawable(Drawable selected, Drawable normal) {
        StateListDrawable result = new StateListDrawable();
        result.addState(new int[]{android.R.attr.state_selected}, selected);
        result.addState(new int[]{android.R.attr.state_checked}, selected);
        result.addState(new int[]{}, normal);
        return result;
    }

}
