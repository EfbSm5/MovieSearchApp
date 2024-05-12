package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.network.parseJsonTools.parseJSONWithJSON;

import android.util.Log;

import com.example.myapplication.db.Movie;
import com.example.myapplication.network.MovieNetworkManager;

import java.util.ArrayList;

public class methodLoadData {
    static ArrayList<Movie> movies = new ArrayList<>();

    public static ArrayList<Movie> methodLoadListView(String _receiveData, int _currentPage) {

        MovieNetworkManager movieNetworkManager = new MovieNetworkManager();
        try {
            movieNetworkManager.sendRequestWithHttpUrl(_receiveData, _currentPage);
            movies.addAll(parseJSONWithJSON(movieNetworkManager.getResponseData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.valueOf(_currentPage));
        return movies;
    }
}
