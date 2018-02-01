package com.mobisolutions.ams.common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vkilari on 7/17/17.
 */

public class InputFields {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("required")
    @Expose
    private boolean required;

    @SerializedName("show")
    @Expose
    private boolean show;

    @SerializedName("maxLength")
    @Expose
    private int maxLength;

    @SerializedName("charactersAllowed")
    @Expose
    private boolean charactersAllowed;

    @SerializedName("validationRule")
    @Expose
    private String validationRule;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The required
     */
    public boolean getRequired() {
        return required;
    }

    /**
     * @param required The required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return The show
     */
    public boolean getShow() {
        return show;
    }

    /**
     * @param show The show
     */
    public void setShow(boolean show) {
        this.show = show;
    }

    /**
     * @return The maxLength
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * @param maxLength The maxLength
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return The required
     */
    public boolean getAllowCharacters() {
        return charactersAllowed;
    }

    /**
     * @return The required
     */
    public String getValidationRule() {
        return validationRule;
    }
}
