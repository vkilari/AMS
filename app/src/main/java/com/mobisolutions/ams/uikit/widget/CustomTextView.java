package com.mobisolutions.ams.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 7/16/17.
 */

public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable
                .TypefaceTextView);

        String fontName = styledAttrs.getString(R.styleable.TypefaceTextView_typeface);

        styledAttrs.recycle();
        if (fontName != null) {
            Typeface typeface = TypefaceCache.getInstance().getTypeface(context.getAssets(),
                    fontName);
            this.setTypeface(typeface, typeface.getStyle());
        }
    }

    public CustomTextView(Context context, String fontName) {
        super(context);
        if (isInEditMode()) {
            return;
        }
        if (fontName != null) {
            Typeface typeface = TypefaceCache.getInstance().getTypeface(context.getAssets(),
                    fontName);
            this.setTypeface(typeface, typeface.getStyle());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isEnabled()) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}