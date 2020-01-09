package com.example.weatherapp.data.entity;


public class OnBoardEntity {
    private int imageView;
    private String title;


    public OnBoardEntity(int imageView, String title) {
        this.imageView = imageView;
        this.title = title;
    }

    public int getImageView() {
        return imageView;
    }

    public String getTitle() {
        return title;
    }
}
