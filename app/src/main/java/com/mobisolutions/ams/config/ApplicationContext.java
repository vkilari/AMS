package com.mobisolutions.ams.config;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mobisolutions.ams.common.AppCoreConstants;

/**
 * Created by vkilari on 7/17/17.
 */

public class ApplicationContext {

    private static Context sContext;
    private static final String TAG = "ApplicationContext";

    private ApplicationContext() {
        Log.d(TAG, AppCoreConstants.METHOD_NOT_USED);
    }

    /**
     * This method is used for passing the application context.
     *
     * @param context ApplicationContext
     */
    public static void init(@NonNull Context context) {
        sContext = context;
    }

    /**
     * Returns current application {@link Context}
     *
     * @return {@link Context} for current {@link android.app.Application}
     */
    public static Context getAppContext() {
        return sContext;
    }
}
