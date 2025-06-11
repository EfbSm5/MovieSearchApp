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
import com.example.myapplication.db.Movie;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Movie movie = (Movie) getIntent().getSerializableExtra("movie need");
        TextView textView = findViewById(R.id.textView);
        textView.setText((movie.getTitle()));
        TextView textView1 = findViewById(R.id.textView1);
        textView1.setText((movie.getYear()));
        ImageView imageView1 = findViewById(R.id.imageView);
        String imageUrl = movie.getPoster();
        Picasso.get().load(imageUrl).into(imageView1);
    }
}