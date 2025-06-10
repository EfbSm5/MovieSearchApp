package com.example.myapplication;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.ActivityShowBinding;
import com.example.myapplication.db.Movie;
import com.example.myapplication.ui.RecyclerViewAdapter;

import java.util.ArrayList;


public class ShowActivity extends AppCompatActivity implements ShowActivityController.ShowActivityControllerInterface {

    private ProgressBar progressBar;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ShowActivityController controller = new ShowActivityController(getIntent().getStringExtra("movie name"), this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityShowBinding binding = ActivityShowBinding.inflate(getLayoutInflater());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = binding.button;
        progressBar = binding.progressBar;
        RecyclerView recyclerView = binding.recyclerview;

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(movie -> {
            Intent intent = new Intent(AppUtil.getInstance().getContext(), DetailsActivity.class);
            intent.putExtra("movie need", movie);
            controller.addHistory(movie);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppUtil.getInstance().getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(VISIBLE);

        controller.mLiveData.observe(this, new Observer<ArrayList<Movie>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Movie> strings) {
                adapter.setMovieData(strings);
            }
        });

        button.setOnClickListener(v -> controller.onClickHistoryBtn());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView _recyclerView, int dx, int dy) {
                super.onScrolled(_recyclerView, dx, dy);
                if (!_recyclerView.canScrollVertically(1)) {
                    controller.onScrolled();
                }
            }
        });
    }

    @Override
    public void noData() {
        Toast.makeText(AppUtil.getInstance().getContext(), "no data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressbarVisibility(Boolean progressbarVisibility) {
        if (progressbarVisibility) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(INVISIBLE);
        }
    }
}



