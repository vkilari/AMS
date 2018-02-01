package com.mobisolutions.ams.contacts;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.home.HomeActivity;
import com.mobisolutions.ams.uikit.widget.CustomTextView;

import android.widget.AdapterView.OnItemClickListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vkilari on 11/6/17.
 */

public class ContactDetailsActivity extends BaseActivity implements OnItemClickListener{

    public static final String TAG = ContactDetailsActivity.class.getSimpleName();

    private ImageView owener_imageView, tenant_imageView;
    private CustomTextView living_staus, owener_name, owener_phone_number, owener_email, tenant_name, tenant_phone_number, tenant_email;

    private ArrayAdapter<String> mPopupAdapter;
    private ArrayList<String> mOptionsArray =
            new ArrayList<>(Arrays.asList("Share", "Call", "Message"));
    private ListPopupWindow mPopupWindow;
    private Context mContext;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;
    private Toolbar toolbar;
    String owenerName;
    String owenerEmail;
    String owenerMobile;
    String tenentName;
    String tenentEmail;
    String tenentMobile;
    String living_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flat_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.contacts_details_label));
        setSupportActionBar(toolbar);

        owener_imageView = (ImageView) findViewById(R.id.owener_imageView);
        tenant_imageView = (ImageView) findViewById(R.id.tenant_imageView);

        living_staus = (CustomTextView) findViewById(R.id.living_status);
        owener_name = (CustomTextView) findViewById(R.id.owener_name);
        owener_phone_number = (CustomTextView) findViewById(R.id.owener_phone_number);
        owener_email = (CustomTextView) findViewById(R.id.owener_email);

        tenant_name = (CustomTextView) findViewById(R.id.tenant_name);
        tenant_phone_number = (CustomTextView) findViewById(R.id.tenant_phone_number);
        tenant_email = (CustomTextView) findViewById(R.id.tenant_email);


        Intent intent = getIntent();
        if (intent != null) {
            owenerName = intent.getStringExtra("name");
            owenerEmail = intent.getStringExtra("email");
            owenerMobile = intent.getStringExtra("mobile");
            living_status = intent.getStringExtra("living_status");

            tenentName = intent.getStringExtra("name");
            tenentEmail = intent.getStringExtra("email");
            tenentMobile = intent.getStringExtra("mobile");

            owener_name.setText(getResources().getString(R.string.owener_name, owenerName));
            owener_phone_number.setText(getResources().getString(R.string.owener_phone, owenerMobile));
            owener_email.setText(getResources().getString(R.string.owener_email, owenerEmail));
            living_staus.setText(getResources().getString(R.string.contacts_living_status_label, living_status));

            tenant_name.setText(getResources().getString(R.string.owener_name, owenerName));
            tenant_phone_number.setText(getResources().getString(R.string.owener_phone, owenerMobile));
            tenant_email.setText(getResources().getString(R.string.owener_email, owenerEmail));

        }



        mContext = this;

        owener_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupPopupWindow(owener_imageView);

            }
        });

        tenant_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupPopupWindow(tenant_imageView);

            }
        });
    }



    private void setupPopupWindow(View view)
    {
        mPopupAdapter = new ArrayAdapter<>(ContactDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, mOptionsArray);
        mPopupWindow = new ListPopupWindow(ContactDetailsActivity.this);
        mPopupWindow.setAdapter(mPopupAdapter);
        mPopupWindow.setAnchorView(view);
        mPopupWindow.setWidth(500);
        mPopupWindow.setHorizontalOffset(-380); //<--this provides the margin you need
        //if you need a custom background color for the popup window, use this line:
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ContactDetailsActivity.this, R.color.ams_white)));
        mPopupWindow.setOnItemClickListener(ContactDetailsActivity.this);
        mPopupWindow.show();

    }



    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_CALENDAR)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 //   writeCalendarEvent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        boolean result = checkPermission();

    Log.d(TAG, "---------position:::"+position);

    if (position == 0) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, owenerName+" : "+owenerMobile);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    } else  if (position == 1) {
        if (result) {
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + owenerMobile));
            mContext.startActivity(intent);
        }
    } else if(position == 2) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", owenerName+" : "+owenerMobile);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }


        mPopupWindow.dismiss();

    }
}
