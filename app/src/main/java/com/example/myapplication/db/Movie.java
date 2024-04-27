package com.example.myapplication.db;

public class Movie {
    public String title;
    public String year;
    public String poster;

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
