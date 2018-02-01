package com.mobisolutions.ams.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobisolutions.ams.R;

/**
 * Created by vkilari on 8/10/17.
 */

public class ForgotFragment extends Fragment implements View.OnClickListener {


    private LoginActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (LoginActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // initViews(view);
        //  initListeners();
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
