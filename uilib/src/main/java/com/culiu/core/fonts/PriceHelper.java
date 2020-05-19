package com.culiu.core.fonts;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;



import com.culiu.uilib.R;

import junit.framework.Assert;

/**
 * 价格显示特殊样式的帮助类
 * Created by wangjing on 5/19/16.
 */
public class PriceHelper {

    private AbsoluteSizeSpan mAbsoluteSizeSpan;

    private String mPrefixStr = "";

    public PriceHelper(Context context) {

        if(context == null) {
            throw new NullPointerException(String.format("\"%s\" 参数不能为空.", new Object[]{ "context can't be null."}));
        }

        mAbsoluteSizeSpan = new AbsoluteSizeSpan(
                context.getResources().getDimensionPixelSize(R.dimen.font_11_sp));
        mPrefixStr = context.getResources().getString(R.string.rmb_symbol);
    }

    /**
     * 设置拼接前缀
     *
     * @param prefixStr
     */
    public void setPrefixStr(String prefixStr) {
        this.mPrefixStr = prefixStr;
    }

    /**
     * 设置AbsoluteSizeSpan，调整字体大小
     *
     * @param absoluteSizeSpan
     */
    public void setAbsoluteSizeSpan(AbsoluteSizeSpan absoluteSizeSpan) {
        this.mAbsoluteSizeSpan = absoluteSizeSpan;
    }

    /**
     * 生成spanable
     *
     * @param text
     * @return
     */
    public CharSequence getText(String text) {
        if (TextUtils.isEmpty(text))
            return "";
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (!text.subSequence(0, 1).toString().equals(mPrefixStr)) {
            ssb.append(mPrefixStr);
        }
        ssb.append(text);
        ssb.setSpan(mAbsoluteSizeSpan, 0, mPrefixStr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return ssb;
    }

}
