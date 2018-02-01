package com.mobisolutions.ams.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 8/8/17.
 */

public class StandardProgressBar extends ProgressBar {

    /**
     * Resource id for the progress bars specified color
     */
    private int colorResourceId;

    /**
     * StandardProgressBar Constructor.
     *
     * @param context Context
     */
    public StandardProgressBar(Context context) {
        super(context);

        init();
    }

    /**
     * StandardProgressBar Constructor.
     *
     * @param context Context
     * @param attrs   Attribute Set
     */
    public StandardProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        loadAttrs(attrs);

        init();
    }

    /**
     * StandardProgressBar Constructor.
     *
     * @param context  Context
     * @param attrs    Attribute Set
     * @param defStyle Style Attribute
     */
    public StandardProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        loadAttrs(attrs);

        init();
    }

    /**
     * Grabs the custom style attributes
     */
    private void loadAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }

        TypedArray a = null;
        try {
          //  a = getContext().obtainStyledAttributes(attributeSet, R.styleable.StandardProgressBar);

          //  setColorResourceId(a.getResourceId(R.styleable.StandardProgressBar_progressBarColor, 0));
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private void init() {
        setContentDescription(getResources().getString(R.string.cd_loading_indicator));
        setupIndeterminateDrawable();
    }

    /**
     * Assigns the custom style attribute to the indeterminate drawable
     */
    private void setupIndeterminateDrawable() {
        Drawable drawable = getIndeterminateDrawable();
        if (drawable == null) {
            return;
        }

        if (colorResourceId == 0) {
            return;
        }

        drawable.setColorFilter(ContextCompat.getColor(getContext(), colorResourceId), PorterDuff.Mode.SRC_IN);
    }

    public int getColorResourceId() {
        return colorResourceId;
    }

    public void setColorResourceId(int colorResourceId) {
        this.colorResourceId = colorResourceId;
    }

    /**
     * Shows the View
     */
    public void show() {
        setVisibility(View.VISIBLE);
    }

    /**
     * Hides the View
     */
    public void hide() {
        setVisibility(View.GONE);
    }
}