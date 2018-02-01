package com.mobisolutions.ams.contacts;

import android.view.View;

/**
 * Created by vkilari on 11/6/17.
 */

public interface OnItemClickListener {
    void onItemClick(Contacts item, View view, boolean isEditClicked, int position);

}
