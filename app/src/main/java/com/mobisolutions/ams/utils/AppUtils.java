package com.mobisolutions.ams.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.calendar.Calendar;
import com.mobisolutions.ams.common.AppCoreConstants;
import com.mobisolutions.ams.config.ApplicationContext;
import com.mobisolutions.ams.config.BuildAppConfig;
import com.mobisolutions.ams.uikit.widget.CustomEditText;
import com.mobisolutions.ams.uikit.widget.CustomTextView;
import com.mobisolutions.ams.uikit.widget.StandardProgressBar;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vkilari on 7/17/17.
 */

public class AppUtils {

    public static final String TAG = AppUtils.class.getName();
    static String PASSWORD_RULE_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[^\\s]+$";
    private static Map<String, String> appParameters = new HashMap<>();

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final String PASSWORD_LENGTH_REGEX = "(\\{[0-9]+,[0-9]+\\}\\$)";
    private static final String PREF_APP_PARAMETERS = "PREF_APP_PARAMETERS";
    private static int sIndicatorCount = 0;
    static StandardProgressBar progressDialog;
    /**
     * Is this list empty. Checks for null as well as size.
     *
     * @return true if empty, false if not
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }


    public static boolean isEmailValid(String email) {
        return null != email && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        String passwordRules = BuildAppConfig.getSharedInstance().getValueForKey(AppCoreConstants.CONFIG_PASSWORD_RULE);
        // If password regex not set in config, use hardcoded value without length validation.
        if (TextUtils.isEmpty(passwordRules)) {
            passwordRules = PASSWORD_RULE_REGEX;
        }
        passwordRules = getPasswordLengthRules(passwordRules, getMinLength(), getMaxLength());
        return !TextUtils.isEmpty(passwordRules) && !TextUtils.isEmpty(password) && Pattern.compile(passwordRules).matcher(password).matches();
    }

    public static String isPasswordValid(@NonNull Context context, String password) {
        //TO-DO need to add the password validation from Account helper  !isPasswordValid(password)
        String minLength = getMinLength();
        String maxLength = getMaxLength();
        String passwordRules = BuildAppConfig.getSharedInstance().getValueForKey(AppCoreConstants.CONFIG_PASSWORD_RULE);
        passwordRules = getPasswordLengthRules(passwordRules, minLength, maxLength);
        passwordRules = TextUtils.isEmpty(passwordRules) ? PASSWORD_RULE_REGEX : passwordRules;

        boolean passwordLengthRulesNotPresent = TextUtils.isEmpty(passwordRules) || passwordRules.equals(PASSWORD_RULE_REGEX) ||
                TextUtils.isEmpty(minLength) || TextUtils.isEmpty(maxLength);
        String errorMessage = "";
        if (!isPasswordValid(password)) {
            if (passwordLengthRulesNotPresent) {
                errorMessage = context.getString(R.string.error_invalid_password);
            } else {
                String passwordHintConfigKey = BuildAppConfig.getSharedInstance().getValueForKey(AppCoreConstants.AMS_PASSWORD_HINT);
                int passwordHintTextId = context.getResources().getIdentifier(passwordHintConfigKey, "string", context.getPackageName());
                errorMessage = context.getResources().getString(passwordHintTextId, minLength, maxLength);
            }
        }
        return errorMessage;
    }

    /**
     * This method will build the password length rules dynamically from the length defined in eCP and append the rules to the existing
     * password regex rules.
     * If length is not specified, no length rules will be applied.
     * @param minPasswordLength Min Password Length set by eCP.
     * @param maxPasswordLength Max Password Length set by eCP.
     * @return True if given password passes all rules. False otherwise.
     */
    private static String getPasswordLengthRules(String passwordRules, String minPasswordLength, String maxPasswordLength) {
        // Build length rules dynamically from eCP values.
        if (!TextUtils.isEmpty(passwordRules) && !TextUtils.isEmpty(minPasswordLength) && !TextUtils.isEmpty(maxPasswordLength)) {
            StringBuilder lengthBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(minPasswordLength) && !TextUtils.isEmpty(maxPasswordLength)) {
                lengthBuilder.append('{')
                        .append(minPasswordLength)
                        .append(',')
                        .append(maxPasswordLength)
                        .append('}')
                        .append('$');
                return passwordRules.replace("+$", lengthBuilder.toString());
            }
        }

        return passwordRules;
    }


    /**
     * Get the min password length of the password validation rules.
     *
     * @return Min Password Length as a String.
     */
    public static String getMinLength() {
        String minLength = "";
        if (getAppParameters() != null) {
            minLength = getAppParameters().get(MIN_PASSWORD_LENGTH);
            if (TextUtils.isEmpty(minLength)) {
                // TO-DO Can remove this hardcoded key once eCP updates this key to correct spelling.
                minLength = getAppParameters().get("minPasswordLenght");
            }
        }
        if (TextUtils.isEmpty(minLength)) {
            minLength = getDefaultLengthByType(MIN_PASSWORD_LENGTH);
        }
        return minLength;
    }

    /**
     * Get the max password length of the password validation rules.
     *
     * @return Max Password Length as a String.
     */
    public static String getMaxLength() {
        String maxLength = "";
        if (getAppParameters() != null) {
            maxLength = getAppParameters().get(MAX_PASSWORD_LENGTH);
            if (TextUtils.isEmpty(maxLength)) {
                // TO-DO Can remove this hardcoded key once eCP updates this key to correct spelling.
                maxLength = getAppParameters().get("maxPasswordLenght");
            }
        }
        if (TextUtils.isEmpty(maxLength)) {
            maxLength = getDefaultLengthByType(MAX_PASSWORD_LENGTH);
        }
        return maxLength;
    }


    /**
     * Get all the app parameters
     *
     * @return a map with all the app parameters
     */
    public static Map<String, String> getAppParameters() {
        if (!appParameters.isEmpty()) {
            return appParameters;
        } else {
            LocalDataManager dataManager = LocalDataManager.getSharedInstance();
            String map = dataManager.getString(PREF_APP_PARAMETERS, null);
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            Map<String, String> params = new Gson().fromJson(map, type);
            if (params != null) {
                appParameters = params;
            }
            return appParameters;
        }
    }

    /**
     * get default max and min length of password from build configuration file
     *
     * @param lengthType
     * @return value of password length
     */
    private static String getDefaultLengthByType(int lengthType) {
        try {
            String passwordRules = BuildAppConfig.getSharedInstance().getValueForKey(AppCoreConstants.CONFIG_PASSWORD_RULE);
            if (!TextUtils.isEmpty(passwordRules)) {
                Pattern patternMatcher = Pattern.compile(PASSWORD_LENGTH_REGEX);
                Matcher matcher = patternMatcher.matcher(passwordRules);
                if (matcher != null && matcher.find() && !TextUtils.isEmpty(matcher.group(1))) {
                    return getDefaultLength(matcher.group(1), lengthType);
                }
            }
        } catch (IllegalStateException e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * method extract min and max length from length rule
     *
     * @param passwordLengthRule
     * @param lengthType
     * @return
     */
    private static String getDefaultLength(String passwordLengthRule, int lengthType) {
        if (lengthType == MIN_PASSWORD_LENGTH) {
            return passwordLengthRule.substring(passwordLengthRule.indexOf("{") + 1, passwordLengthRule.indexOf(","));
        } else if (lengthType == MAX_PASSWORD_LENGTH) {
            return passwordLengthRule.substring(passwordLengthRule.indexOf(",") + 1, passwordLengthRule.indexOf("}"));
        }
        return null;
    }


    public static String getTrimmedText(Editable input) {
        if (input != null && input.length() > 0) {
            return getTrimmedText(input.toString());
        }
        return "";
    }

    public static String getTrimmedText(String input) {
        String trimmedText = null;
        if (null != input) {
            trimmedText = input.trim();
        }
        return trimmedText;
    }

    public static String getString(byte[] myBytes) throws IOException {
        StringBuilder builder = new StringBuilder(0x10000);
        InputStream inStream = new ByteArrayInputStream(myBytes);
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(
                inStream), 8192);
        String line;
        while ((line = buffReader.readLine()) != null)
            builder.append(line);
        return builder.toString();
    }

    public static int getDeviceWidth(Context mCtx) {
        DisplayMetrics metrics = mCtx.getResources().getDisplayMetrics();
        int devwidth = metrics.widthPixels;
        return devwidth;
    }

    public static int getDecorHeight(Context mCtx) {

        int decorHeight = ((Activity) mCtx).getWindow().getDecorView().getHeight();
        return decorHeight;
    }

    public static boolean isRequestFailure(int statusCode) {
        // TODO Auto-generated method stub
        //BADREQUEST=400,UNAUTHORIZED=401,FORBIDDEN=403,NOTFOUND=404,INTERNALERROR=500,NOTIMPLEMENTED=501,GATEWAYTIMEOUT=503

        //400 BadRequest	InvalidParameter	If the num or page parameters are less than 1
        //	400 BadRequest	InvalidSession	SessionID is not recognized, is closed or is malformed
        //401 Unauthorized	LoginRequired
            /*400 BadRequest	AlreadyLoggedIn	The session is logged in, but this action is only valid for non-logged in users.
            400 BadRequest	BadCredentials	Invalid username/password
			400 BadRequest	InvalidSession	SessionID is not recognized, is closed or is malformed*/

        if (statusCode == ResponseStatusCodes.BADREQUEST || statusCode == ResponseStatusCodes.FORBIDDEN || statusCode == ResponseStatusCodes.NOTFOUND) {
            return true;
        }

        return false;
    }



    /**
     * Check if accessibility and accessibility touch exploration is enabled
     *
     * @return if accessibility and accessibility touch exploration is enabled
     */
    public static boolean isAccessibilitySettingsEnabled() {
        return isAccessibilityEnabled() && isAccessibilityTouchExplorationEnabled();
    }

    /**
     * Check if accessibility touch exploration is enabled
     *
     * @return if accessibility touch exploration is enabled
     */
    public static boolean isAccessibilityTouchExplorationEnabled() {
        Context context = AppContext.getContext();
        if (context == null) {
            return false;
        }
        AccessibilityManager systemService = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        return systemService.isTouchExplorationEnabled();
    }

    /**
     * Check if accessibility is enabled
     *
     * @return if accessibility is enabled
     */
    public static boolean isAccessibilityEnabled() {
        Context context = AppContext.getContext();
        if (context == null) {
            return false;
        }
        AccessibilityManager systemService = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        return systemService.isEnabled();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0 || str.toString().trim().length() == 0;
    }


    /**
     * Enable save button
     */
    public static void enableButton(CustomTextView customTextView) {
        updateButton(customTextView, true,
                R.drawable.gradient_yellow_button_enabled, R.color.ams_white);
    }

    /**
     * Disable save button
     */
    public static void disableButton(CustomTextView customTextView) {
        updateButton(customTextView, false,
                R.drawable.gradient_yellow_button_disabled, R.color.mcd_disabled_button_text_color);
    }


    private static void updateButton(CustomTextView button, boolean setEnabled,
                                     @DrawableRes int buttonBG, @ColorRes int textColor) {

        Context context = AppContext.getAppContext();
        button.setEnabled(setEnabled);
        button.setClickable(setEnabled);
        button.setBackgroundResource(buttonBG);
        button.setTextColor(context.getResources().getColor(textColor));
        int padding = (int) context.getResources().getDimension(R.dimen.ams_button_large_padding);
        button.setPadding(padding, padding, padding, padding);
    }

    /**
     * Validates the phone number
     *
     * @return true if phone is valid
     */
    public static boolean isValidPhone(String phoneNumber, int maxChar) {
        return validateInputMaxLength(phoneNumber, maxChar) && TextUtils.isDigitsOnly(phoneNumber);
    }

    /**
     * Checks the input characters with respect to maximum limit
     *
     * @return true if input is valid
     */
    public static boolean validateInputMaxLength(String inputString, int maxChars) {
        return inputString.length() == maxChars;
    }


    public static String getTrimmedText(CustomEditText input) {
        if (input != null) {
            return getTrimmedText(input.getText());
        }
        return "";
    }

    public static void setErrorBackground(View inputLayout) {
        //Fix for Android Support Library 25.0.0, as it adds extra parent to EditText
        // in the form of Frame Layout
        View parentLayout = (ViewGroup) inputLayout.getParent();
        parentLayout.setBackgroundResource(R.drawable.input_bg_error);
    }


    public static void addErrorMessage(String msg, View errorView, Context context) {
        CustomTextView textView = (CustomTextView) errorView.findViewById(R.id.mcd_error_text);
        textView.setText(msg);
        String errorMsg = context.getString(R.string.acs_error_string) + AppCoreConstants.SPACE + msg;
        announceErrorAccessibility(errorMsg, AppCoreConstants.TIME_OUT_ACCESSIBILITY_4000);
    }

    public static void announceErrorAccessibility(final String message, final int timeOut) {
        announceErrorAccessibility(message, timeOut, AppCoreConstants.TIME_INTERVAL_ACCESSIBILITY);
    }


    public static void announceErrorAccessibility(final String message, final int timeOut, final int timeInterval) {
        if (isAccessibilitySettingsEnabled()) {
            new CountDownTimer(timeOut, timeInterval) {
                public void onTick(long millisUntilFinished) {
                    Log.i(TAG, AppCoreConstants.METHOD_NOT_USED);
                }

                public void onFinish() {

                }
            }.start();
        }
    }

    /**
     * Set the accessibility starting focus to this view
     *
     * @param view     view
     * @param duration time lapse
     */
    public static void sendFocusChangeEvent(final View view, int duration) {
        if (isAccessibilitySettingsEnabled()) {
            new CountDownTimer(duration, AppCoreConstants.TIME_INTERVAL_ACCESSIBILITY) {
                public void onTick(long millisUntilFinished) {
                    Log.i(TAG, AppCoreConstants.METHOD_NOT_USED);
                }

                public void onFinish() {
                    view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                }

            }.start();
        }
    }

    public static boolean isServerInvalidResponse(int statusCode) {
        // TODO Auto-generated method stub
        if (statusCode == 0 || statusCode >= 500) {
            return true;
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        boolean online = false;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
            if (netInfo != null)
                online = netInfo.isConnected();
        }
        return online;
    }


    /**
     * Set the accessibility starting focus to this view
     *
     * @param view view
     */
    public static void sendFocusChangeEvent(final View view) {
        sendFocusChangeEvent(view, AppCoreConstants.TIME_OUT_ACCESSIBILITY_500);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationContext.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean validateInputs(CustomEditText view, int minChars) {
        return view.getText().length() >= minChars && view.getText().toString().trim().length()
                >= minChars;
    }

    /**
     * Show loading progress indicator
     *
     * @param context  activity context
     * @param msgResId message string id
     */
    public static void startActivityIndicator(@NonNull Activity context, @StringRes int msgResId) {
        startActivityIndicator(context, context.getString(msgResId));
    }

    /**
     * Show loading progress indicator
     *
     * @param context activity context
     * @param message message to be displayed while showing loading indicator
     */
    public static void startActivityIndicator(@NonNull Activity context, @NonNull String message) {
        if (sIndicatorCount == 0) {
            /* no Indicators, let's attach one. */
            attachIndicator(context, message);
        }
        // increment the indicator count.
        ++sIndicatorCount;
    }

    /**
     * stop loading processDialog indicator
     */
    public static void stopActivityIndicator() {
        if (sIndicatorCount == 1) {
            /* let's detach the last indicator */
            detachIndicator();
        }
        // decrement the indicator count.
        sIndicatorCount = Math.max(0, --sIndicatorCount);
    }

    private static void attachIndicator(Activity context, String message) {
        if (null != context && !context.isFinishing()) {
            /* fall back */
            detachIndicator();
            progressDialog = new StandardProgressBar(context);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            Log.d(TAG, message);

        }
    }

    private static void detachIndicator() {
        progressDialog.hide();
    }

    public static void stopAllActivityIndicators() {
        sIndicatorCount = 0;
        detachIndicator();
    }

    public static class ResponseStatusCodes {

        public static final int OK = 200, NOTMODIFIED = 304, CREATED = 201, ACCEPTED = 202, PARTIALINFORMATION = 203,
                NORESPONSE = 204, BADREQUEST = 400, UNAUTHORIZED = 401, PAYMENTREQUIRED = 402,
                FORBIDDEN = 403, NOTFOUND = 404, INTERNALERROR = 500, NOTIMPLEMENTED = 501,
                GATEWAYTIMEOUT = 503, MOVED = 301, FOUND = 302, METHOD = 303;
    }

    public final static void debugThrowable(String TAG, Throwable t) {
        if (t != null && APIConstants.DEBUGGGABLE_MODE) {
            Log.e(TAG, "debugThrowable returned error---------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + t.getMessage());

        }
    }

    public final static void debugThrowable(String TAG, Throwable t, int id) {
        if (t != null && APIConstants.DEBUGGGABLE_MODE) {
            Log.e(TAG, id + ": debugThrowable returned error---------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + t.getMessage());

        }
    }

    public static String getCurrentTimeStamp() {
        Timestamp tsTemp = new Timestamp(System.currentTimeMillis());
        String ts = tsTemp.toString();
        return ts;
    }

    public static byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        DebugUtil.debugMessage(TAG, "Bytes Count" + bitmap.getByteCount());
        return bos.toByteArray();
    }

    public static String getAPIUrlAppend(Context mContext, String method) {
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder(0x10000);
        builder.append(ConfigUtils.getOmniServiceUrl(mContext));
        builder.append(APIConstants.SLASH);
        builder.append(method);
        return builder.toString();

    }

    public static Date stringToDate(String date){

        Date date1 = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            date1 = dateFormat.parse(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return date1;

    }

    public static String getCurrentDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

       String currentDate = dateFormat.format(new Date());

       return currentDate;

    }

    public static String getYear(String date){
        //System.out.println("---------calendar event calendarMap.containsKey(year)::");


        SimpleDateFormat dateFormat = new SimpleDateFormat(

                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String year="";

        try {

            Date date1 = dateFormat.parse(date);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");

            year = df.format(date1);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return year;
    }

    public static HashMap<String, List<Calendar>> getMonths(String date, String year, List<Calendar> dates, String title, String type, String desc, String meetingDate, HashMap<String, List<Calendar>> mapMonths){



        SimpleDateFormat dateFormat = new SimpleDateFormat(

                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {


            Date date1 = dateFormat.parse(date);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
            DateFormat displayDate = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat time = new SimpleDateFormat("HH:mm:ss");

            String yearFromDate = df1.format(date1);

            if(yearFromDate.equalsIgnoreCase(year)) {
                Calendar calendar = new Calendar();
                calendar.setCalendarTitle(title);
                calendar.setCalendarType(type);
                calendar.setCalendarDescription(desc);
                calendar.setCalendarDate(displayDate.format(date1));
                calendar.setCalendarTime(time.format(date1));

                dates.add(calendar);


                mapMonths.put(year, dates);

            }


        }catch (Exception e) {
            e.printStackTrace();
        }


        return mapMonths;

    }

    static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

}
