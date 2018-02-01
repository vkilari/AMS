package com.mobisolutions.ams.awspinpoint;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vkilari on 11/6/17.
 */

public class AWSPinpointService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

      //  AWSMobileClient.defaultMobileClient().getPinpointManager().getNotificationClient().registerGCMDeviceToken(refreshedToken);
    }

}
