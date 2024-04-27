package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private ArrayList<String> Title = new ArrayList<>();
    private ArrayList<String> Poster = new ArrayList<>();
    private ArrayList<String> Year = new ArrayList<>();
    private String[] title;
    private String[] year;
    private String[] poster;
    private String MovieName = "no data";
    private ArrayList<String> historyInput = new ArrayList<>();
    private int currentPage = 0;

    private boolean sign = false;
    private boolean sign1 = false;

    private ArrayAdapter<String> adapter;

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
        sendRequestWithHttpURLConnection();
        ListView list_view = (ListView) findViewById(R.id.list_view);
        loadMoreData();
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("movie name", title[position]);
                intent.putExtra("movie year", year[position]);
                intent.putExtra("movie poster", poster[position]);
                startActivity(intent);
                historyInput.add(title[position]);
            }
        });
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 检测是否滚动到列表底部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE

                        && list_view.getLastVisiblePosition() == adapter.getCount() - 1) {
                    // 滚动到底部，加载更多数据

                    Log.w("TAG", "Warning level message");
                    if (sign) {
                        Toast.makeText(SecondActivity.this, "到底啦", Toast.LENGTH_SHORT);
                    } else {
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    private void refreshData() {
        title = Title.toArray(new String[Title.size()]);
        year = Year.toArray(new String[Year.size()]);
        poster = Poster.toArray(new String[Poster.size()]);
    }

    //假如45个记录，这时max为4，此时currentPage最高可以到3,最高拉到了40个数据。title.length为45，currentPage*10为40，减去是5
    private void loadMoreData() {
        String[] tempOutput = new String[10];
        refreshData();
        int i = 0;
        ArrayList<String> tempOutput1 = new ArrayList<>();
        while (i < 10) {
            while (title[currentPage * 10 + i].isEmpty()) {
                if (sign1) {
                    sign = true;
                    break;
                } else {
                    refreshData();
                }
            }
            tempOutput1.add(title[currentPage * 10 + i]);
            i++;
        }
        tempOutput = tempOutput1.toArray(new String[tempOutput1.size()]);
        currentPage++;
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, tempOutput);
        adapter.notifyDataSetChanged();
    }


    private void sendRequestWithHttpURLConnection() {

        String receivedata = getIntent().getStringExtra("movie name");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.omdbapi.com/?apikey=7852f5f&s=" + receivedata);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {

                        response.append(line);

                    }
                    MovieName = response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        });
        thread.start();
        try {
            thread.join();
            JSONObject jsonObject = new JSONObject(MovieName);
            JSONArray searchArray = jsonObject.getJSONArray("Search");
            for (int i = 0; i < searchArray.length(); i++) {
                JSONObject movieObject = searchArray.getJSONObject(i);
                Title.add(movieObject.getString("Title"));
                Year.add(movieObject.getString("Year"));
                Poster.add(movieObject.getString("Poster"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sign1 = true;
    }

}