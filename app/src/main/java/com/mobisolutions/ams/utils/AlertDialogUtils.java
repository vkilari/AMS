package com.mobisolutions.ams.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vkilari on 8/8/17.
 */

public final class AlertDialogUtils {

    private AlertDialogUtils() {
    }

    /**
     * @param builder AlertDialog builder used to create dialog
     * @param view    layout Inflater View
     * @return AlertDialog
     */
    public static AlertDialog setupAlertDialog(AlertDialog.Builder builder, View view) {

        final AlertDialog dialog = builder.create();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog.setView(view, 0, 0, 0, 0);
        }

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        view.setFocusableInTouchMode(true);

        return dialog;
    }

}
