package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG = "ShowActivity";
    private int currentPage = 1;
    private ProgressBar progressBar;
    private final Set<Movie> tempMovieHistory = new LinkedHashSet<>();
    private ArrayList<Movie> tempMovieList = new ArrayList<>();
    private final recyclerViewAdapter adapter = new recyclerViewAdapter();
    private final MutableLiveData<ArrayList<Movie>> mLiveData = new MutableLiveData<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String receiveData = getIntent().getStringExtra("movie name");
        super.onCreate(savedInstanceState);
        setupUI(receiveData);
        firstLoadData(receiveData);
        progressBar.setVisibility(View.VISIBLE);
        mLiveData.observe(this, new Observer<ArrayList<Movie>>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Movie> strings) {
                if (tempMovieList != null && !tempMovieList.isEmpty()) {
                    adapter.movieData.addAll(tempMovieList);
                    adapter.notifyDataSetChanged();
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                }
            }
        });
        Log.d(TAG, "onCreate: ");
    }

    public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
        private final ArrayList<Movie> movieData = new ArrayList<>();

        public recyclerViewAdapter() {

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.textView.setOnClickListener(v -> {
                Movie[] tempMovie = movieData.toArray(new Movie[0]);
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(ShowActivity.this, DetailsActivity.class);
                intent.putExtra("movie need", tempMovie[position]);
                tempMovieHistory.add(tempMovie[position]);
                startActivity(intent);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(movieData.toArray(new Movie[0])[position].getTitle());
        }

        @Override
        public int getItemCount() {
            return movieData.size();

        }

    }

    private void setupUI(String receiveData) {

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button = findViewById(R.id.button_);
        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        button.setOnClickListener(v -> {
            Movie[] movieHistory = tempMovieHistory.toArray(new Movie[0]);
            Intent intent = new Intent(ShowActivity.this, HistoryActivity.class);
            intent.putExtra("movie list", movieHistory);
            startActivity(intent);
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView _recyclerView, int dx, int dy) {
                super.onScrolled(_recyclerView, dx, dy);
                if (!_recyclerView.canScrollVertically(1)) {
                    currentPage++;
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread(() -> {
                        tempMovieList = new ArrayList<>(LoadDataTools.methodLoadListView(receiveData, currentPage));
                        mLiveData.postValue(tempMovieList);
                    }).start();
                }
            }
        });
    }

    private void firstLoadData(String receiveData) {
        new Thread(() -> {
            tempMovieList = new ArrayList<>(LoadDataTools.methodLoadListView(receiveData, currentPage));
            mLiveData.postValue(tempMovieList);
            if (tempMovieList.isEmpty()) {
                runOnUiThread(() -> Toast.makeText(this, "no data", Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}



