package com.mofagundez.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * News List
 * Created by Mauricio on June 6, 2017
 * <p>
 * Udacity Android Basics Nanodegree
 * Project 8: News App
 */
class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String GUARDIAN_API_QUERY = "http://content.guardianapis.com/search?q=economy&api-key=test";

    NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        List<News> newses = new ArrayList<>();
        // Create URL object
        URL url = Utils.createUrl(GUARDIAN_API_QUERY);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = Utils.makeHttpRequest(url);
            newses = Utils.extractNewsFromJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newses;
    }
}
