package com.mobisolutions.ams.amc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.ecommerce.Product;
import com.mobisolutions.ams.AMSApplication;
import com.mobisolutions.ams.APIServiceManager;
import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.http.CallbackUtils;
import com.mobisolutions.ams.http.HttpConnectionManager;
import com.mobisolutions.ams.maintenance.GeneralMaintenanceActivity;
import com.mobisolutions.ams.maintenance.MaintenanceItem;
import com.mobisolutions.ams.uikit.widget.StandardButton;
import com.mobisolutions.ams.uikit.widget.StandardDialogFragment;
import com.mobisolutions.ams.utils.APIConstants;
import com.mobisolutions.ams.utils.AppUtils;
import com.mobisolutions.ams.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AMCActivity extends BaseActivity {
    private static final String TAG = AMCActivity.class.getName();
    private Toolbar toolbar;

    private Context mContext;
    private LinearLayout linearLayout;
    private CheckBox checkBox;
    private StandardButton submit;
    private Activity mActivity;
    StringBuffer sb = new StringBuffer();
    public static HttpConnectionManager httpConnectionManager;
    ArrayList<String> selectedAMCList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laout_amc);
        mContext = this;
        mActivity = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        submit = (StandardButton) findViewById(R.id.amc_submit);
        toolbar.setTitle(getString(R.string.amc_header));
        setSupportActionBar(toolbar);

        linearLayout = (LinearLayout) findViewById(R.id.linear_amc);
        httpConnectionManager = new HttpConnectionManager(this);

        ArrayList<AMC> amcs = AMSApplication.getDBHelper().getAMCs();

        for (int i = 0; i<amcs.size(); i++) {

            checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setTag(amcs.get(i).getAmcID());
            checkBox.setText(amcs.get(i).getAmcName());
            //checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            checkBox.setOnCheckedChangeListener(myCheckChangList);
            linearLayout.addView(checkBox);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    for (int i =0; i<selectedAMCList.size(); i++) {
                        Log.d(TAG, "---size"+selectedAMCList.size()+":::::"+selectedAMCList.get(i));
                        sb.append(selectedAMCList.get(i));
                        sb.append(",");
                    }
                    if (selectedAMCList.size() < 1){
                        showDialog(mActivity, getString(R.string.amc_request), getString(R.string.amc_request_no_content),
                                getString(R.string.dialog_ok_button));

                    } else {
                        JSONObject amcJsonObject = new JSONObject();
                        amcJsonObject.put("apartment_id", "sssn101");
                        amcJsonObject.put("apartment_name", "Sri Shirdi Sai Nilayam");
                        amcJsonObject.put("amc_ids", sb);
                        amcJsonObject.put("created_date", AppUtils.getCurrentDate());
                        amcJsonObject.put("updated_date", AppUtils.getCurrentDate());

                        Log.d(TAG, "-------contactsJsonObject----json---" + amcJsonObject.toString());
                         httpConnectionManager.sendAMCRequest(postAMCCallBack(), amcJsonObject);
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            Log.d(TAG,"-------ID:" + buttonView.getTag()+"::::"+ isChecked);
            if (isChecked) {
                selectedAMCList.add(buttonView.getTag().toString());
            } else {
                selectedAMCList.remove(buttonView.getTag().toString());
            }
           // getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };

//    View.OnClickListener getOnClickDoSomething(final Button button) {
//        return new View.OnClickListener() { public void onClick(View v) {
//            Log.d(TAG, "-------ID:" + button.getTag());
//            Log.d(TAG,"-------ID:" + button.getText().toString());
//
//            sb.append(button.getTag());
//            sb.append(",");
//        }
//        };
//    }




    public void showDialog(Activity activity, String title, String content, String button){

        final StandardDialogFragment sdf = StandardDialogFragment.newInstance();
        sdf.setSingleButtonDialog(title,content,
                button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sdf.dismiss();
                    }
                });
        sdf.show(getSupportFragmentManager(), "amc_request");

    }


    public CallbackUtils.AMCCallBack postAMCCallBack() {
        return new CallbackUtils.AMCCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {

            }

            @Override
            public void setSuccessJson(JSONObject jsonObject) {

                try {
                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Contact Updated::" + responseCode + "::::" + updatedTimeStamp);


                    if (responseCode == APIConstants.SUCCESS) {
                        // Toast.makeText(ContactsActivity.this, "Contact Updated Successfully.", Toast.LENGTH_SHORT).show();

                        showDialog(mActivity, getString(R.string.amc_request), getString(R.string.amc_request_content),
                                getString(R.string.amc_request_dialog_button));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }


}

