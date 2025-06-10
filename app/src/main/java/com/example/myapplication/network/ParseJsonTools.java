package com.example.myapplication.network;

import com.example.myapplication.db.Movie;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.io.Serializable;

public class ParseJsonTools implements Serializable {

    public static ArrayList<Movie> parseJSONWithJSON(String jsonData) {
        ArrayList<Movie> tempmovies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray searchArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < searchArray.length(); i++) {
                JSONObject movieObject = searchArray.getJSONObject(i);
                Movie tempMovie = new Movie(movieObject.getString("Title"), movieObject.getString("Year"), movieObject.getString("Poster"));
                tempmovies.add(tempMovie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempmovies;
    }
}
