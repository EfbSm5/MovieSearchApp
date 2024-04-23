package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.UiThread;
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
    ArrayList<String> Title = new ArrayList<>();
    ArrayList<String> Poster = new ArrayList<>();
    ArrayList<String> Year = new ArrayList<>();
    String Response = "no data";

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
        ListView list_view = (ListView) findViewById(R.id.list_view);
        TextView textView = (TextView) findViewById(R.id.textView2);
        sendRequestWithHttpURLConnection();
        String[] title = Title.toArray(new String[Title.size()]);
        String[] year = Year.toArray(new String[Year.size()]);
        String[] poster = Poster.toArray(new String[Poster.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, title);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("movie name", title[position]);
                intent.putExtra("movie year", year[position]);
                intent.putExtra("movie poster", poster[position]);
                startActivity(intent);
            }
        });
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SecondActivity.this, "Title", Toast.LENGTH_SHORT);
                        }
                    });
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
                    Response = response.toString();
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
            JSONObject jsonObject = new JSONObject(Response);
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
    }

}