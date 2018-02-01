package com.mobisolutions.ams;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobisolutions.ams.amc.AMC;
import com.mobisolutions.ams.calendar.Calendar;
import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.database.DBHelper;
import com.mobisolutions.ams.http.CallbackUtils;
import com.mobisolutions.ams.services.ServiceItemBean;
import com.mobisolutions.ams.utils.APIConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkilari on 12/30/17.
 */

public class APIServiceManager {
    private static final String TAG = APIServiceManager.class.getName();
    private Context mContext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public APIServiceManager(Context context){
        mContext = context;
        dbHelper = AMSApplication.getDBHelper();
        db = dbHelper.getWritableDatabase();
    }



    public  CallbackUtils.ContactsCallBack  getContactsGetCallBack(final int settingID, final String serviceUpdatedDate, final GetServiceUpdated getServiceUpdated)
    {
        return new CallbackUtils.ContactsCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Contacts Error::::"+jsonObject);
            }
            @Override
            public void setContactsJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Contacts::::"+jsonObject);
                ArrayList<Contacts> contactsArrayList = new ArrayList<>();
                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Contacts::::"+responseCode+"::::"+updatedTimeStamp);
                    if (responseCode == APIConstants.SUCCESS) {
                        JSONArray jsonArray = jsonObject.getJSONArray("contacts");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Contacts contacts = new Contacts();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setMobile(jsonObject1.getString("mobile"));
                            contacts.setEmail(jsonObject1.getString("email"));
                            contacts.setFlatNo(jsonObject1.getString("flat_no"));
                            contacts.setIsOwener(jsonObject1.getString("living_status"));
                            // contacts.setImage(jsonObject1.getString("photo"));

                            contactsArrayList.add(contacts);
                        }

                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Contacts::"+e.getMessage());
                }

                if (contactsArrayList.size() > 0) {
                    dbHelper.deleteContacts();

                    dbHelper.insertContact(contactsArrayList, db);

                    dbHelper.updateSettings(settingID, serviceUpdatedDate);

                    getServiceUpdated.isServiceUpdated(true);

                }


            }


        };
    }

    public  CallbackUtils.AMCCallBack  getAMCGetCallBack(final int settingID)
    {
        final String updatedTimeStamp="";
        return new CallbackUtils.AMCCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Contacts Error::::"+jsonObject);
            }
            @Override
            public void setSuccessJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Contacts::::"+jsonObject);
                ArrayList<AMC> amcArrayList = new ArrayList<>();
                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Contacts::::"+responseCode+"::::"+updatedTimeStamp);
                    if (responseCode == APIConstants.SUCCESS) {
                        JSONArray jsonArray = jsonObject.getJSONArray("contacts");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            AMC amc = new AMC();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            amc.setAmcID(jsonObject1.getString("amc_id"));
                            amc.setAmcName(jsonObject1.getString("amc_name"));
                            amc.setAmcUpdatedDate(jsonObject1.getString("amc_update_date"));

                            // contacts.setImage(jsonObject1.getString("photo"));

                            amcArrayList.add(amc);
                        }

                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Contacts::"+e.getMessage());
                }

                if (amcArrayList.size() > 0) {
                    dbHelper.deleteAMC();

                    dbHelper.insertAMC(amcArrayList, db);

                    dbHelper.updateSettings(settingID, updatedTimeStamp);

                    //getServiceUpdated.isServiceUpdated(true);

                }


            }


        };
    }

    public  CallbackUtils.HomeServices  getHomeServicesGetCallBack(final int settingID)
    {
        final String updatedTimeStamp="";
        return new CallbackUtils.HomeServices() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Contacts Error::::"+jsonObject);
            }
            @Override
            public void setSuccessJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Contacts::::"+jsonObject);
                ArrayList<ServiceItemBean> servicesArrayList = new ArrayList<>();
                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Contacts::::"+responseCode+"::::"+updatedTimeStamp);
                    if (responseCode == APIConstants.SUCCESS) {

                        JSONArray jsonArray = jsonObject.getJSONArray("contacts");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ServiceItemBean serviceItemBean = new ServiceItemBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            serviceItemBean.setServiceId("service_id");
                            serviceItemBean.setServiceName("service_name");
                            serviceItemBean.setServiceImage("service_image");
                            serviceItemBean.setServiceDescription("service_description");
                            // contacts.setImage(jsonObject1.getString("photo"));

                            servicesArrayList.add(serviceItemBean);
                        }

                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Contacts::"+e.getMessage());
                }

                if (servicesArrayList.size() > 0) {
                    dbHelper.deleteHomeServices();

                    dbHelper.insertHomeServices(servicesArrayList, db);

                    dbHelper.updateSettings(settingID, updatedTimeStamp);

                    //getServiceUpdated.isServiceUpdated(true);

                }


            }


        };
    }


    public  CallbackUtils.MeetingsCallBack  getMeetingHistoryCallBack(final int settingID, final String serviceUpdatedDate, final GetServiceUpdated getServiceUpdated)
    {
        return new CallbackUtils.MeetingsCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Meeting History Error::::"+jsonObject);
            }
            @Override
            public void setMeetingsJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Meetings History::::"+jsonObject);
                ArrayList<Calendar> calendarArrayList = new ArrayList<>();
                try {



                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Contacts::"+e.getMessage());
                }

                if (calendarArrayList.size() > 0) {
                    dbHelper.deleteCalendarMeetings();

                    dbHelper.insertCalendar(calendarArrayList, db);

                    dbHelper.updateSettings(settingID, serviceUpdatedDate);

                    getServiceUpdated.isMeetingServiceUpdated(true);

                }

            }


        };
    }


    public interface GetServiceUpdated {
        public void isServiceUpdated(boolean isUpdated);
        public void isMeetingServiceUpdated(boolean isUpdated);
    }

}
