package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.network.MovieNetworkManager;
import com.example.myapplication.network.parseJSON;
import com.example.myapplication.db.Movie;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private int currentPage = 1;
    private ArrayAdapter<String> adapter;
    public Movie movie[];
    private String[] movielist = new String[10];
    ListView list_view;
    ProgressBar progressBar;
    parseJSON parse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String receivedata = getIntent().getStringExtra("movie name");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        list_view = (ListView) findViewById(R.id.list_view);
        methodLoadListView(receivedata, currentPage);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("movie number", position);
                intent.putExtra("movie need", parse);
                startActivity(intent);
            }
        });
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && list_view.getLastVisiblePosition() == adapter.getCount() - 1) {
                    currentPage++;
                    methodLoadListView(receivedata, currentPage);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    public void methodLoadListView(String areceiveData, int acurrentPage) {
        MovieNetworkManager movieNetworkManager = new MovieNetworkManager();
        parse = new parseJSON();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieNetworkManager.sendRequestWithHttpUrl(areceiveData, acurrentPage);
                    if (!parse.parseJSONWithJSON(movieNetworkManager.responseData)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SecondActivity.this, "没有更多了或者太多了", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                        return;
                    }


                    String[] movielist = new String[parse.dataLength];
                    for (int i = 0; i < parse.dataLength; i++) {
                        movielist[i] = parse.movie[i].getName();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_list_item_1, movielist);
                            list_view.setAdapter(adapter);
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

}

