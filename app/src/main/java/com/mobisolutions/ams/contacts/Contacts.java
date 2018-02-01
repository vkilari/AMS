package com.mobisolutions.ams.contacts;

import android.graphics.Bitmap;

/**
 * Created by vkilari on 11/4/17.
 */

public class Contacts {

    private String name;
    private String mobile;
    private String flatNo;
    private String image;
    private String email;
    private String isOwener;
    private String description;
    private String address;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsOwener() {
        return isOwener;
    }

    public void setIsOwener(String isOwener) {
        this.isOwener = isOwener;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
