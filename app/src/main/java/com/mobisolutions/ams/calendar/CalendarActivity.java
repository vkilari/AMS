package com.mobisolutions.ams.calendar;

import android.app.Dialog;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisolutions.ams.AMSApplication;
import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.contacts.ContactsActivity;
import com.mobisolutions.ams.home.HomeActivity;
import com.mobisolutions.ams.meetings.MeetingRequestActivity;
import com.mobisolutions.ams.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by vkilari on 11/23/17.
 */

public class CalendarActivity extends BaseActivity {

    private static final String TAG = CalendarActivity.class.getName();
    // /Users/vkilari/Downloads/SectionedExpandableGridRecyclerView-master-master

    RecyclerView mRecyclerView;
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    ArrayList<String> yearList;
    private static final int CONTENT_VIEW_ID = 10101010;
    HashMap<String, List<Calendar>> calendarMap = new HashMap<>();
    HashMap<String, List<Calendar>> finalCalendarMap = new HashMap<>();

    ExpandableListView expandableListView;
    CalendarAdapter calendarAdapter;
    List<String> expandableListTitle;
    List<String> calendarList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calendar);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListTitle = new ArrayList<String>();

        DisplayMetrics  metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;



        expandableListView.setIndicatorBounds(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));

        ArrayList<Calendar> calendarEvents = AMSApplication.getDBHelper().getCalendarEvents();

        for (int i=0; i<calendarEvents.size(); i++) {

            String year = AppUtils.getYear(calendarEvents.get(i).getCalendarDate());
            calendarList.add(calendarEvents.get(i).getCalendarDate());
            calendarMap.put(year, calendarEvents);

        }



        for ( String key : calendarMap.keySet() ) {
            expandableListTitle.add(key);
            List<Calendar> dates = new ArrayList<Calendar>();

            for (int i = 0; i < calendarEvents.size(); i++) {

                finalCalendarMap = AppUtils.getMonths(calendarEvents.get(i).getCalendarDate(), key, dates, calendarEvents.get(i).getCalendarTitle(), calendarEvents.get(i).getCalendarType(), calendarEvents.get(i).getCalendarDescription(), calendarEvents.get(i).getCalendarDate(), finalCalendarMap);
            }
        }


        calendarAdapter = new CalendarAdapter(this, expandableListTitle, finalCalendarMap);

        expandableListView.setAdapter(calendarAdapter);


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + finalCalendarMap.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition).getCalendarTitle(), Toast.LENGTH_SHORT
//                ).show();

                ArrayList<String> meetingDetails = new ArrayList<>();
                meetingDetails.add(finalCalendarMap.get(expandableListTitle.get(groupPosition)).get(childPosition).getCalendarTitle());
                meetingDetails.add(finalCalendarMap.get(expandableListTitle.get(groupPosition)).get(childPosition).getCalendarType());
                meetingDetails.add(finalCalendarMap.get(expandableListTitle.get(groupPosition)).get(childPosition).getCalendarDescription());
                meetingDetails.add(finalCalendarMap.get(expandableListTitle.get(groupPosition)).get(childPosition).getCalendarDate());
                meetingDetails.add(finalCalendarMap.get(expandableListTitle.get(groupPosition)).get(childPosition).getCalendarTime());

                Intent intent = new Intent(CalendarActivity.this, MeetingRequestActivity.class);
                intent.putStringArrayListExtra("meeting_details", meetingDetails);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_bottom, 0);

                return false;
            }
        });

    }

    public int GetDipsFromPixel(float pixels){
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(CalendarActivity.this);
                dialog.setContentView(R.layout.dialog_calendar_day);
                dialog.setTitle("Custom Dialog");

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

                    @Override
                    public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
                        Toast.makeText(CalendarActivity.this,
                                "OnCancelListener",
                                Toast.LENGTH_LONG).show();
                    }});

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

                    @Override
                    public void onDismiss(DialogInterface dialog) {
// TODO Auto-generated method stub
                        Toast.makeText(CalendarActivity.this,
                                "OnDismissListener",
                                Toast.LENGTH_LONG).show();
                    }});

//Prepare ListView in dialog
                dialog_ListView = (ListView)dialog.findViewById(R.id.dialoglist);

                DayItemAdapter dayItemAdapter = new DayItemAdapter(CalendarActivity.this, R.layout.dialog_calendar_day_item, yearList);

                dialog_ListView.setAdapter(dayItemAdapter);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
// TODO Auto-generated method stub
                        Toast.makeText(CalendarActivity.this,
                                parent.getItemAtPosition(position).toString() + " clicked",
                                Toast.LENGTH_LONG).show();
                        dismissDialog(CUSTOM_DIALOG_ID);

                      //  DialogFragment dialog = new MeetingDetailsFragment();
                       // dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");



                    }});

                break;
        }

        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
// TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, bundle);

        switch(id) {
            case CUSTOM_DIALOG_ID:
//
                break;
        }
    }

    class DayItemAdapter extends ArrayAdapter<String> {

        public List<String> dayList;
        public DayItemAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public DayItemAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
            dayList = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.dialog_calendar_day_item, null);
            }

            String p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.day_item);
                TextView tt2 = (TextView) v.findViewById(R.id.itemType);


                if (tt1 != null) {
                    tt1.setText(dayList.get(position));
                }



            }

            return v;
        }

    }


}
