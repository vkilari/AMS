package com.mobisolutions.ams.http;


import com.mobisolutions.ams.utils.APIConstants;
import com.mobisolutions.ams.utils.DebugUtil;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satish.gummadi on 05-08-2014.
 */

public class HttpConnectionsCallBack {


    public HttpTask.HttpCallback getHttpSettingsGetCallBack(final CallbackUtils.SettingsCallBack httpSettingsCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpSettingsCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpSettingsCallBack.setSettingsJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback getHttpBannersGetCallBack(final CallbackUtils.BannersCallBack httpBannersCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpBannersCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpBannersCallBack.setBannersJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }


    public HttpTask.HttpCallback getHttpCategoriesGetCallBack(final CallbackUtils.HomeCategoriesCallBack httpCategoriesCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpCategoriesCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpCategoriesCallBack.setCategoriesJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback getHttpAMCGetCallBack(final CallbackUtils.AMCCallBack httpAMCCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpAMCCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpAMCCallBack.setSuccessJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback getHttpHomeServicesGetCallBack(final CallbackUtils.HomeServices httpHomeServicesCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpHomeServicesCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpHomeServicesCallBack.setSuccessJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }


    public HttpTask.HttpCallback getHttpContactsGetCallBack(final CallbackUtils.ContactsCallBack httpContactsCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                        jsonObject = new JSONObject(response.response);
                        if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                        {
                            httpContactsCallBack.setErrorJson(jsonObject);
                        }
                        else
                        {
                            httpContactsCallBack.setContactsJson(jsonObject);
                        }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback getHttpMeetingsGetCallBack(final CallbackUtils.MeetingsCallBack httpMeetingsCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpMeetingsCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpMeetingsCallBack.setMeetingsJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback updateContactRequest(final CallbackUtils.UpdateContactsCallBack httpUpdateContactsCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpUpdateContactsCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpUpdateContactsCallBack.setSuccessJson(jsonObject, headers);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback updateAMCRequest(final CallbackUtils.AMCCallBack httpUpdateAMCCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpUpdateAMCCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpUpdateAMCCallBack.setSuccessJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }

    public HttpTask.HttpCallback updateMeetingRequest(final CallbackUtils.MeetingsCallBack httpUpdateMeetingCallBack) {
        // TODO Auto-generated method stub
        return new HttpTask.HttpCallback(){
            @Override
            public void callback(int id, HttpTask.HttpResponse response, Header headers[]) {
                DebugUtil.debugMessage(getClass().getSimpleName(), id + " " + response.response);
                JSONObject jsonObject;
                try {


                    jsonObject = new JSONObject(response.response);
                    if(jsonObject!=null&&!jsonObject.isNull(APIConstants.ERROR))
                    {
                        httpUpdateMeetingCallBack.setErrorJson(jsonObject);
                    }
                    else
                    {
                        httpUpdateMeetingCallBack.setMeetingsJson(jsonObject);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }


}
