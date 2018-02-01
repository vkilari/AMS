package com.mobisolutions.ams.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.contacts.ContactDetailsActivity;
import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.contacts.OnItemClickListener;
import com.mobisolutions.ams.home.CategoriesBean;
import com.mobisolutions.ams.uikit.widget.CustomTextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vkilari on 8/14/17.
 */

public class CategoryGridAdapter extends BaseAdapter
{
    private ArrayList<CategoriesBean> categoriesBeanArrayList;
    private Activity activity;
    private int[] backgroundColor = {R.color.mcd_green_btn, R.color.mcd_deals_relative, R.color.mcd_wotd_promotion_container_background, R.color.mcd_deals_percent, R.color.mcd_enabled_yellow_button_key_line_color, R.color.ams_dark_cyan};
    private ArrayAdapter<String> mPopupAdapter;
    private ArrayList<String> mOptionsArray =
            new ArrayList<>(Arrays.asList("Share", "Call", "Message"));
    private ListPopupWindow mPopupWindow;
    private Context mContext;
    private RelativeLayout mLayout;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;

    public CategoryGridAdapter(Activity activity, ArrayList<CategoriesBean> categories, RelativeLayout layout) {
        super();
        this.categoriesBeanArrayList = categories;
        this.activity = activity;
        this.mLayout = layout;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categoriesBeanArrayList.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return categoriesBeanArrayList.get(position).getCategoryName();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    public static class ViewHolder
    {
        public ImageView category_overflow;
        public CustomTextView txtViewTitle;
        public FrameLayout layoutView;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.home_category_cards, null);

            int height = mLayout.getHeight();
            Log.d("TAG", "------------Height"+height);

            convertView.setMinimumHeight(height/3);
            view.layoutView = (FrameLayout) convertView.findViewById(R.id.layout_row);
            view.txtViewTitle = (CustomTextView) convertView.findViewById(R.id.category_name);
            view.category_overflow = (ImageView) convertView.findViewById(R.id.category_overflow);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.category_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Row " + position + " was clicked!", Toast.LENGTH_SHORT).show();

                setupPopupWindow(position, v);
            }
        });



        view.txtViewTitle.setText(categoriesBeanArrayList.get(position).getCategoryName());
        view.layoutView.setBackgroundColor(Color.parseColor(categoriesBeanArrayList.get(position).getCategoryColor()));
       // view.imgViewFlag.setImageResource(listFlag.get(position));

        return convertView;
    }


    private void setupPopupWindow(int position, View v)
    {
        mPopupAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mOptionsArray);
        mPopupWindow = new ListPopupWindow(activity);
        mPopupWindow.setAdapter(mPopupAdapter);
        mPopupWindow.setAnchorView(v);
        mPopupWindow.setWidth(500);
        mPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.color.mcd_grey_bg));
        mPopupWindow.setHorizontalOffset(-380); //<--this provides the margin you need
        //if you need a custom background color for the popup window, use this line:
        //mPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(activity, R.color.ams_white)));
        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(activity, "Row " + position + " was clicked!", Toast.LENGTH_SHORT).show();

                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.show();

    }


}