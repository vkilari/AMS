package com.mobisolutions.ams.uikit.widget;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by vkilari on 7/16/17.
 */

public class TypefaceCache {

    private static final TypefaceCache instance = new TypefaceCache();
    private static final Map<String, Typeface> mCache = new HashMap<String, Typeface>();

    public static TypefaceCache getInstance() {
        return instance;
    }

    /**
     * @return typeface
     * @throws RuntimeException If font not found in path.
     */
    public Typeface getTypeface(AssetManager assetManager, String fontName) {

        if (mCache.containsKey(fontName)) {
            return mCache.get(fontName);
        } else {
            Typeface typeface;
            typeface = new WeakReference<Typeface>(Typeface.createFromAsset(assetManager, String
                    .format(Locale.US, "fonts/%s", fontName))).get();
            mCache.put(fontName, typeface);
            return typeface;
        }
    }
}
