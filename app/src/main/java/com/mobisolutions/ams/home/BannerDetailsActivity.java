package com.mobisolutions.ams.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 12/22/17.
 */

public class BannerDetailsActivity extends BaseActivity {

    public static final String TAG = BannerDetailsActivity.class.getSimpleName();

    private WebView mWebView;
    private static final String URL = "http://dc1vk.kpvsolutions.in:9080/apartmentservices/html/banner.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_banner_details);

        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mWebView = (WebView) findViewById(R.id.webView);

        mToggle.setDrawerIndicatorEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Banner Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat
                .getDrawable(this, R.mipmap.back));

        mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Doesn't have to be onBackPressed
                onBackPressed();
            }
        });

        mWebView.loadUrl(URL);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //this.setContentView(mWebView);


    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
