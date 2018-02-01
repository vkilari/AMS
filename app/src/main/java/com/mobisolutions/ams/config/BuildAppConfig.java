package com.mobisolutions.ams.config;

import com.mobisolutions.ams.common.AppCoreConstants;

/**
 * Created by vkilari on 7/17/17.
 */

public class BuildAppConfig extends ConfigHelper{

    private static BuildAppConfig mSharedInstance = null;

    private BuildAppConfig() {
    }

    /**
     * Returns the common configuration instance.
     *
     * @return the singleton instance
     */
    public static synchronized BuildAppConfig getSharedInstance() {
        if (mSharedInstance == null) {
            mSharedInstance = new BuildAppConfig();
            mSharedInstance.setContext(ApplicationContext.getAppContext());
        }

        return mSharedInstance;
    }

    public static boolean isNameValidationEnabled() {
        return getSharedInstance().getBooleanForKey(AppCoreConstants.MCD_NAME_VALIDATION_REGEX_ENABLED);
    }

    public static boolean isAutologinOnPasswordResetRequired() {
        return getSharedInstance().getBooleanForKey(AppCoreConstants.MCD_NAME_IS_RESET_PWD_AUTOLOGIN_REQUIRED);
    }

    public static boolean isMobileEmailLoginAllowed() {
        return getSharedInstance().getBooleanForKey(AppCoreConstants.MCD_EMAIL_MOBILE_LOGIN_REQUIRED);
    }

    public static String getNameValidationRules() {
        return getSharedInstance().getValueForKey(AppCoreConstants.MCD_NAME_VALIDATION_REGEX_RULE);
    }

}
