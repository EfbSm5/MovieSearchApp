package com.example.myapplication;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.db.Movie;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ShowActivityController {
    private static final String TAG = "ShowActivityController";
    private int currentPage = 1;
    private final Set<Movie> tempMovieHistory = new LinkedHashSet<>();
    private ArrayList<Movie> tempMovieList = new ArrayList<>();
    final MutableLiveData<ArrayList<Movie>> mLiveData = new MutableLiveData<>();
    private final ShowActivityControllerInterface showActivityControllerInterface;
    private final String receiveData;

    ShowActivityController(String receiveData, ShowActivityControllerInterface showActivityControllerInterface) {
        this.showActivityControllerInterface = showActivityControllerInterface;
        this.receiveData = receiveData;
        firstLoadData();
    }

    void addHistory(Movie movie) {
        tempMovieHistory.add(movie);
    }

    void onScrolled() {
        currentPage++;
        showActivityControllerInterface.setProgressbarVisibility(true);
        new Thread(this::getMovie).start();
        Log.d(TAG, "onScrolled: ");
    }

    void onClickHistoryBtn() {
        Movie[] movieHistory = tempMovieHistory.toArray(new Movie[0]);
        Intent intent = new Intent(AppUtil.getInstance().getContext(), HistoryActivity.class);
        intent.putExtra("movie list", movieHistory);
        showActivityControllerInterface.startActivity(intent);
    }

    private void firstLoadData() {
        new Thread(() -> {
            getMovie();
            if (tempMovieList.isEmpty()) {
                showActivityControllerInterface.noData();
            }
        }).start();
    }

    void getMovie() {
        showActivityControllerInterface.setProgressbarVisibility(true);
        tempMovieList = new ArrayList<>(LoadDataTools.loadListView(receiveData, currentPage));
        mLiveData.postValue(tempMovieList);
        showActivityControllerInterface.setProgressbarVisibility(false);
    }

    interface ShowActivityControllerInterface {
        void noData();

        void startActivity(Intent intent);

        void setProgressbarVisibility(Boolean progressbarVisibility);
    }
}
