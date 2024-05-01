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
    public Movie[] movie;
    public ArrayList<Movie> movies=new ArrayList<>();
    public List<Movie> movieList = new ArrayList<>();
    public ArrayList<String> Title = new ArrayList<>();
    public int dataLength;

    public void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        movieList = gson.fromJson(jsonData, new TypeToken<List<Movie>>() {
        }.getType());
        int i = 0;
    }

    public boolean parseJSONWithJSON(String jsonData) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray searchArray = jsonObject.getJSONArray("Search");
            dataLength=searchArray.length();
            for (int i = 0; i < searchArray.length(); i++) {
                JSONObject movieObject = searchArray.getJSONObject(i);
                Movie tempMovie=new  Movie (movieObject.getString("Title"),movieObject.getString("Year"),movieObject.getString("Poster"));
                movies.add(tempMovie);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }if(!movies.isEmpty()){
        movie= movies.toArray(new Movie[0]);return true;}
        else return false;
    }
}
