package com.mobisolutions.ams.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vkilari on 7/17/17.
 */

public class LocalDataManager {

    private static final String TAG = LocalDataManager.class.getCanonicalName();

    private static LocalDataManager mSharedInstance = null;
    private SharedPreferences mSharedPreferences = null;
    private Crypto mCrypto = null;
    private DiskCacheManager mCacheManager = null;

    /**
     * Initializes the LocalDataManager with a {@link android.content.Context}
     *
     * @param context {@link android.content.Context} to initialize the LocalDataManager with
     * @return an initialized
     */
    public LocalDataManager initialize(Context context) {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mCacheManager = DiskCacheManager.getInstance(context);

        try {
            mCrypto = new Crypto(context);
        } catch (final NoSuchAlgorithmException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (final IOException e) {
            // FIXME: probably should be just using this to regen a new key
            Log.e(TAG, e.getLocalizedMessage());
        }

        return this;
    }

    public static LocalDataManager getSharedInstance() {
        if (mSharedInstance == null) {
            mSharedInstance = new LocalDataManager();
        }

        return mSharedInstance;
    }


    /**
     * Gets a {@link java.lang.String} for the given {@link java.lang.String} key
     *
     * @param key {@link java.lang.String} key
     * @param def {@link java.lang.String} default string to return if key has no value
     * @return the {@link java.lang.String} value for the given key, or returns the passed in default
     */
    public String getString(final String key, final String def) {
        String decrypted = def;

        try {
            final String encrypted = mSharedPreferences.getString(key, null);

            if (encrypted != null) {
                decrypted = mCrypto.decrypt(encrypted);
            }
        } catch (final Exception ignored) {
        }

        return decrypted;
    }


}
