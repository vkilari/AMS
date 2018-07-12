package com.mobisolutions.ams.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.mobisolutions.ams.AMSApplication;
import com.mobisolutions.ams.APIServiceManager;
import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.adapter.CategoryGridAdapter;
import com.mobisolutions.ams.amc.AMC;
import com.mobisolutions.ams.amc.AMCActivity;
import com.mobisolutions.ams.calendar.CalendarActivity;
import com.mobisolutions.ams.config.Settings;
import com.mobisolutions.ams.contacts.ContactDetailsActivity;
import com.mobisolutions.ams.contacts.Contacts;
import com.mobisolutions.ams.contacts.ContactsActivity;
import com.mobisolutions.ams.database.DBHelper;
import com.mobisolutions.ams.gallery.GalleryActivity;
import com.mobisolutions.ams.http.CallbackUtils;
import com.mobisolutions.ams.http.HttpConnectionManager;
import com.mobisolutions.ams.maintenance.GeneralMaintenanceActivity;
import com.mobisolutions.ams.mobilet.MobiletActivity;
import com.mobisolutions.ams.mygrocery.MyGroceryActivity;
import com.mobisolutions.ams.services.ServicesActivity;
import com.mobisolutions.ams.utils.APIConstants;
import com.mobisolutions.ams.utils.AppUtils;
import com.mobisolutions.carouselview.CarouselView;
import com.mobisolutions.carouselview.ImageClickListener;
import com.mobisolutions.carouselview.ImageListener;
import com.mobisolutions.carouselview.ViewListener;
import com.amazonaws.mobileconnectors.pinpoint.*;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by vkilari on 7/16/17.
 */

public class HomeActivity extends BaseActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();

    CarouselView customCarouselView;
    private CategoryGridAdapter mAdapter;
    private ArrayList<String> listCountry;
    private ArrayList<Integer> listFlag;
    private GridView gridView;
    public static HttpConnectionManager httpConnectionManager;
    DBHelper dbHelper;
    private ArrayList<String> updatedServices;
    ArrayList<Banners> bannersArrayList;
    ArrayList<CategoriesBean> categoriesArrayList;
    SQLiteDatabase db;
    ImageView category_overflow;
    private ArrayAdapter<String> mPopupAdapter;
    private ArrayList<String> mOptionsArray =
            new ArrayList<>(Arrays.asList("Share", "Call", "Message"));
    private ListPopupWindow mPopupWindow;
    private Context mContext;
    BottomSheetBehavior sheetBehavior;

    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};
    String[] sampleTitles = {"Orange", "Grapes", "Strawberry", "Cherry", "Apricot"};

    String[] sampleNetworkImageURLs = {
            "http://dc1vk.kpvsolutions.in:9080/apartmentservices/images/image_1.jpg",
            "http://dc1vk.kpvsolutions.in:9080/apartmentservices/images/image_2.jpg",
            "http://dc1vk.kpvsolutions.in:9080/apartmentservices/images/image_3.jpg",
            "http://dc1vk.kpvsolutions.in:9080/apartmentservices/images/image_4.jpg",
            "http://dc1vk.kpvsolutions.in:9080/apartmentservices/images/image_5.jpg"
    };
    private int width;
    private int height;
    private RelativeLayout categoryLayout;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryLayout = (RelativeLayout) findViewById(R.id.categoryLayout);
        gridView = (GridView) findViewById(R.id.groceryGridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        httpConnectionManager = new HttpConnectionManager(this);
        mContext = this;

        //API Calling
       // httpConnectionManager.getBannersResponse(getBannersGetCallBack(1));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        final ViewTreeObserver observer= categoryLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Log.d("Log", "Height: " + categoryLayout.getHeight());
                        height = categoryLayout.getHeight();
                    }
                });


        Log.d(TAG, "category Layout height::::"+height);
        httpConnectionManager.getSettingsResponse(getSettingsGetCallBack(1));

        //Make database call
        dbHelper = AMSApplication.getDBHelper();
        db = dbHelper.getWritableDatabase();


        updatedServices = new ArrayList<>();

        customCarouselView = (CarouselView) findViewById(R.id.customCarouselView);
        loadBannerView();
        //customCarouselView.setPageCount(sampleNetworkImageURLs.length);
        customCarouselView.setSlideInterval(4000);

        //customCarouselView.setViewListener(viewListener);

        customCarouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(HomeActivity.this, "Clicked item: "+ position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HomeActivity.this, BannerDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_bottom, 0);

            }
        });


        //loadCategoryView(categoryLayout);
        CategoriesTaskRunner runner = new CategoriesTaskRunner();

        runner.execute();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //Toast.makeText(ServiceActivity.this, mAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                Log.d(TAG, "---------ServiceDetailsActivity---------"+mAdapter.getItem(position));

                Intent intent=null;
                if (position == 0) {
                    intent = new Intent(HomeActivity.this, ContactsActivity.class);

                } else if (position == 1) {
                    intent = new Intent(HomeActivity.this, AMCActivity.class);


                } else if (position == 2) {
                    intent = new Intent(HomeActivity.this, ServicesActivity.class);

                } else if (position == 3) {
                    intent = new Intent(HomeActivity.this, CalendarActivity.class);

                } else if (position == 4) {
                    intent = new Intent(HomeActivity.this, MobiletActivity.class);

                } else if (position == 5) {
                    intent = new Intent(HomeActivity.this, GeneralMaintenanceActivity.class);

                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_bottom, 0);

//                Fragment serviceDetailsFragment = new ServiceDetailsFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container, serviceDetailsFragment, "NewFragmentTag");
//                ft.addToBackStack(null);
//                ft.commit();

            }
        });

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            Log.d(TAG, "---------bannersArrayList.get(position).getBannerImage()---------"+bannersArrayList.get(position).getBannerImage());

            Picasso.with(getApplicationContext()).load(bannersArrayList.get(position).getBannerImage()).placeholder(sampleImages[0]).error(sampleImages[3]).fit().centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
        }
    };


    public static void setAccessibilityTraversalAfter(View before, View after) {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && null != after && after.getId() != 0 && null != before) {
            before.setAccessibilityTraversalAfter(after.getId());
        }
    }

    // To set custom views
    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            Log.d(TAG, "---------Home page Banner setViewForPosition---------"+mAdapter.getItem(position) + "::::::"+position);

            View customView = HomeActivity.this.getLayoutInflater().inflate(R.layout.layout_carousel_image_view, null);

            TextView labelTextView = (TextView) customView.findViewById(R.id.labelTextView);
            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);

            Log.d(TAG, "---------sampleNetworkImageURLs[position]---------"+sampleNetworkImageURLs[position]);

            return customView;
        }
    };



    public  CallbackUtils.SettingsCallBack  getSettingsGetCallBack(final int value)
    {
        return new CallbackUtils.SettingsCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Settings Error::::"+jsonObject);
            }
            @Override
            public void setSettingsJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Settings::::"+jsonObject);

                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Banners::::"+responseCode+"::::"+updatedTimeStamp);


                    if (responseCode == APIConstants.SUCCESS) {
                        JSONArray jsonArray = jsonObject.getJSONArray("api_settings");
                        ArrayList<Settings> settingsArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Settings settings = new Settings();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            settings.setSettingID(jsonObject1.getString("service_id"));
                            settings.setServiceName(jsonObject1.getString("service_name"));
                            settings.setServiceUpdatedDate(jsonObject1.getString("updated_date"));

                            settingsArrayList.add(settings);
                        }

                        compareAPIServices(settingsArrayList);
                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Settings::"+e.getMessage());
                    e.printStackTrace();
                }


            }


        };
    }




    public void loadBannerView() {

        bannersArrayList = dbHelper.getAllBanners();
        customCarouselView.setPageCount(bannersArrayList.size());
        customCarouselView.setImageListener(imageListener);


    }

    public void loadCategoryView(RelativeLayout mLayout) {

        ArrayList<CategoriesBean> homePageGridCategories = AMSApplication.getDBHelper().getAllCategories();

        mAdapter = new CategoryGridAdapter(this,homePageGridCategories, mLayout);

        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    public void compareAPIServices(ArrayList<Settings> settingsArrayList){

        ArrayList<Settings> localSettings = dbHelper.getSettings();

        Log.d(TAG, "-----localSettings::::"+localSettings.size());

        for (int i = 0; i<settingsArrayList.size(); i++){

            String updatedDate = settingsArrayList.get(i).getServiceUpdatedDate();
            String currentLocalDate = localSettings.get(i).getServiceUpdatedDate();

            Log.d(TAG, "-----compare dates"+settingsArrayList.get(i).getSettingID() +":::servicedate::"+ updatedDate+"::localcate::"+currentLocalDate);
            Date serviceDate = AppUtils.stringToDate(updatedDate);
            Date localDate = AppUtils.stringToDate(currentLocalDate);

            if (serviceDate.compareTo(localDate) > 0) {
                Log.d(TAG, "-----compare dates serviceDate is after localDate ");
                updatedServices.add(settingsArrayList.get(i).getSettingID());
                syncLocalDatabase(Integer.parseInt(settingsArrayList.get(i).getSettingID()), settingsArrayList.get(i).getServiceUpdatedDate());
            } else if (serviceDate.compareTo(localDate) < 0) {
                Log.d(TAG, "-----compare dates serviceDate is before localDate");
            } else if (serviceDate.compareTo(localDate) == 0) {
                Log.d(TAG, "-----compare dates serviceDate is equal to localDate");
            } else {
                Log.d(TAG, "-----compare dates How to get here?");
            }
        }


    }

    private void syncLocalDatabase(int settingID, String serviceUpdatedDate) {

       switch (settingID) {
           case APIConstants.BANNER :
               Log.d(TAG, "-----Service banners is updated-----");
               httpConnectionManager.getBannersResponse(getBannersGetCallBack(settingID, serviceUpdatedDate));
               break;
           case APIConstants.CATEGORIES :
               httpConnectionManager.getCategoriesResponse(getCategoriesGetCallBack(settingID, serviceUpdatedDate));
               Log.d(TAG, "-----Service categories is updated-----");
               break;
           case APIConstants.CONTACTS :
               Log.d(TAG, "-----Service contacts is updated-----");
               httpConnectionManager.getContactsResponse(new APIServiceManager(mContext).getContactsGetCallBack(settingID, serviceUpdatedDate, new APIServiceManager.GetServiceUpdated() {
                   @Override
                   public void isServiceUpdated(boolean isUpdated) {

                   }

                   @Override
                   public void isMeetingServiceUpdated(boolean isUpdated) {

                   }
               }));
               break;
           case APIConstants.MAINTENANCE :
               Log.d(TAG, "-----Service maintenance is updated-----");
               break;
           case APIConstants.MEETINGS :
               Log.d(TAG, "-----Service meetings is updated-----");
               httpConnectionManager.getMeetingsHistory(new APIServiceManager(mContext).getMeetingHistoryCallBack(settingID, serviceUpdatedDate, new APIServiceManager.GetServiceUpdated() {
                   @Override
                   public void isServiceUpdated(boolean isUpdated) {

                   }

                   @Override
                   public void isMeetingServiceUpdated(boolean isUpdated) {

                   }
               }));
               break;
           case APIConstants.AMC :
               Log.d(TAG, "-----Service AMC is updated-----");

               httpConnectionManager.getAMCResponse(new APIServiceManager(mContext).getAMCGetCallBack(APIConstants.AMC));
               
               break;

           case APIConstants.HOME_SERVICES :
               Log.d(TAG, "-----Service HOME_SERVICES is updated-----");

               httpConnectionManager.getHomeServicesResponse(new APIServiceManager(mContext).getHomeServicesGetCallBack(APIConstants.HOME_SERVICES));

               break;
       }
    }


    public  CallbackUtils.BannersCallBack  getBannersGetCallBack(final int settingID, final String serviceUpdatedDate)
    {
        return new CallbackUtils.BannersCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Banners Error::::"+jsonObject);
            }
            @Override
            public void setBannersJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Banners::::"+jsonObject);
                ArrayList<Banners> bannersArrayList = new ArrayList<>();
                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Banners::::"+responseCode+"::::"+updatedTimeStamp);
                    if (responseCode == APIConstants.SUCCESS) {
                        JSONArray jsonArray = jsonObject.getJSONArray("banners");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Banners banners = new Banners();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            banners.setBanner_Id(jsonObject1.getString("banner_id"));
                            banners.setBannerName(jsonObject1.getString("banner_name"));
                            banners.setBannerType(jsonObject1.getString("banner_type"));
                            banners.setBannerProvider(jsonObject1.getString("banner_provider"));
                            banners.setPromotionStart(jsonObject1.getString("promotion_start"));
                            banners.setPromotionEnd(jsonObject1.getString("promotion_end"));
                            banners.setBannerImage(jsonObject1.getString("banner_image"));
                            banners.setBannerDetails(jsonObject1.getString("banner_details"));

                            bannersArrayList.add(banners);
                        }

                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Banners::"+e.getMessage());
                }

                    if (bannersArrayList.size() > 0) {
                        dbHelper.deleteBanners();

                        dbHelper.insertBanners(bannersArrayList);

                        dbHelper.updateSettings(settingID, serviceUpdatedDate);

                        loadBannerView();

                    }


            }


        };
    }


    public  CallbackUtils.HomeServices  getHomeServicesGetCallBack(final int settingID)
    {
        final String updatedTimeStamp="";
        return new CallbackUtils.HomeServices() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Contacts Error::::"+jsonObject);
            }
            @Override
            public void setSuccessJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Contacts::::"+jsonObject);
                ArrayList<AMC> amcArrayList = new ArrayList<>();
                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Contacts::::"+responseCode+"::::"+updatedTimeStamp);
                    if (responseCode == APIConstants.SUCCESS) {


                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Contacts::"+e.getMessage());
                }

                if (amcArrayList.size() > 0) {
                    dbHelper.deleteHomeServices();

                    dbHelper.getHomeServices();

                    dbHelper.updateSettings(settingID, updatedTimeStamp);

                    //getServiceUpdated.isServiceUpdated(true);

                }


            }


        };
    }


    public  CallbackUtils.HomeCategoriesCallBack  getCategoriesGetCallBack(final int settingID, final String serviceUpdatedDate)
    {
        return new CallbackUtils.HomeCategoriesCallBack() {
            @Override
            public void setErrorJson(JSONObject jsonObject) {
                Log.d(TAG, "---------Getting Categories Error::::"+jsonObject);
            }
            @Override
            public void setCategoriesJson(JSONObject jsonObject) {

                Log.d(TAG, "---------Getting Categories::::"+jsonObject);
                ArrayList<CategoriesBean> categoriesArrayList = new ArrayList<>();
                try {

                    int responseCode = Integer.parseInt(jsonObject.getString("response_code"));
                    String responseMessage = jsonObject.getString("response_status");
                    String updatedTimeStamp = jsonObject.getString("timestamp");
                    Log.d(TAG, "---------Getting Categories::::"+responseCode+"::::"+updatedTimeStamp);
                    if (responseCode == APIConstants.SUCCESS) {
                        JSONArray jsonArray = jsonObject.getJSONArray("home_categories");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            CategoriesBean categoriesBean = new CategoriesBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            categoriesBean.setCategoryId(jsonObject1.getString("category_id"));
                            categoriesBean.setCategoryName(jsonObject1.getString("category_name"));
                            categoriesBean.setCategoryColor(jsonObject1.getString("category_color"));
                            categoriesBean.setCategoryImage(jsonObject1.getString("category_image"));

                            categoriesArrayList.add(categoriesBean);
                        }

                    }

                }catch (Exception e) {
                    Log.e(TAG, "Exception while reading Banners::"+e.getMessage());
                }

                if (categoriesArrayList.size() > 0) {
                    dbHelper.deleteCategories();

                    dbHelper.insertCategories(categoriesArrayList, db);

                    dbHelper.updateSettings(settingID, serviceUpdatedDate);

                    loadCategoryView(categoryLayout);
                }


            }


        };
    }


    private class CategoriesTaskRunner extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {

            try {


                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressBar.setVisibility(View.GONE);
            loadCategoryView(categoryLayout);

        }


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }



    }


}
