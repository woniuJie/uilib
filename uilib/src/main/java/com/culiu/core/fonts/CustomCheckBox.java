package com.culiu.core.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.culiu.uilib.R;


public class CustomCheckBox extends CheckBox {

    private Fonts.FontType fontType;

    public CustomCheckBox(Context context) {
        super(context, null);
        initView(null, 0);
    }

    public CustomCheckBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(null, 0);
    }

    public CustomCheckBox(Context context, AttributeSet attributeSet, int defStyle) {
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
