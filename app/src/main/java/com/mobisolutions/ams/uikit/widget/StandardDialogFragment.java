package com.mobisolutions.ams.uikit.widget;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.utils.AlertDialogUtils;

/**
 * Created by vkilari on 8/8/17.
 */

public class StandardDialogFragment extends DialogFragment implements DialogInteractionListener {

    public static final String TAG = StandardDialogFragment.class.getSimpleName();

    public static final int LAYOUT_SINGLE_BUTTON = R.layout.dialog_single_button_layout;

    public static final int LAYOUT_TWO_BUTTON_SIDE_BY_SIDE = R.layout.dialog_two_button_sidebyside_layout;

    public static final int LAYOUT_EDITTEXT_SINGLE_BUTTON = R.layout.dialog_single_button_edittext;


    private String mTitle;

    private String mPositiveButton;

    private String mNegativeButton;

    private String mNeutralButton;

    private CharSequence mContent;

    protected View positiveButtonView;

    protected View negativeButtonView;

    protected View stackedButtonView;

    protected View neutralButtonView;

    private String mTitleContentDescription;

    private String mMessageContentDescription;

    private DialogInteractionListener mCallback;

    @LayoutRes
    protected int mLayout;

    private boolean showPending;

    private View.OnClickListener positiveButtonOnClickListener;

    private View.OnClickListener negativeButtonOnClickListener;

    private View.OnClickListener neutralButtonOnClickListener;

    private DialogInterface.OnDismissListener onDismissListener = null;

    private RestartAppStandardDialogListener restartAppStandardDialogListener;

    public static StandardDialogFragment newInstance() {
        return new StandardDialogFragment();
    }

    public void setDialog(String title, String content, String positiveButton, String negativeButton, @LayoutRes int layout) {
        mTitle = title;
        mContent = content;
        mPositiveButton = positiveButton;
        mNegativeButton = negativeButton;
        mLayout = layout;
    }

    public void setDialog(String title, String content, String positiveButton, String negativeButton, @LayoutRes int layout,
                          View.OnClickListener positiveButtonOnClickListener,
                          View.OnClickListener negativeButtonOnClickListener) {
        mTitle = title;
        mContent = content;
        mPositiveButton = positiveButton;
        mNegativeButton = negativeButton;
        mLayout = layout;
        this.positiveButtonOnClickListener = positiveButtonOnClickListener;
        this.negativeButtonOnClickListener = negativeButtonOnClickListener;
    }


    public void setTitleAndMessageContentDescription(String titleContentDescription, String messageContentDescription) {
        mTitleContentDescription = titleContentDescription;
        mMessageContentDescription = messageContentDescription;
    }


    public void setDialog(String title, String content, String positiveButton, String negativeButton,
                          String neutralButton, @LayoutRes int layout,
                          View.OnClickListener positiveButtonOnClickListener,
                          View.OnClickListener negativeButtonOnClickListener,
                          View.OnClickListener neutralButtonOnClickListener) {
        mTitle = title;
        mContent = content;
        mPositiveButton = positiveButton;
        mNegativeButton = negativeButton;
        mNeutralButton = neutralButton;
        mLayout = layout;
        this.positiveButtonOnClickListener = positiveButtonOnClickListener;
        this.negativeButtonOnClickListener = negativeButtonOnClickListener;
        this.neutralButtonOnClickListener = neutralButtonOnClickListener;
    }

    public void setSingleButtonDialog(String title, CharSequence content, String positiveButton,
                                      View.OnClickListener positiveButtonOnClickListener) {
        mTitle = title;
        mContent = content;
        mPositiveButton = positiveButton;
        mNegativeButton = null;
        mLayout = LAYOUT_SINGLE_BUTTON;
        this.positiveButtonOnClickListener = positiveButtonOnClickListener;
    }

    public void setSingleButtonDialog(String title, String content, String positiveButton) {
        mTitle = title;
        mContent = content;
        mPositiveButton = positiveButton;
        mNegativeButton = null;
        mLayout = LAYOUT_SINGLE_BUTTON;
    }


    public void setEditTextDialog(String title, CharSequence content, String positiveButton,
                                      View.OnClickListener positiveButtonOnClickListener) {
        mTitle = title;
        mContent = content;
        mPositiveButton = positiveButton;
        mNegativeButton = null;
        mLayout = LAYOUT_EDITTEXT_SINGLE_BUTTON;
        this.positiveButtonOnClickListener = positiveButtonOnClickListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        showPending = false;

        try {
            mCallback = (DialogInteractionListener) activity;
        } catch (ClassCastException e) {
            Log.d(TAG, "Launching activity " + activity.getClass().getSimpleName() + " does not implement listener "
                    + "DialogInteractionListener, assigning to this instance.", e);
            mCallback = this;
        }
    }

    protected void onViewInflated(View view) {
        return;
    }

    @NonNull
    @Override
    @SuppressWarnings("squid:MethodCyclomaticComplexity")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);

        if (mLayout == 0) {
            if (restartAppStandardDialogListener != null) {
                restartAppStandardDialogListener.setupRestartAppStandardDialogListener(this);
            } else {
                setupDefaultErrorModal();
            }
        }

        final View view = getActivity().getLayoutInflater().inflate(mLayout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            builder.setCustomTitle(view);
        }

        TextView tv = (TextView) view.findViewById(R.id.dialog_title);
        tv.setText(mTitle);
        tv.setContentDescription(!TextUtils.isEmpty(mTitleContentDescription) ? mTitleContentDescription : mTitle);
        tv = (TextView) view.findViewById(R.id.dialog_message);
        if (tv != null && mContent != null) {
            tv.setText(mContent);
            tv.setContentDescription(!TextUtils.isEmpty(mMessageContentDescription) ? mMessageContentDescription : mContent);
        }

        Button positiveButton = (Button) view.findViewById(R.id.positive_button);
        positiveButtonView = positiveButton;
        positiveButton.setText(mPositiveButton);

        //fix for devices reading "ok" as "ock"
        readPositiveButtonText(positiveButton);

        positiveButton.setOnClickListener(
                positiveButtonOnClickListener != null ? positiveButtonOnClickListener
                        : new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                        mCallback.onPositiveButtonClick();
                    }
                });

        if (mNegativeButton != null) {
            Button negativeButton = (Button) view.findViewById(R.id.negative_button);
            negativeButtonView = negativeButton;
            negativeButton.setText(mNegativeButton);
            negativeButton.setContentDescription(TextUtils.isEmpty(mNegativeButton) ? null
                    : mNegativeButton.toLowerCase());

            negativeButton.setOnClickListener(
                    negativeButtonOnClickListener != null ? negativeButtonOnClickListener
                            : new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dismiss();
                            mCallback.onNegativeButtonClick();
                        }
                    });
        }

//        if (mNeutralButton != null) {
//            Button neutralButton = (Button) view.findViewById(R.id.neutral_button);
//            neutralButtonView = neutralButton;
//            neutralButton.setText(mNeutralButton);
//            neutralButton.setContentDescription(TextUtils.isEmpty(mNeutralButton) ? null : mNeutralButton.toLowerCase());
//
//            neutralButton.setOnClickListener(
//                    neutralButtonOnClickListener != null ? neutralButtonOnClickListener
//                            : new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dismiss();
//                            mCallback.onNeutralButtonClick();
//
//                        }
//                    });
//        }

        onViewInflated(view);

        final AlertDialog dialog = AlertDialogUtils.setupAlertDialog(builder, view);
        view.setOnKeyListener(new SetOnKeyListener(dialog));
        return dialog;
    }

    private void setupDefaultErrorModal() {
        mLayout = R.layout.dialog_single_button_layout;
        mTitle = getString(R.string.default_error_title);
        mContent = getString(R.string.fall_through_message);
        mPositiveButton = getString(R.string.ok_button_text);
        positiveButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    private void readPositiveButtonText(Button positiveButton) {
        if (!TextUtils.isEmpty(mPositiveButton)) {
            positiveButton.setContentDescription(
                    mPositiveButton.equals(getString(R.string.ok_button_text)) ? getString(R.string.okay) : mPositiveButton.toLowerCase());
        } else {
            positiveButton.setContentDescription(null);
        }

    }

    @Override
    public void onPositiveButtonClick() {

    }

    @Override
    public void onNegativeButtonClick() {

    }

    @Override
    public void onNeutralButtonClick() {

    }

    @Override
    public void onDeviceBackButtonPressed() {

        if (isCancelable()) {
            dismiss();
        }
    }


    private class SetOnKeyListener implements View.OnKeyListener {

        private boolean handleUp;

        AlertDialog dialog;

        public SetOnKeyListener(AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_DOWN:
                        handleUp = !event.isLongPress();
                        break;
                    case KeyEvent.ACTION_UP:
                        if (handleUp) {
                            dialog.onBackPressed();
                            mCallback.onDeviceBackButtonPressed();
                            return true;
                        }
                        break;
                    default:
                }
            }

            return false;

        }
    }



    public interface RestartAppStandardDialogListener {

        /**
         * performs action needed on RestartApp
         *
         * @param dialogFragment Dialog Fragment
         */
        void setupRestartAppStandardDialogListener(StandardDialogFragment dialogFragment);
    }

}
