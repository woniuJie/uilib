package com.culiu.core.fonts;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author zhangyang
 * @ClassName: FontsOverride
 * @Description: 反射获取系统字体并替换
 * @date 2015年7月1日 下午3:50:32
 */
public class Fonts {

    private static Typeface chTypeface;

    private static Typeface enTypeface;


    public static Typeface getFont(Context context, Fonts.FontType fontType) {
        Typeface typeface = null;
        switch (fontType) {
            case CH:
//                typeface = createChTypeface(context);
                break;
            case EN:
//                typeface = createEnTypeface(context);
                break;
        }
//        if (typeface == null)
//            typeface = createChTypeface(context);
        typeface = Typeface.DEFAULT;

        return typeface;
    }

    /**
     * 创建英文字体
     *
     * @param context
     * @return
     */
    private static Typeface createEnTypeface(Context context) {
        if (enTypeface == null) {
            enTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                    "font/micross.ttf");
        }
        return enTypeface;
    }

    /**
     * 创建 方正兰亭中文字体
     *
     * @param context
     * @return
     */
    private static Typeface createChTypeface(Context context) {
        if (chTypeface == null) {
            chTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                    "font/fzltxihjw.ttf");
        }
        return chTypeface;
    }

    public enum FontType {

        CH(0),

        EN(1);

        private int mCode;

        FontType(int code) {
            this.mCode = code;
        }

        public int getValue() {
            return mCode;
        }

        public static FontType setValue(int code) {
            FontType[] fontValues = values();
            for (int i = 0; i < fontValues.length; i++) {
                FontType fontValue = fontValues[i];
                if (fontValue.getValue() == code) {
                    return fontValue;
                }
            }
            return CH;
        }

    }

}


