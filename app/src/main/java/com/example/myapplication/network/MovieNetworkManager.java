package com.example.myapplication.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieNetworkManager {
    public String responseData;
    public boolean finishFlag = false;

    public void sendRequestWithOkhttp(String receiveData, int currentPage) {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://www.omdbapi.com/?apikey=7852f5f&s=" + receiveData + "&page=" + currentPage).build();
            Response response = client.newCall(request).execute();
            responseData = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequestWithHttpUrl(String receiveData, int currentPage) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://www.omdbapi.com/?apikey=7852f5f&s=" + receiveData + "&page=" + currentPage);
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
            responseData = response.toString();
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

}



