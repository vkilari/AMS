package com.mobisolutions.ams.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by vkilari on 7/24/17.
 */

public class AppContext {

    static Context sContext;

    private AppContext() {
    }

    /**
     * Initializes the McDonald's SDK using a {@link android.content.Context}
     * and an {@link android.content.Intent} which is used to start an activity on config change
     *
     * @param context      a {@link android.content.Context} used for most Library Context needs.
     * @param reloadIntent an {@link android.content.Intent} to be fired on config change
     * @param clearData    true if database should be cleared
     */
    public static void initialize(@NonNull Context context, @Nullable Intent reloadIntent, final String configJson,
                                  final boolean clearData) {
        sContext = context.getApplicationContext();

    }

    public static void init(@NonNull Context context) {
        sContext = context;
    }

    /**
     * returns the status of the McDonalds SDK
     *
     * @return true if the SDK has been initialized
     */
    public static boolean isInitialized() {
        return sContext != null;
    }

    /**
     * Returns current application {@link android.content.Context}
     *
     * @return {@link android.content.Context} for current {@link android.app.Application}
     */
    public static Context getContext() {
        if (isInitialized()) {
            return sContext;
        } else {
            throw new IllegalStateException("McDonalds SDK is not initialized. Please call McDonalds.initialize(Context context) to initialize this SDK.");
        }
    }

    public static Context getAppContext() {
        return sContext;
    }
}
