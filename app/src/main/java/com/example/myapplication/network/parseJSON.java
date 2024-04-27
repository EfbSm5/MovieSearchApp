package com.example.myapplication.network;

import com.example.myapplication.db.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class parseJSON {
    public static Movie[] movie;
    public List<Movie> movieList = new ArrayList<>();

    public void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        movieList = gson.fromJson(jsonData, new TypeToken<List<Movie>>() {
        }.getType());
        int i = 0;
    }
}
