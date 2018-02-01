package com.mobisolutions.ams.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobisolutions.ams.AMSApplication;
import com.mobisolutions.ams.amc.AMC;
import com.mobisolutions.ams.calendar.Calendar;
import com.mobisolutions.ams.config.Settings;
import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.grocery.GroceryCategoriesBean;
import com.mobisolutions.ams.grocery.GroceryItemsBean;
import com.mobisolutions.ams.home.Banners;
import com.mobisolutions.ams.home.CategoriesBean;
import com.mobisolutions.ams.services.ServiceItemBean;
import com.mobisolutions.ams.services.ServiceProviderBean;
import com.mobisolutions.ams.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by vkilari on 12/14/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getName();

    private Context mContext;
    private String contactJson = "json/contacts.json";
    private String categoryJson = "json/home_categories.json";
    private String settingsJson = "json/api_services.json";
    private String amcJson = "json/amc.json";
    private String calendarJson = "json/calendar.json";
    private String groceryJson = "json/grocery_master_list.json";
    private String servicesJson = "json/services.json";

    AMSApplication application;
    DBHelper dbHelper;
    SQLiteDatabase db;

    //database name
    public static final String DATABASE_NAME = "ams.db";

    private static final int DATABASE_VERSION = 1;

    //tables
    public static final String CONTACTS_TABLE = "contacts";
    public static final String HOMEPAGE_CATEGORIES_TABLE = "categories";
    public static final String SERVICES_TABLE = "services";
    public static final String SERVICE_PROVIDERS_TABLE = "service_providers";
    public static final String MAINTENANCE_TABLE = "maintenance";
    public static final String MAINTENANCE_EXPENDITURES_TABLE = "maintenance_expenditure_details";
    public static final String CALENDAR_TABLE = "calendar";
    public static final String BANNERS_TABLE = "banners";
    public static final String SETTINGS_TABLE = "settings";
    public static final String AMC_TABLE = "amc";

    private static final String TABLE_CUSTOM_GROCERY_LIST = "table_custom_grocery_list";
    private static final String TABLE_CATEGORIES = "table_categories";
    private static final String TABLE_CATEGORIES_ITEMS = "table_categories_items";
    private static final String TABLE_USER_CHOICE_LIST = "table_user_choice_list";
    private static final String TABLE_ITEM_MEASUREDIN = "Table_item_measuredin";

    //settings table columns
    public static final String SETTING_ID = "service_id";
    public static final String SETTING_SERVICE_NAME = "service_name";
    public static final String SETTING_SERVICE_STATUS = "service_status";


    //AMC table columns
    public static final String SL_NO = "sl_no";
    public static final String AMC_ID = "amc_id";
    public static final String AMC_NAME = "amc_name";

    //contact table columns
    public static final String CONTACTS_ID = "contact_id";
    public static final String CONTACTS_NAME = "contact_name";
    public static final String CONTACTS_EMAIL = "contact_email";
    public static final String CONTACTS_IS_OWNER = "is_owner";
    public static final String CONTACTS_ADDRESS = "contact_address";
    public static final String CONTACTS_PHONE = "contact_phone";
    public static final String CONTACTS_FLAT_NO = "contact_flat_no";
    public static final String CONTACTS_PHOTO = "contact_photo";

    //categories table columns
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_IMAGE = "category_image";
    public static final String CATEGORY_COLOR = "category_color";

    //services table columns
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_NAME = "service_name";
    public static final String SERVICE_IMAGE = "service_image";
    public static final String SERVICE_DESCRIPTION = "service_desc";
    public static final String SERVICE_REGION = "service_region";

    //service_providers table columns
    public static final String SERVICE_PROVIDER_ID = "provider_id";
    public static final String SERVICE_PROVIDER_NAME = "provider_name";
    public static final String SERVICE_PROVIDER_IMAGE = "provider_image";
    public static final String SERVICE_PROVIDER_ADDRESS = "provider_address";
    public static final String SERVICE_PROVIDER_MOBILE = "provider_mobile";
    public static final String SERVICE_PROVIDER_EMAIL = "provider_email";
    public static final String SERVICE_PROVIDER_REGION = "provider_region";

    //maintenance table columns
    public static final String MAINTENANCE_ID = "maintenance_id";
    public static final String MAINTENANCE_NAME = "maintenance_name";
    public static final String MAINTENANCE_STATUS = "maintenance_status";


    //maintenance_expenditure_details table columns
    public static final String EXPENDITURE_ID = "expenditure_id";
    public static final String EXPENDITURE_NAME = "expenditure_name";
    public static final String EXPENDITURE_COMMENTS = "expenditure_comments";
    public static final String EXPENDITURE_AMOUNT = "expenditure_amount";

    //calendar table columns
    public static final String CALENDAR_ID = "calendar_id";
    public static final String APARTMENT_ID = "apartment_id";
    public static final String CALENDAR_TYPE = "calendar_type";
    public static final String CALENDAR_DATE = "date";
    public static final String CALENDAR_TITLE = "title";
    public static final String CALENDAR_DESC = "description";


    //banners table columns
    public static final String BANNER_ID = "banner_id";
    public static final String BANNER_NAME = "banner_name";
    public static final String BANNER_TYPE = "banner_type";
    public static final String BANNER_PROVIDER = "banner_provider";
    public static final String PROMOTION_START = "promotion_start";
    public static final String PROMOTION_END = "promotion_end";
    public static final String BANNER_IMAGE = "banner_image";
    public static final String BANNER_DETAILS = "banner_details";

    //Grocery
    private static final String GROCERY_ID = "Grocery_id";
    private static final String GROCERY_NAME = "Grocery_name";
    private static final String USER_CHOICE_CATEGORY_ID = "user_choice_category_id";
    private static final String USER_CHOICE_ITEM_ID = "user_choice_item_id";

    private static final String GROCERY_CATEGORY_ID = "Category_id";
    private static final String GROCERY_CATEGORY_NAME = "Catogory_name";

    private static final String CATEGORY_ITEM_ID = "Catogory_item_id";
    private static final String ITEM_CATEGORY_ID = "Category_id";
    private static final String CATEGORY_ITEM_NAME = "Catogory_item_name";
    private static final String CATEGORY_ITEM_MESUREDIN = "Category_item_measuredin";

    private static final String CHOICE_CATEGORY_ITEM_ID = "choice_catogory_item_id";
    private static final String CHOICE_ITEM_CATEGORY_ID = "chioce_item_category_id";
    private static final String CHOICE_CATEGORY_ITEM_NAME = "choice_category_item_name";
    private static final String USER_CHOICE_ITEM_MEASUREDIN = "choice_item_measuredin";
    private static final String USER_CHOICE_ITEM_QUANTITY = "choice_item_quantity";
    private static final String CHOICE_CREATED_DATE = "choice_created_date";
    private static final String CHOICE_MODIFIED_DATE = "choice_modified_date";

    private static final String MEASURE_ID = "measure_id";
    private static final String MEASURE_NAME = "Measure_name";
    private static final String MEASURE_TYPE = "Measure_type";

    public static final String CREATED_DATE = "created_date";
    public static final String MODIFIED_DATE = "modified_date";

    private static ArrayList<GroceryCategoriesBean> categoriesList = new ArrayList<>();
    private static ArrayList<GroceryItemsBean> categoriesItemList = new ArrayList<>();

    ArrayList<GroceryItemsBean> categoryItemsBeanList;
    HashMap<GroceryCategoriesBean, ArrayList<GroceryItemsBean>> fetchMap;
    HashMap<GroceryCategoriesBean, ArrayList<GroceryItemsBean>> fetchCustomListMap;
    private HashMap<GroceryCategoriesBean, ArrayList<GroceryItemsBean>> groceryMap = new HashMap<>();
    Context context;

    String CREATE_SETTINGS_TABLE = "CREATE TABLE " + SETTINGS_TABLE + "("
            + SETTING_ID + " INTEGER PRIMARY KEY," + SETTING_SERVICE_NAME + " TEXT,"
            + SETTING_SERVICE_STATUS + " TEXT," +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP"  + ")";

    String CREATE_AMC_TABLE = "CREATE TABLE " + AMC_TABLE + "("
            + SL_NO + " INTEGER PRIMARY KEY," + AMC_ID + " TEXT,"
            + AMC_NAME + " TEXT," +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP"  + ")";


    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE + "("
            + CONTACTS_ID + " INTEGER PRIMARY KEY," + CONTACTS_NAME + " TEXT,"
            + CONTACTS_EMAIL + " TEXT," + CONTACTS_IS_OWNER + " TEXT," +CONTACTS_ADDRESS + " TEXT," +CONTACTS_PHONE + " TEXT," +CONTACTS_FLAT_NO + " TEXT," +CONTACTS_PHOTO + " TEXT," +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP"  + ")";

    String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + HOMEPAGE_CATEGORIES_TABLE + "("
            + CATEGORY_ID + " INTEGER PRIMARY KEY," + CATEGORY_NAME + " TEXT,"
            + CATEGORY_IMAGE + " TEXT," +CATEGORY_COLOR + " TEXT,"  +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP" + ")";

    String CREATE_SERVICES_TABLE = "CREATE TABLE " + SERVICES_TABLE + "("
            + SERVICE_ID + " TEXT," + SERVICE_NAME + " TEXT,"
            + SERVICE_IMAGE + " TEXT," +SERVICE_DESCRIPTION + " TEXT," +SERVICE_REGION + " TEXT," +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP" + ")";

    String CREATE_SERVICE_PROVIDERS_TABLE = "CREATE TABLE " + SERVICE_PROVIDERS_TABLE + "("
            + SERVICE_PROVIDER_ID + " INTEGER PRIMARY KEY," + SERVICE_ID + " TEXT," +SERVICE_PROVIDER_NAME + " TEXT," +SERVICE_PROVIDER_IMAGE + " TEXT," +SERVICE_PROVIDER_ADDRESS + " TEXT," +SERVICE_PROVIDER_MOBILE + " TEXT," +SERVICE_PROVIDER_EMAIL + " TEXT," +SERVICE_PROVIDER_REGION + " TEXT," +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP" + ")";

    String CREATE_MAINTENANCE_TABLE = "CREATE TABLE " + MAINTENANCE_TABLE + "("
            + MAINTENANCE_ID + " INTEGER PRIMARY KEY," + MAINTENANCE_NAME + " TEXT,"
            + MAINTENANCE_STATUS + " TEXT" + ")";

    String CREATE_MAINTENANCE_EXPENDITURES_TABLE = "CREATE TABLE " + MAINTENANCE_EXPENDITURES_TABLE + "("
            + EXPENDITURE_ID + " INTEGER PRIMARY KEY," + EXPENDITURE_NAME + " TEXT,"
            + EXPENDITURE_COMMENTS + " TEXT," +EXPENDITURE_AMOUNT + " TEXT" + ")";

    String CREATE_CALENDAR_TABLE = "CREATE TABLE " + CALENDAR_TABLE + "("
            + CALENDAR_ID + " INTEGER PRIMARY KEY," + APARTMENT_ID + " TEXT," + CALENDAR_TYPE + " TEXT,"
            + CALENDAR_DATE + " DATETIME," + CALENDAR_TITLE + " TEXT," +CALENDAR_DESC + " TEXT," +CREATED_DATE + " DEFAULT CURRENT_TIMESTAMP,"+MODIFIED_DATE + " DEFAULT CURRENT_TIMESTAMP"+ ")";

    String CREATE_BANNERS_TABLE = "CREATE TABLE " + BANNERS_TABLE + "("
            + BANNER_ID + " INTEGER PRIMARY KEY," + BANNER_NAME + " TEXT,"
            + BANNER_TYPE + " TEXT," +BANNER_PROVIDER + " TEXT," +PROMOTION_START + " TEXT," +PROMOTION_END + " TEXT," +BANNER_IMAGE + " TEXT," +BANNER_DETAILS + " TEXT" + ")";

    private String CREATE_TABEL_GROCERY_LIST = "create table " + TABLE_CUSTOM_GROCERY_LIST + " ("
            + GROCERY_CATEGORY_ID + " INTEGER PRIMARY KEY, "
            + GROCERY_CATEGORY_NAME + " TEXT, "
            + USER_CHOICE_CATEGORY_ID + " TEXT, "
            + USER_CHOICE_ITEM_ID + " TEXT, "
            + USER_CHOICE_ITEM_MEASUREDIN + " TEXT, "
            + USER_CHOICE_ITEM_QUANTITY + " TEXT, "
            + CREATED_DATE + " DATETIME, "
            + MODIFIED_DATE + " DATETIME" + ")";
    private String CREATE_TABLE_CATEGORIES = "create table " + TABLE_CATEGORIES + " ("
            // + "SLNo " + "INTEGER PRIMARY KEY, "
            + GROCERY_CATEGORY_ID + " INTEGER PRIMARY KEY, "
            + GROCERY_CATEGORY_NAME + " TEXT, "
            + CREATED_DATE + " DATETIME, "
            + MODIFIED_DATE + " DATETIME" + ")";
    private String CREATE_TABLE_CATEGORIES_ITEMS = "create table " + TABLE_CATEGORIES_ITEMS + " ("
            + CATEGORY_ITEM_ID + " INTEGER PRIMARY KEY, "
            + ITEM_CATEGORY_ID + " INTEGER, "
            + CATEGORY_ITEM_NAME + " TEXT, "
            + CATEGORY_ITEM_MESUREDIN + " TEXT, "
            + CREATED_DATE + " DATETIME, "
            + MODIFIED_DATE + " DATETIME" + ")";
    private String CREATE_TABLE_USER_CHIOCE_LIST = "create table " + TABLE_USER_CHOICE_LIST + " ("
            + CHOICE_CATEGORY_ITEM_ID + " INTEGER PRIMARY KEY, "
            + CHOICE_ITEM_CATEGORY_ID + " INTEGER, "
            + CHOICE_CATEGORY_ITEM_NAME + " TEXT, "
            + USER_CHOICE_ITEM_MEASUREDIN + " TEXT, "
            + CHOICE_CREATED_DATE + " DATETIME, "
            + CHOICE_MODIFIED_DATE + " DATETIME" + ")";
    private String CREATE_TABLE_ITEM_MEASUREDIN = "create table " + TABLE_ITEM_MEASUREDIN + " ("
            + MEASURE_ID + " INTEGER, "
            + MEASURE_NAME + " TEXT, "
            + MEASURE_TYPE + " TEXT, "
            + CREATED_DATE + " DATETIME, "
            + MODIFIED_DATE + " DATETIME" + ")";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        mContext =context;
        application = AMSApplication.getInstance();

        Log.d(TAG, "------DBHelper called");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        Log.d(TAG, "------DBHelper onCreate called");

        db = database;

        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_AMC_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_SERVICES_TABLE);
        db.execSQL(CREATE_SERVICE_PROVIDERS_TABLE);
        db.execSQL(CREATE_MAINTENANCE_TABLE);
        db.execSQL(CREATE_MAINTENANCE_EXPENDITURES_TABLE);
        db.execSQL(CREATE_CALENDAR_TABLE);
        db.execSQL(CREATE_BANNERS_TABLE);
        db.execSQL(CREATE_TABEL_GROCERY_LIST);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_CATEGORIES_ITEMS);
        db.execSQL(CREATE_TABLE_USER_CHIOCE_LIST);
        db.execSQL(CREATE_TABLE_ITEM_MEASUREDIN);

        try {

            JSONObject categoryJsonObject = new JSONObject(loadJSONFromAsset(categoryJson));
            ArrayList<CategoriesBean> categoryArrayList = parseCategoryJson(categoryJsonObject);

            JSONObject contactsJsonObject = new JSONObject(loadJSONFromAsset(contactJson));
            ArrayList<Contacts> contactsArrayList = parseContactsJson(contactsJsonObject);

            JSONObject settingsJsonObject = new JSONObject(loadJSONFromAsset(settingsJson));
            ArrayList<Settings> settingsArrayList = parseSettingsJson(settingsJsonObject);

            JSONObject amcJsonObject = new JSONObject(loadJSONFromAsset(amcJson));
            ArrayList<AMC> amcArrayList = parseAMCJson(amcJsonObject);

            JSONObject calendarJsonObject = new JSONObject(loadJSONFromAsset(calendarJson));
            ArrayList<Calendar> calendarArrayList = parseCalendarJson(calendarJsonObject);

            JSONObject servicesJsonObject = new JSONObject(loadJSONFromAsset(servicesJson));
            List<ServiceItemBean> servicesArrayList = parseServicesJson(servicesJsonObject);

            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(groceryJson));
            insertCategoryListItems(parseMasterJson(jsonObject), db);


            insertContact(contactsArrayList, db);
            insertCategories(categoryArrayList, db);
            insertSettings(settingsArrayList, db);
            insertAMC(amcArrayList, db);
            insertCalendar(calendarArrayList, db);
            insertServices(servicesArrayList, db);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+SETTINGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+AMC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+HOMEPAGE_CATEGORIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SERVICES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SERVICE_PROVIDERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+MAINTENANCE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+MAINTENANCE_EXPENDITURES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CALENDAR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+BANNERS_TABLE);

        onCreate(db);
    }



    public  boolean insertSettings(ArrayList<Settings> settingsList, SQLiteDatabase db) {

        Log.d(TAG, "-------is settings settingsList::"+settingsList.size());
        for (int i = 0; i<settingsList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SETTING_ID, settingsList.get(i).getSettingID());
            contentValues.put(SETTING_SERVICE_NAME, settingsList.get(i).getServiceName());
            contentValues.put(SETTING_SERVICE_STATUS, settingsList.get(i).getServiceStatus());
            contentValues.put(CREATED_DATE, settingsList.get(i).getServiceUpdatedDate());
            contentValues.put(MODIFIED_DATE, settingsList.get(i).getServiceUpdatedDate());

            long settingsInsert = db.insert(SETTINGS_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted Settings::"+settingsInsert);


        }


        return true;
    }

    public void deleteHomeServices() {

        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+SERVICES_TABLE;
        db.execSQL(dropTable);

    }

    public ArrayList<ServiceItemBean> getHomeServices() {

        ArrayList<ServiceItemBean> servicesList = new ArrayList<ServiceItemBean>();

        String query = "select * from "+SERVICES_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ServiceItemBean serviceItemBean = new ServiceItemBean();
            serviceItemBean.setServiceId(res.getString(res.getColumnIndex(SERVICE_ID)));
            serviceItemBean.setServiceName(res.getString(res.getColumnIndex(SERVICE_NAME)));
            serviceItemBean.setServiceImage(res.getString(res.getColumnIndex(SERVICE_IMAGE)));
            serviceItemBean.setServiceDescription(res.getString(res.getColumnIndex(SERVICE_DESCRIPTION)));
            servicesList.add(serviceItemBean);
            res.moveToNext();

        }
        return servicesList;


    }

    public  boolean insertHomeServices(ArrayList<ServiceItemBean> homeServicesList, SQLiteDatabase db) {

        Log.d(TAG, "-------is settings settingsList::"+homeServicesList.size());
        for (int i = 0; i<homeServicesList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(APARTMENT_ID, "sssn101");
            contentValues.put(SERVICE_NAME, homeServicesList.get(i).getServiceName());
            contentValues.put(SERVICE_IMAGE, homeServicesList.get(i).getServiceImage());
            contentValues.put(SERVICE_DESCRIPTION, homeServicesList.get(i).getServiceDescription());
            contentValues.put(CREATED_DATE, AppUtils.getCurrentDate());
            contentValues.put(MODIFIED_DATE, AppUtils.getCurrentDate());

            long amcInsert = db.insert(CALENDAR_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted Calendar::"+amcInsert);

        }


        return true;
    }



    public ArrayList<Settings> getSettings() {

        ArrayList<Settings> settingsList = new ArrayList<Settings>();

        String query = "select * from "+SETTINGS_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Settings settingsBean = new Settings();
            settingsBean.setSettingID(res.getString(res.getColumnIndex(SETTING_ID)));
            settingsBean.setServiceName(res.getString(res.getColumnIndex(SETTING_SERVICE_NAME)));
            settingsBean.setServiceStatus(res.getString(res.getColumnIndex(SETTING_SERVICE_STATUS)));
            settingsBean.setServiceUpdatedDate(res.getString(res.getColumnIndex(MODIFIED_DATE)));
            settingsList.add(settingsBean);
            res.moveToNext();

        }
        return settingsList;


    }

    public void deleteServices() {

        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+SERVICES_TABLE;
        db.execSQL(dropTable);

    }
    public void deleteServiceProviders() {

        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+SERVICE_PROVIDERS_TABLE;
        db.execSQL(dropTable);

    }



    public  boolean insertServices(List<ServiceItemBean> servicesList, SQLiteDatabase db) {

        Log.d(TAG, "-------is settings settingsList::"+servicesList.size());
        for (int i = 0; i<servicesList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SERVICE_ID, servicesList.get(i).getServiceId());
            contentValues.put(SERVICE_NAME, servicesList.get(i).getServiceName());
            contentValues.put(SERVICE_IMAGE, servicesList.get(i).getServiceImage());
            contentValues.put(SERVICE_DESCRIPTION, servicesList.get(i).getServiceDescription());
            contentValues.put(SERVICE_REGION, servicesList.get(i).getServiceRegion());
            contentValues.put(CREATED_DATE, AppUtils.getCurrentDate());
            contentValues.put(MODIFIED_DATE, AppUtils.getCurrentDate());
           List<ServiceProviderBean> providerBeanList = servicesList.get(i).getProviderList();
            for (int j = 0; j<providerBeanList.size(); j++) {
                ContentValues providerValues = new ContentValues();
                providerValues.put(SERVICE_ID, servicesList.get(i).getServiceId());
                providerValues.put(SERVICE_PROVIDER_NAME, providerBeanList.get(i).getProviderName());
                providerValues.put(SERVICE_PROVIDER_IMAGE, providerBeanList.get(i).getProviderEmail());
                providerValues.put(SERVICE_PROVIDER_ADDRESS, providerBeanList.get(i).getProviderAddress());
                providerValues.put(SERVICE_PROVIDER_MOBILE, providerBeanList.get(i).getProviderMobile());
                providerValues.put(SERVICE_PROVIDER_EMAIL, providerBeanList.get(i).getProviderEmail());
                providerValues.put(SERVICE_PROVIDER_REGION, providerBeanList.get(i).getServiceRegion());
                providerValues.put(CREATED_DATE, AppUtils.getCurrentDate());
                providerValues.put(MODIFIED_DATE, AppUtils.getCurrentDate());

                long providerInsert = db.insert(SERVICE_PROVIDERS_TABLE, null, providerValues);

            }

            long serviceInsert = db.insert(SERVICES_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted Calendar::"+serviceInsert);

        }


        return true;
    }


    public void deleteCalendarMeetings() {

        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+CALENDAR_TABLE;
        db.execSQL(dropTable);

    }

    public  boolean insertCalendar(ArrayList<Calendar> calendarList, SQLiteDatabase db) {

        Log.d(TAG, "-------is settings settingsList::"+calendarList.size());
        for (int i = 0; i<calendarList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(APARTMENT_ID, "sssn101");
            contentValues.put(CALENDAR_TYPE, calendarList.get(i).getCalendarType());
            contentValues.put(CALENDAR_DATE, calendarList.get(i).getCalendarDate());
            contentValues.put(CALENDAR_TITLE, calendarList.get(i).getCalendarTitle());
            contentValues.put(CALENDAR_DESC, calendarList.get(i).getCalendarDescription());
            contentValues.put(CREATED_DATE, AppUtils.getCurrentDate());
            contentValues.put(MODIFIED_DATE, AppUtils.getCurrentDate());

            long amcInsert = db.insert(CALENDAR_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted Calendar::"+amcInsert);

        }


        return true;
    }

    public ArrayList<Calendar> getCalendarEvents() {

        ArrayList<Calendar> calendarList = new ArrayList<Calendar>();

        String query = "select * from "+CALENDAR_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Calendar calendarBean = new Calendar();
            calendarBean.setCalendarTitle(res.getString(res.getColumnIndex(CALENDAR_TITLE)));
            calendarBean.setCalendarDate(res.getString(res.getColumnIndex(CALENDAR_DATE)));
            calendarBean.setCalendarType(res.getString(res.getColumnIndex(CALENDAR_TYPE)));
            calendarBean.setCalendarDescription(res.getString(res.getColumnIndex(CALENDAR_DESC)));
            calendarList.add(calendarBean);
            res.moveToNext();

        }
        return calendarList;


    }

    public  boolean insertAMC(ArrayList<AMC> amcList, SQLiteDatabase db) {

        Log.d(TAG, "-------is settings settingsList::"+amcList.size());
        for (int i = 0; i<amcList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AMC_ID, amcList.get(i).getAmcID());
            contentValues.put(AMC_NAME, amcList.get(i).getAmcName());
            contentValues.put(CREATED_DATE, amcList.get(i).getAmcUpdatedDate());
            contentValues.put(MODIFIED_DATE, amcList.get(i).getAmcUpdatedDate());

            long amcInsert = db.insert(AMC_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted AMC::"+amcInsert);


        }


        return true;
    }

    public ArrayList<AMC> getAMCs() {

        ArrayList<AMC> amcList = new ArrayList<AMC>();

        String query = "select * from "+AMC_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            AMC amcBean = new AMC();
            amcBean.setAmcID(res.getString(res.getColumnIndex(AMC_ID)));
            amcBean.setAmcName(res.getString(res.getColumnIndex(AMC_NAME)));
            amcBean.setAmcUpdatedDate(res.getString(res.getColumnIndex(MODIFIED_DATE)));
            amcList.add(amcBean);
            res.moveToNext();

        }
        return amcList;


    }

    public void deleteAMC(){
        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+AMC_TABLE;
        db.execSQL(dropTable);

    }


    public boolean updateSettings(int settingId, String updateDate){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MODIFIED_DATE, updateDate);
        int isUpdated = db.update(SETTINGS_TABLE, contentValues, "service_id = ? ", new String[] { Integer.toString(settingId) } );

        Log.d(TAG, "-------is setting table updated::"+isUpdated);
       return true;

    }

    public boolean insertBanners (ArrayList<Banners> bannersList) {

        Log.d(TAG, "-------inserted banners list size"+bannersList.size());

        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i<bannersList.size(); i++) {
            Log.d(TAG, "-------inserted banners name::::"+bannersList.get(i).getBannerName());
            ContentValues contentValues = new ContentValues();
           // contentValues.put(BANNER_ID, bannersList.get(i).getBanner_Id());
            contentValues.put(BANNER_NAME, bannersList.get(i).getBannerName());
            contentValues.put(BANNER_TYPE, bannersList.get(i).getBannerType());
            contentValues.put(BANNER_PROVIDER, bannersList.get(i).getBannerProvider());
            contentValues.put(PROMOTION_START, bannersList.get(i).getPromotionStart());
            contentValues.put(PROMOTION_END, bannersList.get(i).getPromotionEnd());
            contentValues.put(BANNER_IMAGE, bannersList.get(i).getBannerImage());
            contentValues.put(BANNER_DETAILS, bannersList.get(i).getBannerDetails());
            //contentValues.put(CREATED_DATE, getDateTime());
            //contentValues.put(MODIFIED_DATE, getDateTime());

            long bannersInsert = db.insert(BANNERS_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted Banners::"+bannersInsert);


        }

        return true;

    }

    public void deleteBanners(){
        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+BANNERS_TABLE;
        db.execSQL(dropTable);

    }

    public ArrayList<Banners> getAllBanners() {

        ArrayList<Banners> bannersList = new ArrayList<Banners>();

        String query = "select * from "+BANNERS_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            Banners banners = new Banners();
            banners.setBanner_Id(res.getString(res.getColumnIndex(BANNER_ID)));
            banners.setBannerName(res.getString(res.getColumnIndex(BANNER_NAME)));
            banners.setBannerImage(res.getString(res.getColumnIndex(BANNER_IMAGE)));
            banners.setPromotionStart(res.getString(res.getColumnIndex(PROMOTION_START)));
            banners.setPromotionEnd(res.getString(res.getColumnIndex(PROMOTION_END)));
            banners.setBannerType(res.getString(res.getColumnIndex(BANNER_TYPE)));
            banners.setBannerDetails(res.getString(res.getColumnIndex(BANNER_DETAILS)));
            banners.setBannerProvider(res.getString(res.getColumnIndex(BANNER_PROVIDER)));


            bannersList.add(banners);
            res.moveToNext();

        }
        return bannersList;


    }

    public boolean insertCategories (ArrayList<CategoriesBean> categoriesList, SQLiteDatabase db) {
      //  SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i<categoriesList.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CATEGORY_NAME, categoriesList.get(i).getCategoryName());
            contentValues.put(CATEGORY_IMAGE, categoriesList.get(i).getCategoryImage());
            contentValues.put(CATEGORY_COLOR, categoriesList.get(i).getCategoryColor());
            contentValues.put(CREATED_DATE, getDateTime());
            contentValues.put(MODIFIED_DATE, getDateTime());

            long categoriesInsert = db.insert(HOMEPAGE_CATEGORIES_TABLE, null, contentValues);

            Log.d(TAG, "-------Inserted Categories::"+categoriesInsert);
        }

        return true;
    }

    public ArrayList<CategoriesBean> getAllCategories() {
        ArrayList<CategoriesBean> categoriesList = new ArrayList<CategoriesBean>();

        String query = "select * from "+HOMEPAGE_CATEGORIES_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            CategoriesBean categoriesBean = new CategoriesBean();
            categoriesBean.setCategoryName(res.getString(res.getColumnIndex(CATEGORY_NAME)));
            categoriesBean.setCategoryImage(res.getString(res.getColumnIndex(CATEGORY_IMAGE)));
            categoriesBean.setCategoryColor(res.getString(res.getColumnIndex(CATEGORY_COLOR)));

            categoriesList.add(categoriesBean);
            res.moveToNext();

        }
        return categoriesList;
    }

    public void deleteCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+HOMEPAGE_CATEGORIES_TABLE;
        db.execSQL(dropTable);

    }


    public boolean insertContact (ArrayList<Contacts> contactsList, SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i<contactsList.size(); i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_NAME, contactsList.get(i).getName());
            contentValues.put(CONTACTS_EMAIL, contactsList.get(i).getEmail());
            contentValues.put(CONTACTS_IS_OWNER, contactsList.get(i).getIsOwener());
            contentValues.put(CONTACTS_ADDRESS, contactsList.get(i).getAddress());
            contentValues.put(CONTACTS_PHONE, contactsList.get(i).getMobile());
            contentValues.put(CONTACTS_FLAT_NO, contactsList.get(i).getFlatNo());
            contentValues.put(CONTACTS_PHOTO, contactsList.get(i).getPhoto());
            contentValues.put(CREATED_DATE, getDateTime());
            contentValues.put(MODIFIED_DATE, getDateTime());

           long contactInsert = db.insert(CONTACTS_TABLE, null, contentValues);

           Log.d(TAG, "-------Inserted Contacts::"+contactInsert);
        }

        return true;
    }

    public void deleteContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        String dropTable = "DELETE FROM "+CONTACTS_TABLE;
        db.execSQL(dropTable);

    }



    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Contacts> getAllCotacts() {
        ArrayList<Contacts> contactList = new ArrayList<Contacts>();

        String query = "select * from "+CONTACTS_TABLE;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( query, null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            Contacts contacts = new Contacts();
            contacts.setName(res.getString(res.getColumnIndex(CONTACTS_NAME)));
            contacts.setEmail(res.getString(res.getColumnIndex(CONTACTS_EMAIL)));
            contacts.setIsOwener(res.getString(res.getColumnIndex(CONTACTS_IS_OWNER)));
            contacts.setMobile(res.getString(res.getColumnIndex(CONTACTS_PHONE)));
            contacts.setFlatNo(res.getString(res.getColumnIndex(CONTACTS_FLAT_NO)));
            contacts.setAddress(res.getString(res.getColumnIndex(CONTACTS_ADDRESS)));
            contactList.add(contacts);
            res.moveToNext();

        }
        return contactList;
    }


    public ArrayList<CategoriesBean> parseCategoryJson(JSONObject categoryJson) {

        ArrayList<CategoriesBean> categoryArrayList = new ArrayList<>();
        try {

            JSONArray categoryJsonJSONArray = categoryJson.getJSONArray("categories");

            for (int i = 0; i < categoryJsonJSONArray.length(); i++) {
                CategoriesBean categoriesBean = new CategoriesBean();
                JSONObject categoryObject = categoryJsonJSONArray.getJSONObject(i);
                categoriesBean.setCategoryName(categoryObject.getString("name"));
                categoriesBean.setCategoryImage(categoryObject.getString("image"));
                categoriesBean.setCategoryColor(categoryObject.getString("color"));


                categoryArrayList.add(categoriesBean);
            }
        }catch (Exception e) {
            Log.e(TAG, "Error while parsing contacts JSON"+e.getMessage());
        }

        return categoryArrayList;

    }


    public ArrayList<Settings> parseSettingsJson(JSONObject settingsJson) {

        ArrayList<Settings> settingsArrayList = new ArrayList<>();
        try {

            JSONArray settingsArray = settingsJson.getJSONArray("services");
            Log.d(TAG, "----setting array length"+settingsArray.length());
            for (int i = 0; i < settingsArray.length(); i++) {
                Settings settingsBean = new Settings();
                JSONObject contactObject = settingsArray.getJSONObject(i);
                settingsBean.setSettingID(contactObject.getString("service_id"));
                settingsBean.setServiceName(contactObject.getString("service_name"));
                settingsBean.setServiceStatus(contactObject.getString("service_status"));
                settingsBean.setServiceUpdatedDate(contactObject.getString("created_date"));

                settingsArrayList.add(settingsBean);
            }
        }catch (Exception e) {
            Log.e(TAG, "Error while parsing Settings JSON"+e.getMessage());
        }

        return settingsArrayList;

    }

    public ArrayList<AMC> parseAMCJson(JSONObject amcJson) {

        ArrayList<AMC> amcArrayList = new ArrayList<>();
        try {

            JSONArray amcArray = amcJson.getJSONArray("amc");
            Log.d(TAG, "----setting array length"+amcArray.length());
            for (int i = 0; i < amcArray.length(); i++) {
                AMC amcBean = new AMC();
                JSONObject contactObject = amcArray.getJSONObject(i);
                amcBean.setAmcID(contactObject.getString("amc_id"));
                amcBean.setAmcName(contactObject.getString("amc_name"));
                amcBean.setAmcUpdatedDate(contactObject.getString("amc_update_date"));

                amcArrayList.add(amcBean);
            }
        }catch (Exception e) {
            Log.e(TAG, "Error while parsing AMC JSON"+e.getMessage());
        }

        return amcArrayList;

    }

    public ArrayList<Calendar> parseCalendarJson(JSONObject calendarJson) {

        ArrayList<Calendar> calendarArrayList = new ArrayList<>();
        try {

            JSONArray calendarArray = calendarJson.getJSONArray("calendar");
            Log.d(TAG, "----setting array length"+calendarArray.length());
            for (int i = 0; i < calendarArray.length(); i++) {
                Calendar calendarBean = new Calendar();
                JSONObject contactObject = calendarArray.getJSONObject(i);
                calendarBean.setCalendarDate(contactObject.getString("date"));
                calendarBean.setCalendarTitle(contactObject.getString("title"));
                calendarBean.setCalendarType(contactObject.getString("type"));
                calendarBean.setCalendarDescription(contactObject.getString("description"));

                calendarArrayList.add(calendarBean);
            }
        }catch (Exception e) {
            Log.e(TAG, "Error while parsing Calendar JSON"+e.getMessage());
        }

        return calendarArrayList;

    }

    public List<ServiceItemBean> parseServicesJson(JSONObject servicesJson) {

        List<ServiceItemBean> servicesArrayList = new ArrayList<>();
        try {

            JSONArray servicesArray = servicesJson.getJSONArray("home_service_categories");
            Log.d(TAG, "----setting array length"+servicesArray.length());
            for (int i = 0; i < servicesArray.length(); i++) {
                ServiceItemBean servicesBean = new ServiceItemBean();
                JSONObject servicesObject = servicesArray.getJSONObject(i);
                servicesBean.setServiceName(servicesObject.getString("service_name"));
                servicesBean.setServiceImage(servicesObject.getString("service_image"));
                servicesBean.setServiceDescription(servicesObject.getString("service_description"));
                servicesBean.setServiceRegion(servicesObject.getString("service_region"));
                JSONArray providersArray = servicesObject.getJSONArray("service_details");
                List<ServiceProviderBean> providersArrayList = new ArrayList<>();
                for (int j = 0; j < providersArray.length(); j++) {
                    ServiceProviderBean serviceProviderBean = new ServiceProviderBean();
                    JSONObject providerObject = providersArray.getJSONObject(i);
                    serviceProviderBean.setProviderName(providerObject.getString("provider_name"));
                    serviceProviderBean.setProviderAddress(providerObject.getString("provider_address"));
                    serviceProviderBean.setProviderImage(providerObject.getString("provider_image"));
                    serviceProviderBean.setProviderMobile(providerObject.getString("provider_mobile"));
                    serviceProviderBean.setProviderEmail(providerObject.getString("provider_email"));
                    serviceProviderBean.setServiceRegion(providerObject.getString("provider_region"));
                    providersArrayList.add(serviceProviderBean);
                    servicesBean.setProviderList(providersArrayList);
                }

                servicesArrayList.add(servicesBean);
            }
        }catch (Exception e) {
            Log.e(TAG, "Error while parsing Calendar JSON"+e.getMessage());
        }

        return servicesArrayList;

    }


    public ArrayList<Contacts> parseContactsJson(JSONObject contactsJson) {

        ArrayList<Contacts> contactsArrayList = new ArrayList<>();
        try {

            JSONArray contactsArray = contactsJson.getJSONArray("contacts");

            for (int i = 0; i < contactsArray.length(); i++) {
                Contacts contactsBean = new Contacts();
                JSONObject contactObject = contactsArray.getJSONObject(i);
                contactsBean.setName(contactObject.getString("name"));
                contactsBean.setFlatNo(contactObject.getString("flat_no"));
                contactsBean.setMobile(contactObject.getString("phone"));
                contactsBean.setAddress(contactObject.getString("address"));
                contactsBean.setEmail(contactObject.getString("email"));
                contactsBean.setIsOwener(contactObject.getString("isOwner"));
                contactsBean.setPhoto(contactObject.getString("photo"));

                contactsArrayList.add(contactsBean);
            }
        }catch (Exception e) {
            Log.e(TAG, "Error while parsing contacts JSON"+e.getMessage());
        }

        return contactsArrayList;

    }

    public HashMap<GroceryCategoriesBean, ArrayList<GroceryItemsBean>> parseMasterJson(JSONObject jsonObject) throws JSONException {
        String timestamp = jsonObject.getString("timestamp");
        String version = jsonObject.getString("version");
        JSONArray categoryJsonArray = jsonObject.getJSONArray("categories");
        for (int i = 0; i < categoryJsonArray.length(); i++) {
            ArrayList<GroceryItemsBean> categoryItemsBeansList = new ArrayList<>();
            GroceryCategoriesBean categoriesBean = new GroceryCategoriesBean();
            JSONObject categoryJsonObject = categoryJsonArray.getJSONObject(i);
            categoriesBean.setCategoryName(categoryJsonObject.getString("name"));
            categoriesBean.setCategoryId(Integer.parseInt(categoryJsonObject.getString("id")));
            JSONArray itemsJsonArray = categoryJsonObject.getJSONArray("items");
            for (int j = 0; j < itemsJsonArray.length(); j++) {
                JSONObject itemJsonObject = itemsJsonArray.getJSONObject(j);
                GroceryItemsBean categoryItemsBean = new GroceryItemsBean();
                categoryItemsBean.setItemName(itemJsonObject.getString("name"));
                categoryItemsBean.setItemId(Integer.parseInt(itemJsonObject.getString("id")));
                categoryItemsBeansList.add(categoryItemsBean);
            }
            groceryMap.put(categoriesBean, categoryItemsBeansList);
        }
        return groceryMap;
    }



    public String loadJSONFromAsset(String jsonFile) {
        AssetManager assetManager = mContext.getResources().getAssets();
        InputStream is = null;
        String json = null;
        try {
            is = assetManager.open(jsonFile);
            if ( is != null){

                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;


    }

    public void insertCategoryListItems(HashMap<GroceryCategoriesBean, ArrayList<GroceryItemsBean>> groceryMap, SQLiteDatabase db) {
        try {
            // SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentCategoryListItems = new ContentValues();

            Set<Map.Entry<GroceryCategoriesBean, ArrayList<GroceryItemsBean>>> values = groceryMap.entrySet();
            Iterator iterator = values.iterator();
            while (iterator.hasNext()) {
                Map.Entry<GroceryCategoriesBean, ArrayList<GroceryItemsBean>> entries = (Map.Entry) iterator.next();
                //contentCategoryListItems.put("SLNo", entries.getKey().getCategoryId());
                contentCategoryListItems.put(CATEGORY_ID, entries.getKey().getCategoryId());
                contentCategoryListItems.put(CATEGORY_NAME, entries.getKey().getCategoryName());
                contentCategoryListItems.put(CREATED_DATE, "" + getDateTime());
                contentCategoryListItems.put(MODIFIED_DATE, "" + getDateTime());

                ArrayList<GroceryItemsBean> itemList = entries.getValue();
                for (int j = 0; j < itemList.size(); j++) {
                    ContentValues contentItemValues = new ContentValues();
                    contentItemValues.put(CATEGORY_ITEM_NAME, itemList.get(j).getItemName());
                    contentItemValues.put(CATEGORY_ID, entries.getKey().getCategoryId());

                    contentItemValues.put(CREATED_DATE, "" + getDateTime());
                    contentItemValues.put(MODIFIED_DATE, "" + getDateTime());
                    long Itemvalue = db.insert(TABLE_CATEGORIES_ITEMS, null, contentItemValues);
                    Log.d(TAG, "insertCategoryListItems: " + Itemvalue);
                }
                long value = db.insert(TABLE_CATEGORIES, null, contentCategoryListItems);
                Log.d(TAG, "insertCategoryListItems: " + value);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }



    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}