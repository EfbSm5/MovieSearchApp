package com.example.myapplication.network;

import com.example.myapplication.db.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class parseJSON implements Serializable{
    public Movie[] movie = new Movie[10];
    public List<Movie> movieList = new ArrayList<>();
    public ArrayList<String> Title = new ArrayList<>();
    public int dataLength;

    public void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        movieList = gson.fromJson(jsonData, new TypeToken<List<Movie>>() {
        }.getType());
        int i = 0;
    }

    public void parseJSONWithJSON(String jsonData) throws JSONException {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray searchArray = jsonObject.getJSONArray("Search");
            dataLength=searchArray.length();
            for (int i = 0; i < searchArray.length(); i++) {
                movie[i]=new Movie();
                JSONObject movieObject = searchArray.getJSONObject(i);
                movie[i].setName(movieObject.getString("Title"));
                movie[i].setYear(movieObject.getString("Year"));
                movie[i].setPoster(movieObject.getString("Poster"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
