package com.mobisolutions.ams.login;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.uikit.widget.CustomEditText;
import com.mobisolutions.ams.uikit.widget.CustomTextView;

/**
 * Created by vkilari on 8/12/17.
 */

public class OTPFragment extends Fragment implements View.OnClickListener {


    private LoginActivity mActivity;
    private CustomTextView otp_sub_header;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (LoginActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



         initViews(view);
        //  initListeners();
    }

    private void initViews(View view) {

        otp_sub_header = (CustomTextView) view.findViewById(R.id.otp_sub_header);

        Resources res = getResources();
        String subHeader = String.format(res.getString(R.string.otp_sub_header), "9550284999");

        otp_sub_header.setText(subHeader);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((LoginActivity) getActivity()).setToolbarHeader(getActivity().getString(R.string.forgot_password_title));
    }

    @Override
    public void onClick(View v) {

    }
}