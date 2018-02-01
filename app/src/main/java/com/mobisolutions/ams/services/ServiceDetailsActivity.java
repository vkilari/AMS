package com.mobisolutions.ams.services;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 11/16/17.
 */

public class ServiceDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_service_details);

        mToggle.setDrawerIndicatorEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat
                .getDrawable(this, R.mipmap.back));

        mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Doesn't have to be onBackPressed
                onBackPressed();
            }
        });

    }
}
