package com.mobisolutions.ams.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vkilari on 11/30/17.
 */

public class CommonUtils {


    Calendar c;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Date date = new Date();
    public CommonUtils() {

        c = Calendar.getInstance();
    }

    public String getPreviousMonth(){

        c.add(Calendar.MONTH, -1);
        int month = c.get(Calendar.MONTH);

        return  new DateFormatSymbols().getMonths()[month];
    }

    public int getYear(){

        int year = c.get(Calendar.YEAR);

        return year;

    }

    public String getCurrentDate(){

        String currentDate = sdf.format(date);

        return currentDate;

    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isSoftKeyboardShown(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            return true;
        } else {
            return false;
        }
    }

    public void clickToCall(Context mCtx, String mobileNumber) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobileNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(callIntent);
    }


}
