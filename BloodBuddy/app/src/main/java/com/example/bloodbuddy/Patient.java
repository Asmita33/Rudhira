package com.example.bloodbuddy;

public class Patient {


    private String name;
    private String email;
    private String mobile;
    private String age;
    private String bloodGrp;
    private String amount;
    private String location;
    private String condition;
    private String pdfUrl;
    private String isValid="false";
    private String received="false";
    private String donated="false";
    private String donateTo;
    private String seekerBloodGrp;
    private String seekerContact;

    public String getSeekerBloodGrp() {
        return seekerBloodGrp;
    }

    public void setSeekerBloodGrp(String seekerBloodGrp) {
        this.seekerBloodGrp = seekerBloodGrp;
    }


    public String getSeekerContact() {
        return seekerContact;
    }

    public void setSeekerContact(String seekerContact) {
        this.seekerContact = seekerContact;
    }


    public String getDonated() {
        return donated;
    }

    public void setDonated(String donated) {
        this.donated = donated;
    }

    public String getDonateTo() {
        return donateTo;
    }

    public void setDonateTo(String donateTo) {
        this.donateTo = donateTo;
    }



    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public Patient() {
    }

    public Patient(String name, String mobile, String condition) {
        this.name = name;
        this.mobile = mobile;
        this.condition = condition;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
