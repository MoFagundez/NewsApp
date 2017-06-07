package com.mofagundez.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = Utils.class.getSimpleName();
    private ProgressBar progressBar;
    private NewsAdapter mAdapter;
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a ProgressBar object and finds a layout reference
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Create a TextView objec and finds a layout reference
        emptyStateTextView = (TextView) findViewById(R.id.empty_text_view);

        performQuery();

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        // Perform search with the custom class NewsLoader in a worker thread
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Update the UI passing an ArrayList of news and 'true' to the boolean value isConnected
        updateUi(data, true);
        // Hide progress bar and text after UI is updated
        if (data != null) {
            emptyStateTextView.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }

    /**
     * Update the UI with news retrieved from a successful search or no network connection when connection is not available.
     *
     * @param newses:       This parameter is @{@link Nullable} since no {@link ArrayList} will be passed as a parameter
     *                     when connection is not available.
     * @param isConnected: Passed from when the connection is tested, it's either true or false and will update the UI
     *                     accordingly
     */
    public void updateUi(@Nullable List<News> newses, boolean isConnected) {
        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list_view);
        // Check boolean parameter if connection is available
        if (isConnected) {
            // Check if ArrayList of news is not null
            if (newses != null) {
                // Initialise the NewsAdapter
                mAdapter = new NewsAdapter(this, newses);
                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface
                newsListView.setAdapter(mAdapter);
            } else {
                // Update the UI with network not found
                emptyStateTextView.setText(R.string.empty_view_no_connection);
                emptyStateTextView.setVisibility(View.VISIBLE);
                newsListView.setEmptyView(emptyStateTextView);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Perform a query with LoaderManager
     */
    private void performQuery() {
        clearAdapter();
        // Make progressBar visible and hide TextView to inform user that the query is being processed
        progressBar.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);
        // Check whether or not network connectivity is available and update UI accordingly
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                getLoaderManager().initLoader(0, null, MainActivity.this);
        } else {
            // Update UI passing a boolean parameter isConnected as false
            updateUi(null, false);
        }
    }

    /**
     * Check if mAdapter is null and clear so it won't be in the UI when performing a new query
     */
    private void clearAdapter() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

}
