package com.mobisolutions.ams.utils;

/**
 * Created by vkilari on 12/18/17.
 */

public class APIConstants {

    public static final String ERROR = "Error";
    public static final String CODE = "code";
    public static final String OK = "OK";
    public static final String ERRORMESSAGE = "ErrorMessage";
    public static final int UPDATEDB_TIME_INTERVAL = 24;
    public static final int MAX_STORES_COUNT = 20;

    public static final String QUESTIONMARK = "?";
    public static final String AMPRESAND = "&";
    public static final String EQUALTO = "=";
    public static final String SLASH = "/";
    public static final String HIPHEN = " - ";
    public static final String SPACE = " ";
    public static final String ERRORCAPS = "ERROR";
    public static final String UNDERSCORE = "_";
    public static boolean DEBUGGGABLE_MODE=true;

    public static final int GET_SETTINGS_REQUEST_ID=22;
    public static final int GET_CONTACTS_REQUEST_ID=23;
    public static final int GET_BANNERS_REQUEST_ID=24;
    public static final int GET_CATEGORIES_REQUEST_ID=24;
    public static final int UPDATE_CONTACT_REQUEST_ID=25;
    public static final int SEND_AMC_REQUEST_ID=25;
    public static final int GET_AMC_REQUEST_ID=26;
    public static final int GET_MAINTENANCE_REQUEST_ID=24;
    public static final int GET_MEETINGS_REQUEST_ID=24;
    public static final int SEND_MEETINGS_REQUEST_ID=25;

    //API Response codes
    public static final int SUCCESS=200;
    public static final int RECORDS_NOT_AVAILABLE=204;


    //API Service Codes
    public static final int BANNER=101;
    public static final int CATEGORIES=102;
    public static final int CONTACTS=103;
    public static final int MAINTENANCE=104;
    public static final int MEETINGS=105;
    public static final int AMC=106;
    public static final int HOME_SERVICES=107;
}
