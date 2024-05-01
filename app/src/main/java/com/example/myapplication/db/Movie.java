package com.example.myapplication.db;
import java.io.Serializable;

public class Movie implements Serializable{
    public String title;
    public String year;
    public String poster;

    public Movie(String title, String year, String poster) {
        this.title=title;
        this.year=year;
        this.poster=poster;
    }

    public String getName() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getYear() {
        return year;
    }

    public void setName(String name) {
        this.title = name;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
