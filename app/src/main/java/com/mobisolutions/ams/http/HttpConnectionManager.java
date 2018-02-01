package com.mobisolutions.ams.http;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.utils.APIConstants;
import com.mobisolutions.ams.utils.AppUtils;
import com.mobisolutions.ams.utils.DebugUtil;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;


public class HttpConnectionManager {
	private WeakReference<HttpTask> mWRHttpTask;
	Context mContext;

	 HttpConnectionsCallBack httpCallBack;
  private static final String TAG = HttpConnectionManager.class.getSimpleName();	
  
	public HttpConnectionManager(Context context)
	{
		mContext=context;

	    httpCallBack = new HttpConnectionsCallBack();
	}

    public void getSettingsResponse( CallbackUtils.SettingsCallBack callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_settings_method));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.GET_SETTINGS_REQUEST_ID, httpCallBack.getHttpSettingsGetCallBack(callBack), null, url);

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    public void getContactsResponse( CallbackUtils.ContactsCallBack callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_contacts_method));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.GET_CONTACTS_REQUEST_ID, httpCallBack.getHttpContactsGetCallBack(callBack), null, url);

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    public void getBannersResponse( CallbackUtils.BannersCallBack callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_banners_method));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.GET_BANNERS_REQUEST_ID, httpCallBack.getHttpBannersGetCallBack(callBack), null, url);
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    public void getCategoriesResponse( CallbackUtils.HomeCategoriesCallBack callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_categories_method));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.GET_CATEGORIES_REQUEST_ID, httpCallBack.getHttpCategoriesGetCallBack(callBack), null, url);
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    public void getAMCResponse( CallbackUtils.AMCCallBack callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_amc_method));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.GET_AMC_REQUEST_ID, httpCallBack.getHttpAMCGetCallBack(callBack), null, url);
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    public void getHomeServicesResponse( CallbackUtils.HomeServices callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_services));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.HOME_SERVICES, httpCallBack.getHttpHomeServicesGetCallBack(callBack), null, url);
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }



    public AsyncHttpClient updateContactRequest( CallbackUtils.UpdateContactsCallBack callBack,JSONObject jsonObject) {
        String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.update_contacts_method));

        HttpEntity httpEntity= new ByteArrayEntity(jsonObject.toString().getBytes());

        return executePostRequest(APIConstants.UPDATE_CONTACT_REQUEST_ID, httpCallBack.updateContactRequest(callBack),httpEntity,url);
    }


    public AsyncHttpClient sendAMCRequest( CallbackUtils.AMCCallBack callBack,JSONObject jsonObject) {
        String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_request_amc_method));

        HttpEntity httpEntity= new ByteArrayEntity(jsonObject.toString().getBytes());

        return executePostRequest(APIConstants.SEND_AMC_REQUEST_ID, httpCallBack.updateAMCRequest(callBack),httpEntity,url);
    }

    public AsyncHttpClient sendMeetingRequest( CallbackUtils.MeetingsCallBack callBack,JSONObject jsonObject) {
        String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.insert_meeting));

        HttpEntity httpEntity= new ByteArrayEntity(jsonObject.toString().getBytes());

        return executePostRequest(APIConstants.SEND_MEETINGS_REQUEST_ID, httpCallBack.updateMeetingRequest(callBack),httpEntity,url);
    }

    public void getMeetingsHistory( CallbackUtils.MeetingsCallBack callBack) {
        try
        {
            String url= AppUtils.getAPIUrlAppend(mContext,mContext.getString(R.string.get_contacts_method));
            Log.d(TAG, "-----Requested url"+url);
            executeGetRequest(APIConstants.GET_MEETINGS_REQUEST_ID, httpCallBack.getHttpMeetingsGetCallBack(callBack), null, url);

        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }



    public AsyncHttpClient executePostRequest(int requestID, HttpTask.HttpCallback httpCallback, HttpEntity httpEntity, String url)
    {
        HttpTask.HttpRequest httpRequest = new HttpTask.HttpRequest();
        httpRequest.method = HttpTask.HttpRequest.METHOD_POST;
        httpRequest.entity=httpEntity;
        httpRequest.isResponseBinary = true;
        httpRequest.url = url;
        DebugUtil.debugMessage(TAG, "POST URL "+ httpRequest.url);
        HttpTask httpTask = new HttpTask(mContext,requestID,  httpCallback, null) ;

        mWRHttpTask = new WeakReference<HttpTask>(httpTask);
        mWRHttpTask.get().doInBackground(httpRequest);
        return mWRHttpTask.get().client;
    }



    public void executeGetRequest(int requetID, HttpTask.HttpCallback httpCallback, RequestParams requestParams, String url)
    {
        HttpTask.HttpRequest httpRequest = new HttpTask.HttpRequest();
        httpRequest.method = HttpTask.HttpRequest.METHOD_GET;
        //HttpTask.HttpRequest.requestParams=requestParams;
        httpRequest.isResponseBinary = true;
        httpRequest.url =url ;
        HttpTask httpTask = new HttpTask(mContext, requetID,  httpCallback, null) ;
        mWRHttpTask = new WeakReference<HttpTask>(httpTask);
        mWRHttpTask.get().doInBackground(httpRequest);
    }

    public AsyncHttpClient executePostImageRequest(int requetID,HttpTask.HttpCallback httpCallback,HttpEntity httpEntity,String url)
    {
        HttpTask.HttpRequest httpRequest = new HttpTask.HttpRequest();
        httpRequest.method = HttpTask.HttpRequest.METHOD_POST_IMAGE;
        httpRequest.entity=httpEntity;
        httpRequest.isResponseBinary = true;
        httpRequest.url = url;
        HttpTask httpTask = new HttpTask(mContext,requetID,  httpCallback, null) ;

        mWRHttpTask = new WeakReference<HttpTask>(httpTask);
        mWRHttpTask.get().doInBackground(httpRequest);
        return mWRHttpTask.get().client;
    }


}
