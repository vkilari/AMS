package com.mobisolutions.ams.mygrocery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;

public class MyGroceryActivity extends BaseActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grocery);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.grocery_header));
        setSupportActionBar(toolbar);

    }
}
