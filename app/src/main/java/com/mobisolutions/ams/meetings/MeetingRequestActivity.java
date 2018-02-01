package com.mobisolutions.ams.meetings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.calendar.CalendarActivity;
import com.mobisolutions.ams.http.CallbackUtils;
import com.mobisolutions.ams.http.HttpConnectionManager;
import com.mobisolutions.ams.uikit.widget.StandardButton;
import com.mobisolutions.ams.utils.APIConstants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MeetingRequestActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MeetingRequestActivity.class.getName();
    private EditText date;
    private Spinner meetingAttendies;
    private DatePickerDialog fromDatePickerDialog;
    TimePickerDialog timePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText meetingDate, meetingTime, meetingTitle, meetingDesc;
    private int mHour, mMinute;
    private StandardButton send_meeting_request;
    public static HttpConnectionManager httpConnectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_request);

        httpConnectionManager = new HttpConnectionManager(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        meetingAttendies = (Spinner) findViewById(R.id.meetingAttendies);

        meetingAttendies.setOnItemSelectedListener(new ItemSelectedListener());

        meetingDate = (EditText) findViewById(R.id.meetingDate);
        meetingTime = (EditText) findViewById(R.id.meetingTime);
        meetingTitle = (EditText) findViewById(R.id.meetingTitle);
        meetingDesc = (EditText) findViewById(R.id.meetingDesc);
        send_meeting_request = (StandardButton) findViewById(R.id.send_meeting_request);
        setEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
         ArrayList<String> meetingDetails = bundle.getStringArrayList("meeting_details");

         for (int i = 0; i<meetingDetails.size(); i++) {
             Log.d(TAG, "----Meeting Details::::"+meetingDetails.get(i));
             if (i == 0) {
                 meetingTitle.setText(meetingDetails.get(i));
             }
             if (i == 2) {
                 meetingDesc.setText(meetingDetails.get(i));
             }
             if (i == 3) {
                 meetingDate.setText(meetingDetails.get(i));
             }
             if (i == 4) {
                 meetingTime.setText(meetingDetails.get(i));
             }

         }
            setEnabled(false);
        }
        setDateField();
        setTimeField();

        send_meeting_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    JSONObject amcJsonObject = new JSONObject();
                    amcJsonObject.put("apartment_id", "sssn101");
                    amcJsonObject.put("meeting_date", "2018-01-31");

                    httpConnectionManager.sendMeetingRequest(postMeetingCallBack(), amcJsonObject);

                }catch (Exception e) {
                    Log.e(TAG, "Error while sending meeting request:::"+e.getMessage());
                }

            }
        });
    }


    public CallbackUtils.MeetingsCallBack postMeetingCallBack() {
        return new CallbackUtils.MeetingsCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {

            }

            @Override
            public void setMeetingsJson(JSONObject jsonObject) {

                try {
                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Meeting Request::" + responseCode + "::::" + updatedTimeStamp);


                    if (responseCode == APIConstants.SUCCESS) {


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
    }

    private void setEnabled(boolean isEnabled) {
        meetingTitle.setEnabled(isEnabled);
        meetingDesc.setEnabled(isEnabled);
        meetingDate.setEnabled(isEnabled);
        meetingTime.setEnabled(isEnabled);
    }

    @Override
    public void onClick(View view) {
        if(view == meetingDate) {
            fromDatePickerDialog.show();
        }  if(view == meetingTime) {
            timePickerDialog.show();
        }
    }

    private void setTimeField() {
        meetingTime.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        meetingTime.setText(hourOfDay + ":" + minute);


                    }
                }, mHour, mMinute, false);

    }

    private void setDateField() {
        meetingDate.setOnClickListener(this);


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                meetingDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));




        fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);





    }


    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        String firstItem = String.valueOf(meetingAttendies.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(meetingAttendies.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
                Toast.makeText(parent.getContext(),
                        "You have selected : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();
                // Todo when item is selected by the user
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }

    }

}
