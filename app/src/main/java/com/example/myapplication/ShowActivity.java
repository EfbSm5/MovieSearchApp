package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Movie;
import com.example.myapplication.network.MovieNetworkManager;
import com.example.myapplication.network.parseJsonTools;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowActivity extends AppCompatActivity {

    private int currentPage = 1;
    private ProgressBar progressBar;
    private parseJsonTools parse;
    public Set<Movie> tempMovieHistory = new LinkedHashSet<>();
    RecyclerView recyclerView;
    SmartRefreshLayout smartRefreshLayout;

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
        String receivedata = getIntent().getStringExtra("movie name");
        Button button = (Button) findViewById(R.id.button_);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        smartRefreshLayout=(SmartRefreshLayout)findViewById(R.id.smartRefresh);
        methodLoadListView(receivedata, currentPage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie[] movieHistory = tempMovieHistory.toArray(new Movie[0]);
                Intent intent = new Intent(ShowActivity.this, HistoryActivity.class);
                intent.putExtra("movie list", movieHistory);
                startActivity(intent);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);



            }
        });
    }

    public void methodLoadListView(String _receiveData, int _currentPage) {
        MovieNetworkManager movieNetworkManager = new MovieNetworkManager();
        parse = new parseJsonTools();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieNetworkManager.sendRequestWithHttpUrl(_receiveData, _currentPage);
                    if (!parse.parseJSONWithJSON(movieNetworkManager.responseData)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShowActivity.this, "没有更多了或者太多了", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ShowActivity.this);
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            List<String> namesList = parse.movies.stream()
                                    .map(Movie::getName)
                                    .collect(Collectors.toList());
                            recyclerViewAdapter adapter = new recyclerViewAdapter();
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
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
                    intent.putExtra("movie need", parse.movieArray[position]);
                    tempMovieHistory.add(parse.movieArray[position]);
                    startActivity(intent);
                }
            });
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(parse.movieArray[position].getName());
        }
        @Override
        public int getItemCount() {
            return parse.movies.size();
        }
    }
}

