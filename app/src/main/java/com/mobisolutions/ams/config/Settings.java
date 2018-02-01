package com.mobisolutions.ams.config;

/**
 * Created by vkilari on 12/24/17.
 */

public class Settings {

    private String settingID;
    private String serviceName;
    private String serviceStatus;
    private String serviceUpdatedDate;

    public String getSettingID() {
        return settingID;
    }

    public void setSettingID(String settingID) {
        this.settingID = settingID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceUpdatedDate() {
        return serviceUpdatedDate;
    }

    public void setServiceUpdatedDate(String serviceUpdatedDate) {
        this.serviceUpdatedDate = serviceUpdatedDate;
    }
}
