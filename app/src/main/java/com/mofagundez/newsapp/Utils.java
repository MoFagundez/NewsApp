package com.mofagundez.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.mofagundez.newsapp.MainActivity.LOG_TAG;

/**
 * News List
 * Created by Mauricio on June 6, 2017
 * <p>
 * Udacity Android Basics Nanodegree
 * Project 8: News App
 */
final class Utils {

    /**
     * List of constants to be used parsing {@link JSONObject and {@link JSONArray}}
     */
    private static final String JSON_ITEMS = "response";
    private static final String JSON_RESULTS = "results";
    private static final String JSON_TITLE = "webTitle";
    private static final String JSON_SECTION_NAME = "sectionName";
    private static final String JSON_URL = "webUrl";
    ;
    private static final String JSON_NULL_RESULT = "Unknown";

    /**
     * Create a private constructor because no one should ever create a {@link Utils} object.
     * This class is only meant to hold static methods for networking.
     * <p>
     * Instancing this class will throw and exception.
     */
    private Utils() {
        throw new AssertionError();
    }

    /**
     * Check if the Url passed is valid
     *
     * @param stringUrl: Url passed from {@link NewsLoader} containing the search terms
     */
    static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Static method that makes the HTTP connection type GET to proceed with the search
     *
     * @param url: Url passed as a parameter from the {@link NewsLoader} class,
     *             already validated by the createUrl method of this class
     */
    static String makeHttpRequest(URL url) throws IOException {
        Log.i(LOG_TAG, "makeHTTPRequest");
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            Log.i("HTTP Response Code:", String.valueOf(urlConnection.getResponseCode()));

            // Check if response code is 200 to proceed parsing data
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, String.valueOf(urlConnection.getResponseCode()));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception thrown:" + e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Read the stream of bytes received and transforms into a String to be used later by JSON
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Read the stream collected with the HTTP request and extract the {@link News} objects
     * to an {@link ArrayList} in order to populate the UI later
     */
    static List<News> extractNewsFromJson(String newsJSON) {
        // Check if JSON is null
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        ArrayList<News> newses = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject(JSON_ITEMS);
            JSONArray itemsArray = response.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < itemsArray.length(); i++) {
               //JSONObject response = itemsArray.getJSONObject(i);
                //JSONObject results = response.getJSONObject(JSON_RESULTS);

                // Extract title, section name and url from JSONObject and pass to a variables
                // using the method parseNewsInformation
                String title = parseNewsInformation(itemsArray.getJSONObject(i), JSON_TITLE);
                String sectionName = parseNewsInformation(itemsArray.getJSONObject(i), JSON_SECTION_NAME);
                String url = parseNewsInformation(itemsArray.getJSONObject(i), JSON_URL);

                // Create News object with the variables initialised above
                News news = new News(title, sectionName, url);
                // Add news to list of newses
                newses.add(news);
            }
            return newses;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing newses JSON results from Guardian API", e);

        }
        return null;
    }

    /**
     * Method used to parse simple Strings from {@link JSONObject}
     *
     * @param jsonObject: Declare the {@link JSONObject} being parsed
     * @param jsonTag:    Declare the desired JSON tag according to the API - in this case - The Guardian
     */
    private static String parseNewsInformation(JSONObject jsonObject, String jsonTag) {
        try {
            // Try to return a String value with jsonTag being parsed from jsonObject
            return jsonObject.getString(jsonTag);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing news information at Utils.parseNewsInformation from Guardian API", e);
        }
        // Return a constant String if not successfully parsed from jsonObject
        return JSON_NULL_RESULT;
    }

}
