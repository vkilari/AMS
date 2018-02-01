package com.mobisolutions.ams.utils;

import android.content.Context;

import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 12/18/17.
 */

public class ConfigUtils {

    public static String getOmniServiceUrl(Context context)
    {

        if(BuildConfig.IS_CMS_LIVE)
        {

            return context.getString(R.string.mobile_domain_prod);

        }else
        {

            return context.getString(R.string.mobile_domain);

        }



    }


}
