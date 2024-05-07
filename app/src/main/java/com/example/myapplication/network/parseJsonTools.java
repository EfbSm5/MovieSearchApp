package com.example.myapplication.network;

import com.example.myapplication.db.Movie;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.Serializable;

public class parseJsonTools implements Serializable {
    public ArrayList<Movie> movies = new ArrayList<>();
    public int dataLength;
    public Movie[] movieArray;
    public boolean parseJSONWithJSON(String jsonData) throws JSONException {
        ArrayList<Movie> tempmovies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray searchArray = jsonObject.getJSONArray("Search");
            dataLength = searchArray.length();
            for (int i = 0; i < searchArray.length(); i++) {
                JSONObject movieObject = searchArray.getJSONObject(i);
                Movie tempMovie = new Movie(movieObject.getString("Title"), movieObject.getString("Year"), movieObject.getString("Poster"));
                tempmovies.add(tempMovie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        movies.addAll(tempmovies);
        movieArray=movies.toArray(new Movie[0]);
        return !tempmovies.isEmpty();
    }
}
