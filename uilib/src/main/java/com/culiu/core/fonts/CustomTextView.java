package com.culiu.core.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.culiu.uilib.R;


public class CustomTextView extends TextView {

    private Fonts.FontType fontType;
    /**
     * 是否有￥符号，用于价格显示
     */
    private boolean hasPrice;

    /**
     * ￥符号的字体大小
     */
    private int symbolSize;

    public CustomTextView(Context context) {
        super(context);
        initView(null, 0);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(attributeSet, 0);
    }

    public CustomTextView(Context context, AttributeSet attributeSet, int defStyle) {
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

            hasPrice = typedArray.getBoolean(R.styleable.Font_hasPrice, false);
            symbolSize = typedArray.getInteger(R.styleable.Font_symbolSize, 11);

            typedArray.recycle();

        }
        setFontType(fontType);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (hasPrice && text != null) {
            PriceHelper priceHelper = new PriceHelper(getContext());
            priceHelper.setAbsoluteSizeSpan(new AbsoluteSizeSpan(symbolSize, true));
            super.setText(priceHelper.getText(text.toString()), type);
        } else {
            super.setText(text, type);
        }

    }

    public void setFontType(Fonts.FontType fontType) {
        setTypeface(Fonts.getFont(getContext(), fontType));
    }

    public Fonts.FontType getFontType() {
        return fontType;
    }

    public boolean isHasPrice() {
        return hasPrice;
    }

    public void setHasPrice(boolean hasPrice) {
        this.hasPrice = hasPrice;
        postInvalidate();
    }

    public int getSymbolSize() {
        return symbolSize;
    }

    public void setSymbolSize(int symbolSize) {
        this.symbolSize = symbolSize;
        postInvalidate();
    }
}
