package com.mobisolutions.ams;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.database.DBHelper;
import com.mobisolutions.ams.utils.AppContext;

/**
 * Created by vkilari on 8/8/17.
 */

public class AMSApplication extends Application {

    private static final String TAG = AMSApplication.class.getName();

    private static  Context mContext;
    private static DBHelper dbHelper;
    private static AMSApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.d(TAG, "------AMSApplication onCreate called");
        dbHelper = new DBHelper(this);

        dbHelper.getWritableDatabase();

        AppContext.init(getApplicationContext());


    }

    public static AMSApplication getInstance() {
        return sInstance;
    }

    public static Context getContext() {
        return  mContext;

    }

    public static DBHelper getDBHelper() {
        return  dbHelper;

    }




}
