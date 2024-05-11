package com.example.myapplication;

import static com.example.myapplication.network.parseJsonTools.parseJSONWithJSON;

import com.example.myapplication.db.Movie;
import com.example.myapplication.network.MovieNetworkManager;

import java.util.ArrayList;

public class methodLoadData {
    static ArrayList<Movie> movies = new ArrayList<>();

    public static ArrayList<Movie> methodLoadListView(String _receiveData, int _currentPage) {
        MovieNetworkManager movieNetworkManager = new MovieNetworkManager();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieNetworkManager.sendRequestWithHttpUrl(_receiveData, _currentPage);
                    movies.addAll(parseJSONWithJSON(movieNetworkManager.getResponseData()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return movies;
    }
}
