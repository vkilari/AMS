package com.mobisolutions.ams.services;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;

import com.mobisolutions.ams.AMSApplication;
import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.amc.AMC;
import com.mobisolutions.ams.gallery.ImageItem;
import com.mobisolutions.ams.home.HomeActivity;
import com.mobisolutions.ams.http.CallbackUtils;
import com.mobisolutions.ams.utils.APIConstants;

import org.json.JSONObject;

import java.util.ArrayList;

public class ServicesActivity extends BaseActivity {

    public static final String TAG = ServicesActivity.class.getSimpleName();

    private String[] services = {"Water Cans", "Rice", "Plumber", "Electroriction", "Painter", "Water Tank", "Water Cans",};
    private int[] serviceImages = {R.mipmap.ic_watercan_thumb, R.mipmap.ic_watercan_thumb, R.mipmap.ic_plumber_thumb, R.mipmap.ic_electrician_thumb, R.mipmap.ic_plumber_thumb,  R.mipmap.ic_watercan_thumb, R.mipmap.ic_watercan_thumb};
    private GridView gridView;
    private ServiceGridViewAdapter gridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_services);


        ArrayList<ServiceItemBean> serviceItemBeans = AMSApplication.getDBHelper().getHomeServices();


        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new ServiceGridViewAdapter(this, R.layout.layout_grid_services_item, serviceItemBeans);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ServiceItemBean item = (ServiceItemBean) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(ServicesActivity.this, ServiceDetailsActivity.class);
                intent.putExtra("title", item.getServiceName());
                intent.putExtra("image", item.getServiceImage());
                //Start details activity
                startActivity(intent);
            }
        });


    }


    private ArrayList<ServiceItemBean> getData() {
        final ArrayList<ServiceItemBean> imageItems = new ArrayList<>();
       // TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
//        for (int i = 0; i < serviceImages.length; i++) {
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), serviceImages[i]);
//            imageItems.add(new ServiceItemBean(bitmap, services[i]));
//        }
        return imageItems;
    }




}
