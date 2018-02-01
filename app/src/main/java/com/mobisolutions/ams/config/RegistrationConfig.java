package com.mobisolutions.ams.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobisolutions.ams.common.model.InputFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkilari on 7/17/17.
 */

public class RegistrationConfig {

    @SerializedName("fields")
    @Expose
    private List<InputFields> fields = new ArrayList<>();

    /**
     * @return The fields
     */
    public List<InputFields> getFields() {
        return fields;
    }

    /**
     * @param fields The fields
     */
    public void setFields(List<InputFields> fields) {
        this.fields = fields;
    }
}
