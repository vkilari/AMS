package com.mobisolutions.ams.http;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vkilari on 12/18/17.
 */

public class CallbackUtils {

    public interface SettingsCallBack
    {
        public void setSettingsJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface BannersCallBack
    {
        public void setBannersJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface HomeCategoriesCallBack
    {
        public void setCategoriesJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface ContactsCallBack
    {
        public void setContactsJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface UpdateContactsCallBack
    {
        public void setSuccessJson(JSONObject jsonObject, Header header[]);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface AMCCallBack
    {
        public void setSuccessJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface MaintenanceCallBack
    {
        public void setMaintenanceJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface MeetingsCallBack
    {
        public void setMeetingsJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }

    public interface HomeServices
    {
        public void setSuccessJson(JSONObject jsonObject);
        public void setErrorJson(JSONObject jsonObject);
    }


}
