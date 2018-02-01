package com.mobisolutions.ams.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;


import com.mobisolutions.ams.R;
import com.mobisolutions.ams.uikit.widget.utils.AsteriskPasswordTransformationMethod;


/**
 * Created by vkilari on 7/16/17.
 */

public class CustomEditText extends EditText {

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        String fontName = styledAttrs.getString(R.styleable.TypefaceTextView_typeface);

        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = TypefaceCache.getInstance().getTypeface(context.getAssets(),
                    fontName);
            this.setTypeface(typeface, typeface.getStyle());
        }
        styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        final boolean isPassword = styledAttrs.getBoolean(R.styleable.CustomEditText_isPassword, false);
        styledAttrs.recycle();
        Log.d("TAG", "----isPassword:::"+isPassword);
        if (isPassword) {
            setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }

    }
}
