package com.mobisolutions.ams.services;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkilari on 11/16/17.
 */

public class ServiceItemBean {

    private String serviceId;
    private String serviceImage;
    private String serviceName;
    private String serviceDescription;
    private String serviceRegion;
    private List<ServiceProviderBean> providerList;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceRegion() {
        return serviceRegion;
    }

    public void setServiceRegion(String serviceRegion) {
        this.serviceRegion = serviceRegion;
    }

    public List<ServiceProviderBean> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<ServiceProviderBean> providerList) {
        this.providerList = providerList;
    }
}
