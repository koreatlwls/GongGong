package com.example.gonggong;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

public class ListViewItem {
    private Drawable icon;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private CheckBox checkBox=null;

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    private int code;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
