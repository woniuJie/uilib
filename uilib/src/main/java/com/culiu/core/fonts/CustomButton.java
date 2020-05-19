package com.culiu.core.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.culiu.uilib.R;


/**
 * @author zhangyang
 * @ClassName: CustomRadioButton
 * @Description: 自定义RadioButton用与替换字体
 * @date 2015年7月10日 下午1:26:16
 */
public class CustomButton extends Button {

    private Fonts.FontType fontType;

    public CustomButton(Context context) {
        this(context, null);
    }

    public CustomButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(null,0);
    }

    public CustomButton(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        initView(attributeSet, defStyle);
    }

    private void initView(AttributeSet attributeSet, int defStyle) {
        if (isInEditMode())
            return;
        if (attributeSet == null) {
            fontType = Fonts.FontType.CH;
        } else {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet,
                    R.styleable.Font, defStyle, 0);
            fontType = Fonts.FontType.setValue(
                    typedArray.getInt(R.styleable.Font_customFontFamily, 0));

        }
        setFontType(fontType);
    }

    public void setFontType(Fonts.FontType fontType) {
        setTypeface(Fonts.getFont(getContext(), fontType));
    }

    public Fonts.FontType getFontType() {
        return fontType;
    }

}
