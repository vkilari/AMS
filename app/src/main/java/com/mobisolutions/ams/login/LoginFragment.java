package com.mobisolutions.ams.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.common.AppCoreConstants;
import com.mobisolutions.ams.config.ApplicationContext;
import com.mobisolutions.ams.home.HomeActivity;
import com.mobisolutions.ams.uikit.widget.CustomEditText;
import com.mobisolutions.ams.uikit.widget.CustomTextInputLayout;
import com.mobisolutions.ams.uikit.widget.CustomTextView;
import com.mobisolutions.ams.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkilari on 8/5/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = LoginFragment.class.getSimpleName();

    protected static final String METHOD_NOT_USED = "Un-used Method";

    private final List<CustomEditText> mInputFields = new ArrayList<>();
    private CustomEditText mPassword;
    private CustomTextView mSave;
    private CustomEditText mEmailPhone;
    private CustomTextView mForgotPassword;
    private LinearLayout mEmailPhoneError;
    private CustomTextView mRegister;
    private CustomTextInputLayout mEmailPhoneInputLayout;
    private boolean mIsMobileEmailLoginAllowed;
    private int mPhoneLength;
    private LoginActivity mActivity;
    private Context mContext;

    public LoginFragment() {
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

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

        ApplicationContext.init(mContext);

        initViews(view);

        addListeners();
    }


    private void initViews(View view) {
        mEmailPhoneError = (LinearLayout) view.findViewById(R.id.error_email_phone);
        mEmailPhone = (CustomEditText) view.findViewById(R.id.email_phone);
        mEmailPhoneInputLayout = (CustomTextInputLayout) view.findViewById(R.id.email_phone_hint);
        mPassword = (CustomEditText) view.findViewById(R.id.password);
        mForgotPassword = (CustomTextView) view.findViewById(R.id.forgot_password_link);
        mSave = (CustomTextView) view.findViewById(R.id.save);
        mRegister = (CustomTextView) view.findViewById(R.id.register_link);
        mSave.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        mRegister.append(" >");
    }

    private void addListeners() {
        mInputFields.add(mEmailPhone);
        mInputFields.add(mPassword);
        setOnFocusChangeListener(mEmailPhone, mEmailPhoneError);
        mEmailPhone.addTextChangedListener(inputWatcher);
        mPassword.addTextChangedListener(inputWatcher);
    }

    private void setOnFocusChangeListener(final CustomEditText textView, final LinearLayout errorView) {
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !TextUtils.isEmpty(textView.getText().toString())) {
                    resetErrorLayout(textView, errorView);
                    if (mIsMobileEmailLoginAllowed) {
                        showErrorIfMobileLoginEnabled();
                    } else if (!isValidEmail()) {
                        showError(mEmailPhone, mEmailPhoneError, getString(R.string.error_registration_invalid_email));
                    }
                    enableDisableSave();
                }
            }
        });
    }

    private void showErrorIfMobileLoginEnabled() {
        if (TextUtils.isDigitsOnly(AppUtils.getTrimmedText(mEmailPhone))) {
            if (!AppUtils.isValidPhone(AppUtils.getTrimmedText(mEmailPhone), mPhoneLength)) {
                showError(mEmailPhone, mEmailPhoneError, getString(R.string.error_valid_mobile));
            }
        } else if (!isValidEmail()) {
            showError(mEmailPhone, mEmailPhoneError, getString(R.string.error_registration_invalid_email));
        }
    }

    private boolean isValidEmail() {
        return AppUtils.isEmailValid(AppUtils.getTrimmedText(mEmailPhone));
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


    protected void resetErrorLayout(CustomEditText view, LinearLayout errorView) {
        //Fix for Android Support Library 25.0.0, as it adds extra parent to EditText
        // in the form of Frame Layout
        final View inputLayout = (ViewGroup) view.getParent().getParent();
        inputLayout.setBackgroundResource(R.drawable.input_bg);
        errorView.setVisibility(View.GONE);
    }

    private final TextWatcher inputWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i(TAG, METHOD_NOT_USED);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enableDisableSave();
        }

        public void afterTextChanged(Editable s) {
            Log.i(TAG, METHOD_NOT_USED);
        }
    };


    private void enableDisableSave() {
        boolean isValidInput = !AppUtils.isEmpty(mEmailPhone.getText()) &&
                !AppUtils.isEmpty(mPassword.getText());
        if (isValidInput && AppUtils.isEmailValid(mEmailPhone.getText().toString())) {
            mSave.setContentDescription(mSave.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button));
            AppUtils.enableButton(mSave);
        } else {
            boolean isValidPhoneAndPassword = mIsMobileEmailLoginAllowed && isValidInput
                    && AppUtils.isValidPhone(AppUtils.getTrimmedText(mEmailPhone), mPhoneLength);
            if (isValidPhoneAndPassword) {
                mSave.setContentDescription(mSave.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button));
                AppUtils.enableButton(mSave);
            } else {
                mSave.setContentDescription(mSave.getText() + AppCoreConstants.SPACE + getString(R.string.acs_button_disabled));
                AppUtils.disableButton(mSave);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                hideKeyboardIfShown();
                login(AppUtils.getTrimmedText(mEmailPhone), AppUtils.getTrimmedText(mPassword));

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

                break;
            case R.id.register_link:

                ((LoginActivity) getActivity()).showFragment(new RegistrationFragment());

                break;

            case R.id.forgot_password_link:
                ((LoginActivity) getActivity()).showFragment(new ForgotFragment());
                break;
        }
    }


    private void hideKeyboardIfShown() {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        if(manager.isAcceptingText()) {
            manager.hideSoftInputFromWindow(mSave.getWindowToken(), 0);
        }
    }


    protected void login(@NonNull String username, @NonNull String password) {
        if (AppUtils.isNetworkAvailable()) {

            // Need to make a service call.

            // doDefaultLogin(parameters);
        } else {
            showErrorNotification(getString(R.string.error_no_network_connectivity), false, true);
        }
    }

    public void showErrorNotification(String message, final boolean rightArrowVisible,
                                      boolean errorIconVisible) {


    }


}