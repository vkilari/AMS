package com.mobisolutions.ams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.database.DBHelper;
import com.mobisolutions.ams.home.CategoriesBean;
import com.mobisolutions.ams.home.HomeActivity;
import com.mobisolutions.ams.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by vkilari on 7/16/17.
 */

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getName();

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    AMSApplication application;
    DBHelper dbHelper;


    private Context mCtx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        mCtx = this;
        application = AMSApplication.getInstance();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);



    }




}
