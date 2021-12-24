package com.example.bloodbuddy.modelClasses;

public class Feed
{
    private String text;
    private int image;
    private String link;


    public Feed(String text, int image, String link) {
        this.text = text;
        this.image = image;
        this.link = link;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
