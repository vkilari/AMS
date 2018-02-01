package com.mobisolutions.ams.login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.config.ApplicationContext;

/**
 * Created by vkilari on 8/5/17.
 */

public class LoginActivity extends AppCompatActivity{

    private static String TAG = LoginActivity.class.getSimpleName();

    protected static final String METHOD_NOT_USED = "Un-used Method";

    Toolbar toolbar;
    private LoginActivity mActivity;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarHeader(getString(R.string.log_in_header));

       // setToolbarTitle(R.string.log_in_header);


        ApplicationContext.init(mContext);

        if (findViewById(R.id.fragment_container) != null) {

            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public void setToolbarHeader(String title) {

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        mTitle.setText(title);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    protected void showFragment(Fragment fragment) {

        TAG = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void backstackFragment() {
        Log.d("Stack count", getSupportFragmentManager().getBackStackEntryCount() + "");
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
        getSupportFragmentManager().popBackStack();
        removeCurrentFragment();
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment currentFrag = getFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    protected void enableNavigationIcon() {

        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backstackFragment();
            }
        });
    }

    protected void disableNavigationIcon() {
        toolbar.setNavigationIcon(null);
    }

    protected void setToolbarTitle(int resID) {
        toolbar.setTitle(resID);
    }



}
