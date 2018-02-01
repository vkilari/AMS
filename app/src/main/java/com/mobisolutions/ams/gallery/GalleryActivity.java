package com.mobisolutions.ams.gallery;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;

import com.mobisolutions.ams.BaseActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.uikit.widget.StandardButton;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by vkilari on 10/11/17.
 */

public class GalleryActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = GalleryActivity.class.getName();
    private StandardButton yearButton;
    static Dialog d ;
    private GridView gridView;
    private GalleryViewAdapter galleryViewAdapter;

    int year = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);
        yearButton = (StandardButton) findViewById(R.id.year);
        gridView = (GridView) findViewById(R.id.gridView);
        galleryViewAdapter = new GalleryViewAdapter(this, R.layout.layout_gallery_category, getData());
        gridView.setAdapter(galleryViewAdapter);

        yearButton.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                Log.d(TAG, "" + item.getTitle() + "" + item.getImage());

            }

        });
    }


    @Override
    public void onClick(View v) {

            switch (v.getId()) {
                case R.id.year:
                    showYearDialog();
                    break;
            }
    }


    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

    public void showYearDialog()
    {

        final Dialog d = new Dialog(GalleryActivity.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.layout_year_dialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                yearButton.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }


}
