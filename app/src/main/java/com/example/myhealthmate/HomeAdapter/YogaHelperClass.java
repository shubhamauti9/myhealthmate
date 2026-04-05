package com.example.myhealthmate.HomeAdapter;

public class YogaHelperClass {

    int image;
    String title, des;

    public YogaHelperClass(int image, String title, String des) {
        this.image = image;
        this.title = title;
        this.des = des;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }
}
