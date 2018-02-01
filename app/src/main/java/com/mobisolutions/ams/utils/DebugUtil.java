package com.mobisolutions.ams.utils;

import com.mobisolutions.ams.http.HttpTask;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Created by vkilari on 12/18/17.
 */

public class DebugUtil {

    public  static  String throwableToString(Throwable t) {
        if (t == null)
            return null;

        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public    final static void debugThrowable(String TAG, Throwable t) {
     /*   if (t != null&&Constants.DEBUGGABLE) {
            Log.e(TAG, "debugThrowable returned error---------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + t.getMessage());

        }*/
    }

    public    final static void debugThrowable(String TAG, Throwable t,int id) {
      /*  if (t != null&&Constants.DEBUGGABLE) {
            Log.e(TAG, id + ": debugThrowable returned error---------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + t.getMessage());

        }*/
    }


    public   final  static  void debugResponse(String TAG, String response) {
       /* if (response != null&&Constants.DEBUGGABLE) {
            Log.d(TAG, "Response data:" + response);

        }*/
    }

    public   final static void debugStatusCode(String TAG, int statusCode) {
        String msg = String.format(Locale.US, "Return Status Code: %d", statusCode);
       /* if(Constants.DEBUGGABLE) {
            Log.d(TAG, msg);
        }*/
    }

    public   final static void debugMessage(String TAG, String message) {
    /*   if(Constants.DEBUGGABLE) {
            Log.d(TAG, "Message :" + message);
        }*/
    }

    public   final static void debugEntity(HttpTask.HttpRequest request, String TAG, int mId) {


      /*  try
        {
            if(request.entity!=null&&Constants.DEBUGGABLE)
            {

                String reqBody = EntityUtils.toString(request.entity, HTTP.UTF_8);
                if(reqBody!=null&&!reqBody.toLowerCase().contains("password"))
                {
                    Log.d(TAG,  mId+" Message : Entity "+reqBody);
                }

            }
        }
        catch (Exception e) {
           e.printStackTrace();
        }
*/
    }

    public   final static void debugHttpRequest(String TAG, String message) {
      /* if (Constants.DEBUGGABLE) {
            Log.d(TAG, "Request  :" + message);
        }*/
    }

    public   final static void debugHttpResponse(String TAG, String message) {
      /*  if (Constants.DEBUGGABLE) {
            Log.d(TAG, "Response  :" + message);
        }*/
    }

    public   final static void debugMallTest(String TAG, String message) {
       /* if (Constants.DEBUGGABLE) {
            Log.d(TAG, "MallTest  :" + message);
        }*/
    }
}
