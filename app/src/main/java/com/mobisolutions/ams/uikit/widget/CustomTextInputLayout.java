package com.mobisolutions.ams.uikit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by vkilari on 7/16/17.
 */

public class CustomTextInputLayout extends TextInputLayout {
    private static final String METHOD_NOT_USED = "Un-used Method";
    private static final String TAG = CustomTextInputLayout.class.getSimpleName();

    public CustomTextInputLayout(Context context) {
        super(context, null);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        /* Fix for https://code.google.com/p/android/issues/detail?id=175228 */
        if (child instanceof EditText) {
            child.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                    .OnGlobalLayoutListener() {
                @SuppressLint("WrongCall")
                @Override
                public void onGlobalLayout() {
                    onLayout(false, getLeft(), getTop(), getRight(), getBottom());
                }
            });
            try {
                final Field collapsingTextHelper = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
                collapsingTextHelper.setAccessible(true);

                final Object obj = collapsingTextHelper.get(this);
                final Field textPaint = obj.getClass().getDeclaredField("mTextPaint");
                textPaint.setAccessible(true);

                ((TextPaint) textPaint.get(obj)).setTypeface(((EditText) child).getTypeface());
            } catch (Exception ignored) {
                Log.d(TAG, METHOD_NOT_USED, ignored);
            }
        }
        super.addView(child, index, params);
    }
}
