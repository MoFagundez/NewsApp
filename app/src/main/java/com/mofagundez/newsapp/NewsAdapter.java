package com.mofagundez.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * News List
 * Created by Mauricio on June 6, 2017
 * <p>
 * Udacity Android Basics Nanodegree
 * Project 8: News App
 */
class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if View is already created - if not, create a View
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_view, parent, false);
        }
        // Create a variable to store information of the current news populating convertView
        final News currentNews = getItem(position);

        // Create variable titleTextView, find a layout reference and pass the value from currentNews
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNews.getTitle());

        // Create variable sectionTextView, find a layout reference and pass the value from currentNews
        TextView sectionTextView = (TextView) convertView.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentNews.getSection());

        // Set click listener to the convertView throwing an implicit intent to open the news website
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = currentNews.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });

        return convertView;
    }

}
