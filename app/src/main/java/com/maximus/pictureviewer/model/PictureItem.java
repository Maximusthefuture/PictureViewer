package com.maximus.pictureviewer.model;


import com.google.gson.annotations.SerializedName;

public class PictureItem {
    //Title for pictures
    @SerializedName("title")
    private String title;
    //Pictures id
    @SerializedName("id")
    private String id;
    //Pictures 	small square 75x75
    @SerializedName("url_s")
    private String mUrl;

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
