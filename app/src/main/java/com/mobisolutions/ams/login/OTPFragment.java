package com.mobisolutions.ams.login;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.SplashActivity;
import com.mobisolutions.ams.common.AppCoreConstants;
import com.mobisolutions.ams.home.HomeActivity;
import com.mobisolutions.ams.uikit.widget.CustomEditText;
import com.mobisolutions.ams.uikit.widget.CustomTextView;
import com.mobisolutions.ams.utils.AppUtils;

/**
 * Created by vkilari on 8/12/17.
 */

public class OTPFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = OTPFragment.class.getName();

    private HomeActivity mActivity;
    private CustomTextView otp_sub_header;
    private LinearLayout mOTPError;
    private CustomTextView mRegister;
    private CustomEditText mOTP;
    private LinearLayout mRegisteredError;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       // mActivity = (HomeActivity) getActivity();
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
          initListeners();
    }

    private void initViews(View view) {

        otp_sub_header = (CustomTextView) view.findViewById(R.id.otp_sub_header);
        mOTPError = (LinearLayout) view.findViewById(R.id.otp_input_layout);
        mOTP = (CustomEditText) view.findViewById(R.id.otp);
        mRegister = (CustomTextView) view.findViewById(R.id.submit_registration);
        mRegisteredError = (LinearLayout) view.findViewById(R.id.register_registered_error);
        mRegister.setContentDescription(mRegister.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button_disabled));

        Resources res = getResources();
        String subHeader = String.format(res.getString(R.string.otp_sub_header), "9550284999");

        otp_sub_header.setText(subHeader);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((LoginActivity) getActivity()).setToolbarHeader(getActivity().getString(R.string.forgot_password_title));
    }

    private void initListeners() {
        setClickListeners();
        setFocusChangeListeners();

        setListeners();
    }

    private void setListeners() {
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, AppCoreConstants.METHOD_NOT_USED);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, AppCoreConstants.METHOD_NOT_USED);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doEnablingCheck();
            }
        };

        mOTP.addTextChangedListener(mTextWatcher);

    }

    private void setFocusChangeListeners() {
        setOnFocusChangeListener(mOTP, mOTPError);


    }

    protected void resetErrorLayout(CustomEditText view, LinearLayout errorView) {
        final View inputLayout = (ViewGroup) view.getParent().getParent();
        inputLayout.setBackgroundResource(R.drawable.input_bg);
        errorView.setVisibility(View.GONE);
    }

    private void setOnFocusChangeListener(final CustomEditText textView, final LinearLayout errorView) {
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !TextUtils.isEmpty(textView.getText().toString())) {
                    resetErrorLayout(textView, errorView);
                    if (mRegisteredError.getVisibility() == View.VISIBLE) {
                        resetErrorLayout(mOTP, mOTPError);
                        mRegisteredError.setVisibility(View.GONE);
                    }
                    doEnablingCheck();
                }
            }
        });
    }



    /**
     * Sets OnClickListener on various View instances.
     */
    private void setClickListeners() {
        mRegister.setOnClickListener(this);

    }


    protected boolean validateInputs(CustomEditText view, int minChars) {
        return view.getText().length() >= minChars && view.getText().toString().trim().length()
                >= minChars;
    }


    private boolean validateAllFields() {
        boolean isValidNamePhone = validateInputs(mOTP, 6);

        return isValidNamePhone;
    }

    private boolean doEnablingCheck() {
        if (validateAllFields()) {
            enableRegister();
            return true;
        } else {
            disableRegister();
        }
        return false;
    }

    private void enableRegister() {
        mRegister.setContentDescription(mRegister.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button));
        AppUtils.enableButton(mRegister);
    }

    private void disableRegister() {
        mRegister.setContentDescription(mRegister.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button_disabled));
        AppUtils.disableButton(mRegister);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit_registration:
                register();
                break;
        }

    }

    public void register() {
        if (validateAllFields() && AppUtils.isNetworkAvailable()) {
            doRegistration();
        } else {
            if (!doEnablingCheck()) {
               // doFieldValidation(mAllFields);
            }
        }
    }

    private void doRegistration() {
        //  AppUtils.startActivityIndicator(mActivity, R.string.registering);
        //((LoginActivity) getActivity()).showFragment(new OTPFragment());

        Intent i = new Intent(getActivity(), HomeActivity.class);
        startActivity(i);
    }
}