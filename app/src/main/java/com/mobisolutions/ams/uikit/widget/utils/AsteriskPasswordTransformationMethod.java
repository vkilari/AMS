package com.mobisolutions.ams.uikit.widget.utils;

import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by vkilari on 7/16/17.
 */

public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @NonNull
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private static class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }

        @Override
        public char charAt(int index) {
            return '\u002a'; // *
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }
}
