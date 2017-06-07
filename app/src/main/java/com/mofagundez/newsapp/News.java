package com.mofagundez.newsapp;

/**
 * News List
 * Created by Mauricio on June 6, 2017
 * <p>
 * Udacity Android Basics Nanodegree
 * Project 8: News App
 */
class News {
    private String mTitle;
    private String mSection;
    private String mUrl;

    /**
     * Default constructor to instantiate the class
     */
    News(String title, String section, String url) {
        this.mTitle = title;
        this.mSection = section;
        this.mUrl = url;
    }

    /**
     * List of getters
     */
    String getTitle() {
        return mTitle;
    }

    String getSection() {
        return mSection;
    }

    String getUrl() {
        return mUrl;
    }
}
