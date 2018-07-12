package com.mobisolutions.ams.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.common.AppCoreConstants;
import com.mobisolutions.ams.uikit.widget.CustomEditText;
import com.mobisolutions.ams.uikit.widget.CustomTextView;
import com.mobisolutions.ams.utils.AppUtils;

/**
 * Created by vkilari on 7/17/17.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = RegistrationFragment.class.getName();

    private static final int SMOOTH_SCROLL_FACTOR = 10;

    private LoginActivity mActivity;
    private boolean mShowPhone;
    private int mPhoneLength = 10;
    private int maxLength = 4;
    private boolean mAllowZipCodeCharacters;
    private int mZipCodeLength;
    private String mZipCodeRegex;
    private boolean mShowZipCode;
    private CustomEditText mFirstName;
    private CustomEditText mPhone;
    private CustomEditText mEmail;
    private CustomEditText mPassword;
    private CustomEditText mAddress;
    private LinearLayout mAddressError;
    private LinearLayout mFirstNameError;
    private LinearLayout mPhoneError;
    private LinearLayout mEmailError;
    private LinearLayout mPasswordError;
    private LinearLayout mEmailRegisteredError;
    private LinearLayout mRegisteredError;
    private CustomTextView mRegister;
    private CustomTextView mResetPassword;
    private CustomTextView mErrorTextView;
    private String mPasswordValidMessage;
    private CustomEditText mAllFields;
    private ScrollView mScrollView;
    private Context mContext;


    public RegistrationFragment() {
        // Default Constructor.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

        initViews(view);

       // ((LoginActivity) getActivity()).enableNavigationIcon();
        ((LoginActivity) getActivity()).setToolbarHeader(getActivity().getString(R.string.acs_register_login_msg));

        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        doEnablingCheck();
    }


    private void initViews(View v) {

        mFirstName = (CustomEditText) v.findViewById(R.id.first_name);
        mPhone = (CustomEditText) v.findViewById(R.id.phone_number);
        mEmail = (CustomEditText) v.findViewById(R.id.email);
        mPassword = (CustomEditText) v.findViewById(R.id.password);
        mAddress = (CustomEditText) v.findViewById(R.id.address);
        mRegister = (CustomTextView) v.findViewById(R.id.register);
        mFirstNameError = (LinearLayout) v.findViewById(R.id.error_first_name);
        mAddressError = (LinearLayout) v.findViewById(R.id.error_address);
        mPhoneError = (LinearLayout) v.findViewById(R.id.error_phone_number);
        mEmailError = (LinearLayout) v.findViewById(R.id.error_email);
        mPasswordError = (LinearLayout) v.findViewById(R.id.error_password);
        mRegisteredError = (LinearLayout) v.findViewById(R.id.register_registered_error);
        mEmailRegisteredError = (LinearLayout) v.findViewById(R.id.error_email_registered);
        mAllFields = new CustomEditText(mContext);

        mRegister.setContentDescription(mRegister.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button_disabled));

    }

    private void initListeners() {
        setClickListeners();
        setFocusChangeListeners();

        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, AppCoreConstants.METHOD_NOT_USED);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doEnablingCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < maxLength) {
                    resetErrorLayout(mFirstName, mFirstNameError);
                }
            }
        });
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

        mPassword.addTextChangedListener(mTextWatcher);
        mEmail.addTextChangedListener(mTextWatcher);

    }

    /**
     * Sets OnClickListener on various View instances.
     */
    private void setClickListeners() {
        mRegister.setOnClickListener(this);

    }

    private void setFocusChangeListeners() {
        setOnFocusChangeListener(mFirstName, mFirstNameError);
        setOnFocusChangeListener(mPhone, mPhoneError);
        setOnFocusChangeListener(mEmail, mEmailError);
        setOnFocusChangeListener(mPassword, mPasswordError);
        setOnFocusChangeListener(mAddress, mAddressError);

    }


    private void setOnFocusChangeListener(final CustomEditText textView, final LinearLayout errorView) {
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !TextUtils.isEmpty(textView.getText().toString())) {
                   resetErrorLayout(textView, errorView);
                    if (mRegisteredError.getVisibility() == View.VISIBLE) {
                        resetErrorLayout(mEmail, mEmailRegisteredError);
                        mRegisteredError.setVisibility(View.GONE);
                    }
                    doFieldValidation(textView);
                    doEnablingCheck();
                }
            }
        });
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


    private boolean validateAllFields() {
        boolean isValidNamePhone = validateInputs(mFirstName, 4) && isValidPhone();
        boolean isValidPasswords = AppUtils.isPasswordValid(AppUtils.getTrimmedText(mPassword));
        boolean isValidAddress = validateInputs(mAddress, 10);
        return isValidNamePhone && isValidEmail() && isValidPasswords;
    }


    private void doFieldValidation(CustomEditText view) {

        if (view == mPassword || view == mAllFields) {
            doPasswordFieldValidation(view);
        }

        if (isViewValid(view, mEmail) && !isValidEmail()) {
            showError(mEmail, mEmailError, getString(R.string.error_registration_invalid_email_ios));
            setFocusRegistrationError(mEmail, view);
        }

        doFieldValidationExtra(view);
    }

    private void doPasswordFieldValidation(CustomEditText view) {
        mPasswordValidMessage = AppUtils.isPasswordValid(mContext, mPassword.getText().toString());
        if (!TextUtils.isEmpty(mPasswordValidMessage)) {
            showError(mPassword, mPasswordError, mPasswordValidMessage, getAccessibilityPasswordMessage());
            mPasswordError.setContentDescription(getAccessibilityPasswordMessage());
            setFocusRegistrationError(mPassword, view);
        }
    }

    private String getAccessibilityPasswordMessage() {
        return !TextUtils.isEmpty(mPasswordValidMessage) ? mPasswordValidMessage.replace("–", AppCoreConstants.SPACE + getString(R.string.acs_to) + AppCoreConstants.SPACE) : "";
    }


    private void doFieldValidationExtra(CustomEditText view) {
        if (isViewValid(view, mPhone) && !isValidPhone()) {
            showError(mPhone, mPhoneError, String.format(getString(R.string.invalid_phone), mPhoneLength));
            setFocusRegistrationError(mPhone, view);
        }

        if ((isViewValid(view, mFirstName) && !validateInputs(mFirstName, 4)) || mFirstName.getText().toString().contains(" ")) {
            showError(mFirstName, mFirstNameError, getString(R.string.error_registration_invalid_first_name));
            setFocusRegistrationError(mFirstName, view);
        }
    }

    protected void showError(final CustomEditText view, LinearLayout errorView, String msg, String accessibilityMessage) {
        if (null != view) {
            View inputLayout = (ViewGroup) view.getParent();
            setErrorBackground(inputLayout);
          //  scrollToPosition(inputLayout, errorView);
        }
        errorView.setVisibility(View.VISIBLE);
        addErrorMessage(msg, accessibilityMessage, errorView);
        if (AppUtils.isAccessibilitySettingsEnabled() && view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.requestFocus();
                }
            });
            AppUtils.sendFocusChangeEvent(view);
        }
    }

    private void setErrorBackground(View inputLayout) {
        View parentLayout = (ViewGroup) inputLayout.getParent();
        parentLayout.setBackgroundResource(R.drawable.input_bg_error);
    }



    private void addErrorMessage(String msg, String accessibilityMessage, View errorView) {
        CustomTextView textView = (CustomTextView) errorView.findViewById(R.id.mcd_error_text);
        textView.setText(msg);
        String message = accessibilityMessage;
        if(TextUtils.isEmpty(accessibilityMessage)) {
            message = msg;
        }
        String errorMsg = getString(R.string.acs_error_string) + AppCoreConstants.SPACE + message;
        AppUtils.announceErrorAccessibility(errorMsg, AppCoreConstants.TIME_OUT_ACCESSIBILITY_4000);
    }


    protected boolean validateInputs(CustomEditText view, int minChars) {
        return view.getText().length() >= minChars && view.getText().toString().trim().length()
                >= minChars;
    }

    private boolean isValidPhone() {
        return AppUtils.validateInputs(mPhone, mPhoneLength) && TextUtils.isDigitsOnly(AppUtils.getTrimmedText(mPhone));
    }

    private void setFocusRegistrationError(CustomEditText viewToFocus, CustomEditText view) {
        if (view == mAllFields && !AppUtils.isAccessibilitySettingsEnabled()) {
            viewToFocus.requestFocus();
        }
    }

    private boolean isViewValid(CustomEditText view, CustomEditText assignedView) {
        return view == assignedView || view == mAllFields;
    }

    private boolean isValidEmail() {
        return AppUtils.isEmailValid(AppUtils.getTrimmedText(mEmail));
    }

    protected void resetErrorLayout(CustomEditText view, LinearLayout errorView) {
        final View inputLayout = (ViewGroup) view.getParent().getParent();
        inputLayout.setBackgroundResource(R.drawable.input_bg);
        errorView.setVisibility(View.GONE);
    }


    protected void showError(final CustomEditText view, LinearLayout errorView, String msg) {
        if (null != view) {
            View inputLayout = (ViewGroup) view.getParent();
            AppUtils.setErrorBackground(inputLayout);
        }
        errorView.setVisibility(View.VISIBLE);
        AppUtils.addErrorMessage(msg, errorView, mContext);
        if (AppUtils.isAccessibilitySettingsEnabled() && view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.requestFocus();
                }
            });
            AppUtils.sendFocusChangeEvent(view);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.register:
                register();
                break;
        }

    }

    public void register() {
        if (validateAllFields() && AppUtils.isNetworkAvailable()) {
            doRegistration();
        } else {
            if (!doEnablingCheck()) {
                doFieldValidation(mAllFields);
            }
        }
    }

    private void doRegistration() {
      //  AppUtils.startActivityIndicator(mActivity, R.string.registering);
        ((LoginActivity) getActivity()).showFragment(new OTPFragment());
    }

}
