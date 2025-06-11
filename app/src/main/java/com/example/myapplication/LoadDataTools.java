package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.network.ParseJsonTools.parseJSONWithJSON;

import android.util.Log;

import com.example.myapplication.db.Movie;
import com.example.myapplication.network.MovieNetworkManager;

import java.util.ArrayList;

public class LoadDataTools {
    public static ArrayList<Movie> loadListView(String _receiveData, int _currentPage) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            movies.addAll(parseJSONWithJSON(MovieNetworkManager.sendRequestWithHttpUrl(_receiveData, _currentPage)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, String.valueOf(_currentPage));
        return movies;
    }
}
