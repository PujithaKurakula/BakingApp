package com.example.acer.bakingapp;

public class StepsModel
{


    String shortDescription,description,video,thumbnail;

    public StepsModel(String shortDescription, String description, String video, String thumbnail) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.video = video;
        this.thumbnail = thumbnail;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
