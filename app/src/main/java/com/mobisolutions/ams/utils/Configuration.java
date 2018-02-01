package com.mobisolutions.ams.utils;

import android.content.Context;

import java.util.Map;

/**
 * Created by vkilari on 7/17/17.
 */

public class Configuration {

    private static Configuration mSharedInstance;
    static Context sContext;
    private boolean mIsInitialized;
    private Map<String, Object> mConfigMap;

    public Configuration(Context context) {
        sContext = context.getApplicationContext();
    }



    public static Configuration getSharedInstance() {
        if (mSharedInstance == null) {
            return getSharedInstance(sContext);
        } else if (!mSharedInstance.isInitialized()) {
            // after being created once, always verify we
            // have been initialized
            throw new RuntimeException("You must initialize this class first");
        }
        return mSharedInstance;
    }

    /**
     * Returns the common configuration instance.
     *
     * @return the singleton instance
     */
    public static Configuration getSharedInstance(Context context) {
        if (mSharedInstance == null) {
            mSharedInstance = new Configuration(context);
        } else if (!mSharedInstance.isInitialized()) {
            // after being created once, always verify we
            // have been initialized
            throw new RuntimeException("You must initialize this class first");
        }
        return mSharedInstance;
    }

    /**
     * A one time call used to supply a valid {@link android.content.Context} for use by this
     * component.
     *
     * @param context the valid context
     * @return the same instance being initialized
     */
    public Configuration initialize(Context context) {
        if (!mIsInitialized) {
            sContext = context;
            mIsInitialized = true;
        }
        return this;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }



}
