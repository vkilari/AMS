package com.mobisolutions.ams.contacts;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisolutions.ams.AMSApplication;
import com.mobisolutions.ams.APIServiceManager;
import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.http.CallbackUtils;
import com.mobisolutions.ams.http.HttpConnectionManager;
import com.mobisolutions.ams.maintenance.GeneralMaintenanceActivity;
import com.mobisolutions.ams.utils.APIConstants;
import com.mobisolutions.ams.utils.AppUtils;
import com.mobisolutions.ams.utils.CommonUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vkilari on 11/4/17.
 */

public class ContactsActivity extends BaseActivity{

    private static final String TAG = ContactsActivity.class.getName();
    private RecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Contacts> contactsList;
    private static final int REQUEST_PERMISSIONS = 20;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    public static HttpConnectionManager httpConnectionManager;
    private Toolbar toolbar;
    private TextView admingEdit;
    boolean isEditMode=false;
    private Context mContext;
    ProgressBar mprogressBar;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contacts);
        contactsList = new ArrayList<Contacts>();
        mRecyclerView = (RecyclerView) findViewById(R.id.contactList);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        mContext = this;
        mRecyclerView.setHasFixedSize(true);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.contacts_label));
        setSupportActionBar(toolbar);

        admingEdit = (TextView) toolbar.findViewById(R.id.admin_edit);
        admingEdit.setVisibility(View.VISIBLE);
        httpConnectionManager = new HttpConnectionManager(this);

        admingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEditMode)
                {
                    isEditMode=true;
                    admingEdit.setText(getResources().getString(R.string.done_label));

                }else
                {
                    admingEdit.setText(getResources().getString(R.string.edit_label));
                    isEditMode=false;

                }

                setAdapter();
            }
        });
        //http://dc1vk.kpvsolutions.in:9080/apartmentservices/getContacts
        //http://dc1vk.kpvsolutions.in:9080/apartmentservices/addContacts
        //http://dc1vk.kpvsolutions.in:9080/apartmentservices/updateContacts
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        setAdapter();

    }

    public void setAdapter() {

        Log.d(TAG, "----------------setAdapter called----");

        ArrayList<Contacts> contacts = AMSApplication.getDBHelper().getAllCotacts();
        Log.d(TAG, "----------------setAdapter size----"+contacts.size());

        mAdapter = new ContactsAdapter(contacts, new OnItemClickListener() {
            @Override
            public void onItemClick(final Contacts item, View view, boolean isEditClicked, int position) {
                //Toast.makeText(ContactsActivity.this, "Item Clicked"+item.getName() +":::::"+isEditClicked, Toast.LENGTH_LONG).show();

                if (isEditClicked) {
                    Log.d(TAG, "---------Edit clicked----");
                    contactEditDialog(ContactsActivity.this, item, position);
                } else {
                    Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                    intent.putExtra("name", item.getName());
                    intent.putExtra("mobile", item.getMobile());
                    intent.putExtra("email", item.getEmail());
                    intent.putExtra("living_status", item.getIsOwener());
                    startActivity(intent);

                    view.findViewById(R.id.phone_call).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            askForPermission(Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CODE);
                            phoneNumber = item.getMobile();
                            makePhoneCall(phoneNumber);
                        }
                    });
                }

            }
        }, this, this, isEditMode);

        mRecyclerView.setAdapter(mAdapter);
    }

    public void contactEditDialog(Activity activity, final Contacts item, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_contact_edit);

        final EditText name = (EditText) dialog.findViewById(R.id.name);
        final EditText mobile = (EditText) dialog.findViewById(R.id.mobile);
        final EditText email = (EditText) dialog.findViewById(R.id.email);
        name.setText(item.getName());
        mobile.setText(item.getMobile());
        email.setText(item.getEmail());
        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonUtils.hideSoftKeyboard(ContactsActivity.this);

               // Log.d("TAG", "get Item:"+mAdapter.getItem(itemPosition)+" :::"+text.getText().toString());

                //mAdapter.getItem(position).setName(name.getText().toString());
               // mAdapter.getItem(position).setMobile(mobile.getText().toString());
               // mAdapter.getItem(position).setEmail(email.getText().toString());
               // mAdapter.notifyDataSetChanged();
                mprogressBar.setVisibility(View.VISIBLE);


                try {

                    JSONObject contactsJsonObject = new JSONObject();
                    contactsJsonObject.put("apartment_id", "sssn101");
                    contactsJsonObject.put("name", name.getText().toString());
                    contactsJsonObject.put("email", email.getText().toString());
                    contactsJsonObject.put("living_type", item.getIsOwener());
                    contactsJsonObject.put("mobile", mobile.getText().toString());
                    contactsJsonObject.put("flat_no", item.getFlatNo());
                    contactsJsonObject.put("updated_date", AppUtils.getCurrentDate());

                    Log.d(TAG, "-------contactsJsonObject----json---"+contactsJsonObject.toString());
                    httpConnectionManager.updateContactRequest(updateContactsCallBack(), contactsJsonObject);

                }catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public CallbackUtils.UpdateContactsCallBack updateContactsCallBack() {
        return new CallbackUtils.UpdateContactsCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {

            }

            @Override
            public void setSuccessJson(JSONObject jsonObject, Header header[]) {

                try {
                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Contact Updated::" + responseCode + "::::" + updatedTimeStamp);


                    if (responseCode == APIConstants.SUCCESS) {
                       // Toast.makeText(ContactsActivity.this, "Contact Updated Successfully.", Toast.LENGTH_SHORT).show();

                      //  new APIServiceOperation().execute(updatedTimeStamp);

                        httpConnectionManager.getContactsResponse(new APIServiceManager(mContext).getContactsGetCallBack(APIConstants.CONTACTS, updatedTimeStamp, new APIServiceManager.GetServiceUpdated() {
                            @Override
                            public void isServiceUpdated(boolean isUpdated) {

                                Log.d(TAG, "---------Is Service Updated::" + isUpdated);

                                if (isUpdated) {

                                    setAdapter();
                                    mAdapter.notifyDataSetChanged();
                                    mprogressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void isMeetingServiceUpdated(boolean isUpdated) {

                            }
                        }));



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ContactsActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            makePhoneCall(phoneNumber);
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                    makePhoneCall(phoneNumber);
                }
                return;
            }
        }
    }


    public void makePhoneCall(String phoneNumber){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phoneNumber));//change the number
        mContext.startActivity(callIntent);

    }

}


