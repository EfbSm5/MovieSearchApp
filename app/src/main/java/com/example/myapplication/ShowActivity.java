package com.example.myapplication;

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
    MutableLiveData<ArrayList<Movie>> mLiveData = new MutableLiveData<>();
    Set<Movie> tempMovieHistory = new LinkedHashSet<>();
    ArrayList<Movie> tempMovieList;


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
        recyclerViewAdapter adapter = new recyclerViewAdapter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                tempMovieList = new ArrayList<>(methodLoadData.methodLoadListView(receiveData, currentPage));
                mLiveData.postValue(tempMovieList);
            }
        }).start();
        Button button = (Button) findViewById(R.id.button_);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tempMovieList=new ArrayList<Movie>(methodLoadData.methodLoadListView(receiveData, currentPage));
                            mLiveData.postValue(tempMovieList);
                        }
                    }).start();
                }
            }
        });
        mLiveData.observe(this, new Observer<ArrayList<Movie>>() {

            @Override
            public void onChanged(ArrayList<Movie> strings) {
                Log.d(TAG, "This is Log:run: " + tempMovieList.size() + "    " + currentPage + "time" + "   methodOnChanged");
                if (tempMovieList != null && !tempMovieList.isEmpty()) {
                    adapter.movieData.addAll(tempMovieList);
                    adapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "progressView off");
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
    }

    public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
        ArrayList<Movie> movieData = new ArrayList<>();

        public recyclerViewAdapter() {

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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Intent intent = new Intent(ShowActivity.this, DetailsActivity.class);
                    intent.putExtra("movie need", movieData.toArray(new Movie[0])[position]);
                    tempMovieHistory.add(movieData.toArray(new Movie[0])[position]);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(movieData.toArray(new Movie[0])[position].getName());
        }

        @Override
        public int getItemCount() {
            if (movieData == null) {
                return 0;
            } else return movieData.size();

        }

    }
}

