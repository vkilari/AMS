package com.mobisolutions.ams.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 8/8/17.
 */

public class StandardButton extends Button {

    private static final int[] ACTIVE = {R.attr.active};

    private static final int[] PRIMARY = {R.attr.primary};

    private static final int[] SECONDARY = {R.attr.secondary};

    private static final int STATE_LENGTH = 3;

    private boolean isActive;

    private boolean isPrimary;

    private boolean isSecondary;

    /**
     * StandardButton Constructor.
     *
     * @param context Context
     */
    public StandardButton(Context context) {
        super(context);

        init(null);
    }

    /**
     * Standard Button Constructor.
     *
     * @param context Context
     * @param attrs   Attribute Set
     */
    public StandardButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        loadStateFromAttrs(attrs);

        init(attrs);
    }

    /**
     * Standard Button Constructor.
     *
     * @param context  Context
     * @param attrs    Attribute Set
     * @param defStyle Style Attribute
     */
    public StandardButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        loadStateFromAttrs(attrs);

        init(attrs);
    }

    private void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }

        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.StandardButtonView);

            setActive(a.getBoolean(R.styleable.StandardButtonView_active, false));

            setPrimary(a.getBoolean(R.styleable.StandardButtonView_primary, false));

            setSecondary(a.getBoolean(R.styleable.StandardButtonView_secondary, false));
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private void init(AttributeSet attrs) {
        boolean hasDefaultTypeFace = false;

        refreshDrawableState();
    }

    /**
     * Note : update the STATE_LENGTH constant when adding new states.
     */
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + STATE_LENGTH);

        if (isActive) {
            mergeDrawableStates(drawableState, ACTIVE);
        }

        if (isSecondary) {
            mergeDrawableStates(drawableState, SECONDARY);
        } else {
            setPrimary(true);
            mergeDrawableStates(drawableState, PRIMARY);
        }

        return drawableState;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public boolean isSecondary() {
        return isSecondary;
    }

    public void setSecondary(boolean isSecondary) {
        this.isSecondary = isSecondary;
    }

}