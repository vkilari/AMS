package com.mobisolutions.ams;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.mobisolutions.ams.directions.DirectionsActivity;
import com.mobisolutions.ams.gallery.GalleryActivity;
import com.mobisolutions.ams.meetings.MeetingRequestActivity;
import com.mobisolutions.ams.uikit.widget.StandardDialogFragment;
import com.mobisolutions.ams.utils.AppUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by vkilari on 7/16/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public static final String CURRENT_PERMISSIONS_STATE = "currentPermissionsState";

    public static final String LAST_STATE = "lastState";

    protected static final int PERMISSION_REQUEST_CODE = 10101;

    protected static final int NEED_PERMISSIONS = 0;

    protected static final int ASKING_PERMISSIONS = 1;

    protected static final int DENIED_PERMISSIONS = 2;

    protected static final int GRANTED_PERMISSIONS = 3;

    protected int currentPermissionsState = NEED_PERMISSIONS;

    protected static String lastState;

    public Toolbar mActionBarToolbar;
    private DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    public ActionBarDrawerToggle mToggle;
    private boolean showDrawerIndicator = true;

    /** Set the lastState value
     *
     * @param lastState
     */
    protected static void setLastState(String lastState) {
        BaseActivity.lastState = lastState;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      // setContentView(getResourceId());



    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //setTitle("Activity Title");


        getActionBarToolbar();

        setupNavDrawer();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            }
        }

    }

    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                // Depending on which version of Android you are on the Toolbar or the ActionBar may be
                // active so the a11y description is set here.
                mActionBarToolbar.setNavigationContentDescription(getResources()
                        .getString(R.string.navdrawer_description_a11y));
                //setSupportActionBar(mActionBarToolbar);

                if (useToolbar()) { setSupportActionBar(mActionBarToolbar);
                } else { mActionBarToolbar.setVisibility(View.GONE); }

            }
        }

        return mActionBarToolbar;
    }


    protected boolean useToolbar() {
        return true;
    }


    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     * @return
     */
    public void useDrawerToggle(boolean showDrawer) {
        showDrawerIndicator = showDrawer;
    }


    private void setupNavDrawer() {


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }

        Log.d(TAG, "-----showDrawerIndicator----"+showDrawerIndicator);

        // use the hamburger menu
        if( showDrawerIndicator) {
            mToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mActionBarToolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(mToggle);

            mToggle.setDrawerIndicatorEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            mToggle.syncState();
        }
        else if(!showDrawerIndicator ) {
            // Use home/back button instead
            mToggle.setDrawerIndicatorEnabled(false);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat
                    .getDrawable(this, R.mipmap.back));
        }


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "Config changed!: " + newConfig.toString());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PERMISSIONS_STATE, currentPermissionsState);
        outState.putString(LAST_STATE, lastState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        BaseActivity.setLastState(savedState.getString(LAST_STATE));
        currentPermissionsState = savedState.getInt(CURRENT_PERMISSIONS_STATE);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.my_grocery:
              //  startActivity(new Intent(this, Item1Activity.class));
                // finish();
                //createBackStack(new Intent(this, CatageoryActivity.class));
                break;

            case R.id.nav_2:

             //   startActivity(new Intent(this, Item1Activity.class));
                break;

            case R.id.nav_3:

               // startActivity(new Intent(this, Item1Activity.class));
                break;

            case R.id.gallery:

                 startActivity(new Intent(this, GalleryActivity.class));
                break;

            case R.id.directions:

                startActivity(new Intent(this, DirectionsActivity.class));
                break;

            case R.id.locateMe:

              //  startActivity(new Intent(this, LocationActivity.class));
                break;

            case R.id.meetingRequest:

                startActivity(new Intent(this, MeetingRequestActivity.class));
                break;

            case R.id.maintenance:

               // startActivity(new Intent(this, MaintenanceActivity.class));
                break;
            case R.id.logout:
               // prefUtils.clearLoggedInEmailAddress(this);
               // startActivity(new Intent(this, LoginActivity.class));
                break;


        }


        //closeNavDrawer();
        // overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

        return true;
    }

    /**
     * Get the list of permissions that are required for this activity API 23 will send these requests to the user for approval
     *
     * @return a list of permissions that the user has not yet allowed
     */
    @SuppressWarnings("squid:S1168")
    @Nullable
    protected Set<String> getRequiredPermissions() {
        return null;
    }

    protected CharSequence getPermissionRationale() {
        return null;
    }


    protected void requestActivityPermissions() {
        // Not a dessert we need to worry about -
        // This activity doesn't neeed any permissions
        // -- either way - call through to Result
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || getRequiredPermissions() == null) {
            handleRequestPermissions(null);
            return;
        }

        // Create a copy, as getRequiredPermissions should be unmodifiable
        final Set<String> permissions = new HashSet<>();
        permissions.addAll(getRequiredPermissions());

        // No requests to worry about - call through to Result
        if (AppUtils.isEmpty(permissions)) {
            handleRequestPermissions(null);
            return;
        }

        boolean showRationale = defaultShouldShowRationale();

        // Find out which ones are valid and needed
        Iterator<String> permissionElements = permissions.iterator();
        while (permissionElements.hasNext()) {

            String currentPermission = permissionElements.next();
            // If the element is invalid, or we already have permission, remove it
            if (TextUtils.isEmpty(currentPermission)
                    || PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(currentPermission)) {
                permissionElements.remove();
                continue;
            }
            Log.e(TAG, "Permission: " + currentPermission);

            showRationale = showRationale || shouldShowRationale(currentPermission);

        }

        // No requests to worry about - call through to Result
        if (AppUtils.isEmpty(permissions)) {
            handleRequestPermissions(null);
            return;
        }

        // If we should show Rational, and we have one, display it
        // Otherwise, go ahead and ask again...
        if (defaultShouldShowRationale() && showRationale && !TextUtils.isEmpty(getPermissionRationale())) {
            handleShowPrePermissionRationaleDialog(permissions);
        } else {
            handleRequestPermissions(permissions.toArray(new String[]{}));
        }
    }

    /**
     * Wrapper function that only calls requestPermissions if we are running marshmallow. If we are not running marshmallow, or there are
     * no permissions to request, we fall to result with 'success'
     *
     * @param requestedPermissions array of String values that are being requested
     */
    public final void handleRequestPermissions(String[] requestedPermissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestedPermissions != null && requestedPermissions.length != 0) {
            requestPermissions(requestedPermissions, BaseActivity.PERMISSION_REQUEST_CODE);
            return;
        }

        onRequestPermissionsResult(BaseActivity.PERMISSION_REQUEST_CODE, new String[]{""}, new int[]{PackageManager.PERMISSION_GRANTED});
    }

    protected boolean defaultShouldShowRationale() {
        return false;
    }

    private boolean shouldShowRationale(String currentPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return shouldShowRequestPermissionRationale(currentPermission);
        }

        return false;
    }

    protected void handleShowPrePermissionRationaleDialog(final Set<String> permissions) {
        final StandardDialogFragment sdf = StandardDialogFragment.newInstance();
        sdf.setSingleButtonDialog(getString(R.string.permissions_title), getPermissionRationale(),
                getString(R.string.permissions_request_positive), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleRequestPermissions(permissions.toArray(new String[permissions.size()]));
                        sdf.dismiss();
                    }
                });
        sdf.show(getSupportFragmentManager(), String.valueOf(getPermissionRationale().hashCode()));
    }

    protected void launchAppPermissions() {
        // TODO - Find a way to launch straight into App Permissions
        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}