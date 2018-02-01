package com.mobisolutions.ams.common;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * constants class for McD coreApp
 */
public class AppCoreConstants {


    public static final String CONFIG_PASSWORD_RULE = "user_interface_build.password_format_rule";
    public static final String CONFIG_USER_INTERFACE_REGISTRATION = "user_interface_build.registration";
    public static final String CONFIG_USER_INTERFACE_REGISTRATION_PHONE = "phoneNumber";
    public static final String CONFIG_USER_INTERFACE_REGISTRATION_ZIPCODE = "zipCode";
    public static final String AMS_PASSWORD_HINT = "user_interface_build.password_hint";
    public static final String MCD_NAME_VALIDATION_REGEX_ENABLED = "user_interface_build.name.validationEnabled";
    public static final String MCD_NAME_IS_RESET_PWD_AUTOLOGIN_REQUIRED = "user_interface_build.resetPassword.autoLoginRequired";
    public static final String MCD_EMAIL_MOBILE_LOGIN_REQUIRED = "user_interface_build.login.showPhoneOrEmailAsUsername";
    public static final String MCD_NAME_VALIDATION_REGEX_RULE = "user_interface_build.name.validationRule";

    public static final String METHOD_NOT_USED = "Un-used Method";
    public static final String SPACE = " ";
    public static final int TIME_OUT_ACCESSIBILITY_4000 = 4000;
    public static final int TIME_INTERVAL_ACCESSIBILITY = 1000;
    public static final int TIME_OUT_ACCESSIBILITY_500 = 500;

}
