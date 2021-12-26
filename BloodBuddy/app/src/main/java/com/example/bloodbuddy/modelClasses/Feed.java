package com.example.bloodbuddy.modelClasses;

import android.net.Uri;

public class Feed
{
    private String text;
    private String image;
    private String link;

    public Feed(){}


    public Feed(String text, String image, String link) {
        this.text = text;
        this.image = image;
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
