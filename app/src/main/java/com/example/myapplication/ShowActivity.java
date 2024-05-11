package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Movie;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


public class ShowActivity extends AppCompatActivity {

    private int currentPage = 1;
    private ProgressBar progressBar;

    MutableLiveData<ArrayList<Movie>> mLiveData = new MutableLiveData<>();
    Set<Movie> tempMovieHistory = new LinkedHashSet<>();
    recyclerViewAdapter adapter;
    ArrayList<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String receiveData = getIntent().getStringExtra("movie name");
        mLiveData.setValue(movieList);
        Button button = (Button) findViewById(R.id.button_);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        movieList.addAll(methodLoadData.methodLoadListView(receiveData, currentPage));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie[] movieHistory = tempMovieHistory.toArray(new Movie[0]);
                Intent intent = new Intent(ShowActivity.this, HistoryActivity.class);
                intent.putExtra("movie list", movieHistory);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView _recyclerView, int dx, int dy) {
                super.onScrolled(_recyclerView, dx, dy);
                if (!_recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    progressBar.setVisibility(View.VISIBLE);
                    movieList.addAll(methodLoadData.methodLoadListView(receiveData, currentPage));
                }
            }
        });
        mLiveData.observe(this, new Observer<ArrayList<Movie>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Movie> strings) {
                if (!movieList.isEmpty()) {
                    recyclerView.setAdapter(new recyclerViewAdapter(movieList));
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
        ArrayList<Movie> movieList = new ArrayList<>();
        Movie[] movieData;

        public recyclerViewAdapter(ArrayList<Movie> _movieData) {
            movieList.addAll(_movieData);
            movieData = movieList.toArray(new Movie[0]);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                textView = itemView.findViewById(R.id.textViewTitle);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Intent intent = new Intent(ShowActivity.this, DetailsActivity.class);
                    intent.putExtra("movie need", movieData[position]);
                    tempMovieHistory.add(movieData[position]);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(movieData[position].getName());
        }

        @Override
        public int getItemCount() {
            return movieData.length;
        }
    }

}

