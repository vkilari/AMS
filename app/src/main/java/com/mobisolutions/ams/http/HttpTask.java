package com.mobisolutions.ams.http;

import android.content.Context;
import android.os.Handler;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.mobisolutions.ams.utils.APIConstants;
import com.mobisolutions.ams.utils.AppUtils;
import com.mobisolutions.ams.utils.DebugUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.entity.StringEntity;



public class HttpTask {

    private static final String TAG = HttpTask.class.getSimpleName();

    public ResponseHandlerInterface getResponseHandler() {
        return new AsyncHttpResponseHandler() {



            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] errorResponse, Throwable e) {

                // status codes 400,401,500
                //BADREQUEST=400,UNAUTHORIZED=401,FORBIDDEN=403,NOTFOUND=404,INTERNALERROR=500,NOTIMPLEMENTED=501,GATEWAYTIMEOUT=503

                DebugUtil.debugHttpResponse(TAG, mId + "  onFailure statusCode " + statusCode + headers);
                //AppUtils.debugThrowable(TAG, e,mId);
                mResponse.status = ResponseStatus.ERROR;
                mResponse.statusCode = statusCode;


                try
                {
                    if (errorResponse != null && errorResponse.length > 0) {

                        mResponse.response = AppUtils.getString(errorResponse);
                        DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure  "+mResponse.response);

                    }
                    else
                    {
                        //DebugUtil.debugThrowable(TAG, e,mId);
                        e.printStackTrace();
                    }

                }catch(Exception exception)
                {
                    e.printStackTrace();
                }

                if(AppUtils.isRequestFailure(mResponse.statusCode))
                {

                    try
                    {
                        DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure isValidSession "+ AppUtils.getString(errorResponse));
                        //DebugUtil.inActiveSessionDialog(mContext, mId);
                        mCallback.callback(mId, mResponse,headers);

                    }catch(Exception exception)
                    {
                        e.printStackTrace();
                    }

                }
                else if(AppUtils.isServerInvalidResponse(statusCode)&&AppUtils.isOnline(mContext))
                {
                    try
                    {
                        JSONObject jsonObject= new JSONObject();
                        jsonObject.put(APIConstants.ERROR, "Oops! We are having trouble connecting you. Please try again later.");
                        jsonObject.put(APIConstants.ERRORMESSAGE, "Oops! We are having trouble connecting you. Please try again later.");
                        mResponse.response=jsonObject.toString();

                        mCallback.callback(mId, mResponse,headers);
                        DebugUtil.debugHttpResponse(TAG,  mId+"onFailureL  8"+jsonObject.toString());
                    }catch(Exception exception)
                    {
                        e.printStackTrace();
                        DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure 18 "+statusCode);
                    }

                }
                else
                {
					/*
				    if(APIUtils.isReauthRequired(mContext,mResponse.response,statusCode))
					{
						Bundle bundle= new Bundle();
						bundle.putInt(APIConstants.REQUEST_ID, mId);
						DebugUtil.checkUserProfileReauthStatus(mContext,false,mId);
					    DebugUtil.debugHttpResponse(TAG, mId+"   onFailure isReauthRequired "+APIUtils.isReauthRequired(mContext,mResponse.response,statusCode));
						//DebugUtil.reLoginRequired(mContext,bundle);
					}	*/


                    try {
                        DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure 3 "+statusCode);
                        if (errorResponse != null && errorResponse.length > 0) {
                            DebugUtil.debugHttpResponse(TAG, mId+ "  onFailure 4 4"+statusCode);
                            mResponse.response = AppUtils.getString(errorResponse);
                            //DebugUtil.debugHttpResponse(TAG, "  onFailure 5 "+statusCode);
                            mCallback.callback(mId, mResponse,headers);
                            //	DebugUtil.debugHttpResponse(TAG, "  onFailure 6 "+statusCode);
                            DebugUtil.debugResponse(TAG, "onFailure"	+ new String(errorResponse));
                            //	DebugUtil.debugHttpResponse(TAG, "  onFailure 7 "+statusCode);
                        }
                        else
                        {
                            if(mContext!=null&&!AppUtils.isOnline(mContext))
                            {
                                JSONObject jsonObject= new JSONObject();
                                jsonObject.put(APIConstants.ERROR, "The Internet connection appears to be offline");
                                jsonObject.put(APIConstants.ERRORMESSAGE, "The Internet connection appears to be offline");
                                mResponse.response=jsonObject.toString();

                                mCallback.callback(mId, mResponse,headers);
                                DebugUtil.debugHttpResponse(TAG,  mId+"onFailureL  8"+jsonObject.toString());
                            }
                            else
                            {
                                //DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure  8"+statusCode);
                                mResponse.response = "";
                                mCallback.callback(mId, mResponse,headers);
                                DebugUtil.debugHttpResponse(TAG, "  onFailure 9"+statusCode);
                                if (errorResponse != null && errorResponse.length > 0) {
                                    DebugUtil.debugResponse(TAG, "onFailure"
                                            + new String(errorResponse));
                                }
                                DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure 10"+statusCode);
                            }
                        }
                    }catch (Exception exception) {
                        // TODO: handle exception
                        e.printStackTrace();
                        DebugUtil.debugHttpResponse(TAG,  mId+"  onFailure 11 "+statusCode);
                    }



                    mException = e;
                    // setError();
                    // cancel(false);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] response) {
                // status codes 200,204
                try {
                    boolean isImageOctetStream=false;
//
//                    for (int i = 0; i < headers.length; i++) {
//                        if(headers[i].getValue().contains(HolidayConstants.OCTET_STREAM))
//                        {
//                            isImageOctetStream=true;
//                        }
//                    }

                    DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess Response code  "+statusCode);
                    mResponse.statusCode = statusCode;
                    mResponse.status = ResponseStatus.COMPLETE;
                    DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess 1  "+statusCode);
                    DebugUtil.debugStatusCode(TAG, statusCode);
                    //DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess 2 "+statusCode);
                    if (response != null && response.length > 0) {
                        DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess 3  "+statusCode);
                        mResponse.response = AppUtils.getString(response);

                        if(isImageOctetStream)
                        {
                            mResponse.bytes=response;
                        }

                        if(mResponse.response!=null&&mResponse.response.contains("<html>"))
                        {
                            JSONObject jsonObject= new JSONObject();
                            jsonObject.put(APIConstants.ERROR, mResponse.response);
                            jsonObject.put(APIConstants.ERRORMESSAGE, mResponse.response);
                            mResponse.response=jsonObject.toString();
                            mCallback.callback(mId, mResponse,headers);
                        }else
                        {
                            mCallback.callback(mId, mResponse,headers);
                        }

                        DebugUtil.debugResponse(TAG, mId+" onSuccess"	+ new String(response));
                        //DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess Response 4  "+statusCode);
                    }
                    else
                    {
                        //DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess Response 5  "+statusCode);
                        mResponse.response = "";
                        mCallback.callback(mId, mResponse,headers);
                        //	DebugUtil.debugHttpResponse(TAG,  mId+"  onSuccess Response 6  "+statusCode);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    DebugUtil.debugHttpResponse(TAG, "onSuccess  7  "+statusCode);
                }

                //DebugUtil.debugStatusCode(TAG, statusCode);
            }





        };
    }

    public interface HttpCallback {
        public void callback(int id, HttpResponse response, Header headers[]);
    }

    public static class HttpRequest {

        public static final int METHOD_GET = 0;
        public static final int METHOD_POST = 1;
        public static final int METHOD_GET_WITHOUT_HEADER = 2;
        public static final int METHOD_POST_WITH_PARAMETERS = 3;
        public static final int METHOD_PUT = 5;
        public static final int METHOD_POST_IMAGE = 6;
        public static List<NameValuePair> params;
        public static RequestParams requestParams;

        public HttpEntity entity;

        public boolean isResponseBinary;
        public String ifNoneMatchHeader;
        public int method;
        public String url;

        public HttpRequest() {

        }

        public HttpRequest(int method, String url, String data) {
            this.method = method;
            this.url = url;
            try {
                entity = new StringEntity(data);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public HttpRequest(int method, String url) {
            this.method = method;
            this.url = url;
        }
    }

    public static class HttpResponse {
        public String response;
        public byte[] bytes;
        public InputStream respStream;
        public int status = ResponseStatus.NONE;
        public int statusCode = ResponseStatusCodes.OK;

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.format("HttpResponse{response: %s, status: %d}",
                    response, status);
        }
    }

    public static class ResponseStatus {
        public static final int NONE = 0, RESPONSE_WAITING = 1, CANCELLED = 2,
                COMPLETE = 3, ERROR = 4, NW_UNAVAILABLE = 5;
    }

    public static class ResponseStatusCodes {

        public static final int OK = 200, NOTMODIFIED = 304 , CREATED=201,ACCEPTED=202,PARTIALINFORMATION=203,
                NORESPONSE=204,BADREQUEST=400,UNAUTHORIZED=401,PAYMENTREQUIRED=402,
                FORBIDDEN=403,NOTFOUND=404,INTERNALERROR=500,NOTIMPLEMENTED=501,
                GATEWAYTIMEOUT=503,MOVED=301,FOUND=302,METHOD=303;
    }

    private HttpCallback mCallback;

    private int mId;
    private HttpResponse mResponse;

    private Handler mHandler;

    private int mErrorMsgWhat;

    private Context mContext;

    private Throwable mException;
    public AsyncHttpClient client;

    public HttpTask(Context context, int id, HttpCallback callback,
                    Handler handler) {
        mId = id;
        mCallback = callback;
        // mUserAgent = userAgent;
        mHandler = handler;
        // mTimeout = timeout;
        mErrorMsgWhat = id;
        // mDeviceId = deviceId;
        mContext = context;
        client = new AsyncHttpClient();

    }

    public HttpTask(HttpCallback callback) {
        mCallback = callback;

    }



    public HttpResponse doInBackground(HttpRequest request) {

        RequestHandle requestHandle;

        //Need to remove the below line  when https certificates are authorised.  Below line is for skipping check for certificate authorisation
        //client.setSSLSocketFactory(MySSLSocketFactory.getSocketFactory());

        client.setTimeout(30 * 1000);

        PersistentCookieStore cookieStore = new PersistentCookieStore(mContext);
        client.setCookieStore(cookieStore);

        mResponse = new HttpResponse();


        InputStream respStream = null;
        try {

            switch (request.method) {

                case HttpRequest.METHOD_POST:

                    DebugUtil.debugHttpRequest(TAG, mId+"  METHOD_POST"
                            + request.url);

                    DebugUtil.debugEntity(request,TAG, mId);

                    requestHandle =   client.post(mContext, request.url,
                            HttpHelper.getDefalutHeaders(), request.entity,
                            "application/x-www-form-urlencoded; charset=utf-8", getResponseHandler());

                    break;

                case HttpRequest.METHOD_GET:
                    //   DebugUtil.debugHttpRequest(TAG, mId+"  METHOD_GET "+ HttpRequest.requestParams.toString());
                    DebugUtil.debugHttpRequest(TAG, mId + "  METHOD_GET"
                            + request.url);

                    requestHandle = client.get(mContext, request.url,
                            HttpHelper.getDefalutHeaders(), HttpRequest.requestParams,
                            getResponseHandler());
                    break;

		/*	case HttpRequest.METHOD_PUT:

                DebugUtil.debugHttpRequest(TAG, mId+" METHOD_PUT "+ request.url);

				requestHandle = client.put(mContext, request.url,
						HttpHelper.getDefalutHeaders(), request.entity,
						"application/x-www-form-urlencoded; charset=utf-8", getResponseHandler());

				break;*/


                case HttpRequest.METHOD_POST_IMAGE:
                    DebugUtil.debugHttpRequest(TAG, mId+" METHOD_POST_IMAGE "+ request.url);
                    //AppUtils.debugEntity(request,TAG, mId);
                    requestHandle = client.post(mContext, request.url,
                            HttpHelper.getImagePostHeaders(), request.entity,
                            HttpHelper.getImagePostHeaders()[1].getValue(), getResponseHandler());
                    DebugUtil.debugHttpRequest(TAG, mId+" METHOD_POST_IMAGE ");




                    break;


                default:
                    throw new UnsupportedOperationException("method not supported");
            }

        }

        catch (Exception e) {
            mException = e;
            // setError();
            // cancel(false);
            e.printStackTrace();
        }

        finally {
            if (!request.isResponseBinary && respStream != null)
                try {
                    respStream.close();
                } catch (IOException e) {// ignore
                    e.printStackTrace();
                }
        }
        return null;
    }


	/*private void setError() {
		// Log.w(TAG, mException);
		if (mHandler != null)
			mHandler.sendMessage(mHandler.obtainMessage(mErrorMsgWhat,
					mException));
		if (mResponse.status == ResponseStatus.RESPONSE_WAITING)
			mResponse.status = ResponseStatus.CANCELLED;

		if (mResponse.status != ResponseStatus.COMPLETE) {
			if (mException instanceof SocketTimeoutException
					|| mException instanceof ConnectTimeoutException
					|| mException instanceof SocketException
					|| mException instanceof UnknownHostException
					|| mException instanceof ConnectException)
				mResponse.status = ResponseStatus.NW_UNAVAILABLE;
		}
		mCallback.callback(mId, mResponse,null);
	}*/
}
