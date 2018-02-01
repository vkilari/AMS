package com.mobisolutions.ams.mobilet;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 1/18/18.
 */

public class CardFrontFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_mobilet_card_front, container, false);
    }
}
