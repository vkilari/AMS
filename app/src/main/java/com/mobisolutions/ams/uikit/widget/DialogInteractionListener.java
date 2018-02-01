package com.mobisolutions.ams.uikit.widget;

/**
 * Created by vkilari on 8/8/17.
 */

public interface DialogInteractionListener {

    /**
     * performs action on dialog positive button click.
     */
    void onPositiveButtonClick();

    /**
     * performs action on dialog negative button click.
     */
    void onNegativeButtonClick();

    /**
     * performs action on dialog neutral button click.
     */
    void onNeutralButtonClick();

    /**
     * performs action on device back button click.
     */
    void onDeviceBackButtonPressed();

}
