package com.example.myapplication.db;

import androidx.databinding.Bindable;

import java.io.Serializable;

public class Movie implements Serializable {
    private final String title;
    private final String year;
    private final String poster;

    public Movie() {
        this.title = "";
        this.year = "";
        this.poster = "";
    }

    public Movie(String title, String year, String poster) {
        this.title = title;
        this.year = year;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getYear() {
        return year;
    }

}
