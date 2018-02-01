package com.mobisolutions.ams.utils;

import android.content.Context;
import android.support.v4.BuildConfig;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vkilari on 7/17/17.
 */

public class DiskCacheManager {

    private static final String LOG_TAG = DiskCacheManager.class.getSimpleName();

    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
    private static final int DEFAULT_MEM_CACHE_SIZE = 20;
    private static final String HASH_ALGORITHM = "MD5";
    private static final String STRING_ENCODING = "UTF-8";
    private static final int VALUE_COUNT = 1;

    private static DiskCacheManager sInstance;

    private DiskLruCache mDiskCache;
    private final LruCache mMemCache;
    private final File mCacheDirectory;
    private final Gson mGson;

    public static DiskCacheManager getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DiskCacheManager(context);
        }
        return sInstance;
    }

    private DiskCacheManager(Context context) {

        mCacheDirectory = CacheUtils.getDiskCacheDir(context, "/objectcache");
        mMemCache = new LruCache(DEFAULT_MEM_CACHE_SIZE);
        mGson = new Gson();

        open();
    }

    private void open() {
        long cacheSize = DEFAULT_CACHE_SIZE;


            /* 3.X Catalogs are much smaller so use 10 MB */
            cacheSize *= 10;

            /* 2.X Catalogs are huge, allow 250 MBs for cache */
            cacheSize *= 250;


        try {
            mDiskCache = DiskLruCache.open(mCacheDirectory, BuildConfig.VERSION_CODE, VALUE_COUNT, cacheSize);
        } catch (IOException e) {
            Log.e(LOG_TAG, "error creating object cache", e);
        }
    }

    public void clear() throws IOException {
        mDiskCache.delete();
        mMemCache.evictAll();
        open();
    }

    public boolean delete(String key) throws IOException {
        mMemCache.remove(key);
        return mDiskCache.remove(getHashOf(key));
    }

    public boolean hasObjectForKey(String key) throws IOException {
        boolean hasObject = mMemCache.get(key) != null;

        if (!hasObject) {
            hasObject = mDiskCache.get(getHashOf(key)) != null;
        }

        return hasObject;
    }

    public <T> T get(String key, Type type) throws IOException {
        T ob = (T) mMemCache.get(key);
        if (ob != null) {
            return ob;
        }
        DiskLruCache.Snapshot snapshot = mDiskCache.get(getHashOf(key));
        if (snapshot != null) {
            String value = snapshot.getString(0);
            ob = mGson.fromJson(value, type);
        }
        return ob;
    }

    public <T> T get(String key, Type type, boolean serialize) throws IOException {

        if (serialize == false) {
            return get(key, type);
        }

        T ob = (T) mMemCache.get(key);
        if (ob != null) {
            return ob;
        }
        DiskLruCache.Snapshot snapshot = mDiskCache.get(getHashOf(key));
        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
            jsonReader.setLenient(true);
            ob = mGson.fromJson(jsonReader, type);
        }
        return ob;
    }

    public void put(String key, Object object) throws IOException {
        mMemCache.put(key, object);
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(getHashOf(key));
            if (editor == null) {
                return;
            }

            if (writeValueToCache(mGson.toJson(object), editor)) {
                mDiskCache.flush();
                editor.commit();
            } else {
                editor.abort();
            }

        } catch (IOException e) {
            if (editor != null) {
                editor.abort();
            }

            throw e;
        }
    }


    public void put(String key, Object object, boolean serialize) throws IOException {

        if (serialize == false) {
            put(key, object);
            return;
        }

        mMemCache.put(key, object);
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(getHashOf(key));
            if (editor == null) {
                return;
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(editor.newOutputStream(0)));
            mGson.toJson(object, bufferedWriter);
            bufferedWriter.flush();
            bufferedWriter.close();
            editor.commit();
            mDiskCache.flush();

        } catch (IOException e) {
            if (editor != null) {
                editor.abort();
            }

            throw e;
        }

    }

    protected boolean writeValueToCache(String value, DiskLruCache.Editor editor) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(editor.newOutputStream(0));
            outputStream.write(value.getBytes(STRING_ENCODING));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        return true;
    }

    protected String getHashOf(String string) throws UnsupportedEncodingException {
        try {
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(string.getBytes(STRING_ENCODING));
            byte[] digest = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, digest);

            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            return string;
        }
    }


}
