package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_third);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String title = ("title:" + getIntent().getStringExtra("movie name"));
        String year = ("year:" + getIntent().getStringExtra("movie year"));
        String poster = getIntent().getStringExtra("movie poster");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(title);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(year);
        ImageView imageView1 = findViewById(R.id.imageView);
                String imageUrl = poster;
                Picasso.get().load(imageUrl).into(imageView1);
    }
}