package com.mobisolutions.ams.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mobisolutions.ams.config.BuildConfig;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vkilari on 7/17/17.
 */

public class CacheUtils {

    private static final String LOG_TAG = CacheUtils.class.getSimpleName();
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /**
     * Creates a unique subdirectory of the designated app cache directory.
     * Updated to use internal storage only.  McD Security team deemed external
     * storage to be a risk. (images could be replaced maliciously, etc.)
     *
     * @param context    contains global information about an application environment
     * @param uniqueName a file name
     * @return a file system entity or Null
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {

        if (context != null) {

            String cachePath = null;

            // Check if media is mounted or storage is built-in,
            // if so, try and use external cache dir
            // otherwise use internal cache dir

            final File internal = context.getCacheDir();
            if (internal != null) {
                cachePath = internal.getPath();
            }

            if (!TextUtils.isEmpty(cachePath)) {
                final File cacheFile = new File(cachePath + File.separator + uniqueName);
                final boolean dirs = cacheFile.exists() || cacheFile.mkdir();
                if (dirs) {
                    return cacheFile;
                }
            }

            return null;

        }

        return null;
    }

    /**
     * Create MD5 Hash
     *
     * @param s the string for convertation
     * @return the computed one way hash value or empty string
     */
    public static String md5(final String s) {

        final String MD5 = "MD5";
        try {

            // Create MD5 Hash
            final MessageDigest digestor = MessageDigest.getInstance(MD5);
            digestor.update(s.getBytes());

            return bytesToHex(digestor.digest());

        } catch (NoSuchAlgorithmException e) {

            if (BuildConfig.DEBUG) {
                Log.e(LOG_TAG, "Failed to generate hash", e);
            }
        }

        return "";
    }

    private static String bytesToHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 0x0F];
        }

        return new String(hexChars);
    }
}
