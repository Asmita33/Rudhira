package com.example.bloodbuddy;

import android.widget.ImageView;

public class Users {

    private int profilePic;
    private String email;
    private String name;
    private String mobile;
    private String uid;

    public Users()
    {

    }

    public Users(int pic, String name)
    {
        this.profilePic = pic;
        this.name = name;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
