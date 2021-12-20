package com.example.bloodbuddy;

import android.widget.ImageView;

public class Users {

//    private int profilePic;*/
    private String email;
    private String name;
    private String mobile;
    private String uid;
    private String imgUri;
    private String dob;
    private String bloodGrp;
    private String address;

    public Users()
    {

    }

    public Users(String pic, String name)
    {
        this.imgUri = pic;
        this.name = name;
    }

    //Constructor used for storing profile images in realtime firebase database
    public Users(String uid,String phone,String imgUri)
    {
        this.uid=uid;
        this.mobile=phone;
        this.imgUri=imgUri;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
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
