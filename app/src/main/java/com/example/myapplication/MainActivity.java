package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.db.Data;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Data movie = new Data();
        binding.setMovie(movie);
        //database for the future
        binding.button.setOnClickListener(v -> {
            String inputText = movie.text;
            if (inputText.isEmpty()) {
                Toast.makeText(MainActivity.this, "please input title", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(AppUtil.getInstance().getContext(), ShowActivity.class);
                intent.putExtra("movie name", inputText);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}