package com.example.tourmate;

import com.google.firebase.database.Exclude;

public class Upload {

    private String upload_id;
    private String imageCaption;
    private String imageUrl;
    private String user_id;

    public Upload(){

    }

    public Upload(String upload_id, String imageCaption, String imageUrl, String user_id) {
        this.upload_id = upload_id;
        this.imageCaption = imageCaption;
        this.imageUrl = imageUrl;
        this.user_id = user_id;
    }


    public void setUpload_id(String upload_id) {
        this.upload_id = upload_id;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getUpload_id() {
        return upload_id;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUser_id() {
        return user_id;
    }
}
