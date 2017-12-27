package com.example.user.project1;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable mIcon;
    private String mName;
    private String mPhoneNum;

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }
}
