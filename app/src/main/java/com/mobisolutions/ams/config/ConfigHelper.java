package com.mobisolutions.ams.config;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vkilari on 7/17/17.
 */

public class ConfigHelper {

    private static final int SUBSTRING_START_POSITION = 4;
    private static final int BUFFER_SIZE = 8192;
    private static String TAG = "ConfigHelper";
    private Context mContext = null;
    private Map<String, Object> mConfigMap = null;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Loads the specified json file as the global configuration from local storage
     *
     * @param configurationString the JSON string containing the configuration to use
     */
    public void loadConfigurationWithJsonString(@NonNull final String configurationString) {
        // overwrite any old config and broadcast the change
        mConfigMap = new Gson().fromJson(configurationString, HashMap.class);
    }

    /**
     * Loads Configuration From Given JSON Object
     *
     * @param configuration the JSON object
     */
    public void loadConfigurationWithJsonObject(@NonNull Map<String, Object> configuration) {
        mConfigMap = configuration;
    }

    /**
     * Replaces the current configuration with the file located at the specified URL.
     *
     * @param url location of configuration file
     */
    public void loadConfigurationWithUrl(URL url) {
        URL mUrl = url;
        try {
            URLConnection connection = mUrl.openConnection();
            InputStream inStream = new BufferedInputStream(connection.getInputStream());

            StringBuilder jsonStringBuilder = new StringBuilder();
            int bytesRead = 0;
            byte[] buffer = new byte[BUFFER_SIZE];

            try {
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    jsonStringBuilder.append(buffer);
                }
            } finally {
                inStream.close();
            }

            // overwrite any old config and broadcast the change
            if (jsonStringBuilder.length() > 0) {
                mConfigMap = new Gson().fromJson(jsonStringBuilder.toString(), HashMap.class);
            }
        } catch (IOException e) {
            // could not get remote config!
            Log.e(TAG,
                    "Error accessing configuration URL. Your application must provide a configuration. " +
                            "Please see the Getting Started Guide for more information.", e);
        }
    }

    /**
     * Validates that a key exists in the configuration
     *
     * @param key the key or key path to validate
     * @return true if key exists, false otherwise
     */
    public boolean hasKey(final String key) {
        return getValueForKey(key) != null;
    }

    /**
     * The general getter for any property within the Configuration data structure.
     *
     * @param key the key or key path to retrieve
     * @param <T> the expected type of the returned value
     * @return the Object requested in the config, or null if not present
     */
    public <T> T getValueForKey(String key) {
        String[] args = null;
        if (key != null) {
            args = key.split("\\.");
        } else {
            return null;
        }

        if (mConfigMap == null) {
            Log.e(TAG, "Error accessing configuration. Your application must provide a configuration. Please see the Getting Started Guide for more information.");
        }

        return (T) getValueForKey(args, mConfigMap);
    }

    private <T> T getValueForKey(String[] args, Map<String, ?> base) {
        if (args == null || mConfigMap == null) {
            return null;
        }

        Object result = base.get(args[0]);
        if (args.length > 1 && result instanceof Map) {
            return getValueForKey(Arrays.copyOfRange(args, 1, args.length), (Map<String, ?>) result);
        } else {
            return (T) result;
        }
    }

    /**
     * Convenience method to obtain a primitive double
     *
     * @param key the key or key path to retrieve
     * @return the double value, or 0.0 if not present
     */
    public double getDoubleForKey(String key) {
        Object result = getValueForKey(key);
        if (result instanceof Double) {
            return ((Double) result).doubleValue();
        }
        return 0.0;
    }

    /**
     * Convenience method to obtain a primitive long
     *
     * @param key the key or key path to retrieve
     * @return the long value, or 0L if not present
     */
    public long getLongForKey(String key) {
        Object result = getValueForKey(key);
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0L;
    }

    /**
     * Convenience method to obtain a primitive int
     *
     * @param key the key or key path to retrieve
     * @return the int value, or 0 if not present
     */
    public int getIntForKey(String key) {
        Object result = getValueForKey(key);
        if (result instanceof Number) {
            return ((Number) result).intValue();
        }
        return 0;
    }

    /**
     * Convenience method to obtain a primitive boolean
     *
     * @param key the key or key path to retrieve
     * @return the boolean value, or false if not present
     */
    public boolean getBooleanForKey(String key) {
        Object result = getValueForKey(key);
        return result instanceof Boolean && ((Boolean) result).booleanValue();
    }

    /**
     * Convenience method to obtain a {@link java.lang.String}
     *
     * @param key {@link java.lang.String} the key or key path to retrieve
     * @return {@link java.lang.String} retrieved or null if not present
     */
    public String getLocalizedStringForKey(final String key) {
        Object result = getValueForKey(key);
        if (result instanceof String) {
            return localizedStringForKey((String) result);
        }

        return null;
    }

    /**
     * Convenience method to obtain a {@link java.lang.String}
     *
     * @param key {@link java.lang.String} the key or key path to retrieve
     * @return {@link java.lang.String} retrieved or null if not present
     */
    public String localizedStringForKey(final String key) {
        if (key != null) {
            // if our key starts with "raw:", we take whatever is after it literally
            if (key.startsWith("raw:")) {
                return key.substring(SUBSTRING_START_POSITION);
            }

            // if our key is not "raw:", then we try to find the matching resource
            int resourceId = mContext.getResources().getIdentifier(key, "string", mContext.getPackageName());

            // and if we find it, get the localized version.  Otherwise we just spit the key back
            return resourceId > 0 ? mContext.getString(resourceId) : key;
        }

        // if key was null, we just return null
        return null;
    }
}
