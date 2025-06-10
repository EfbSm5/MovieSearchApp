package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.Movie;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Movie> movieData = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        this.movieData = movieData;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
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
        holder.textView.setOnClickListener(v -> {
            Movie[] tempMovie = movieData.toArray(new Movie[0]);
            int position = holder.getAdapterPosition();
            onItemClickListener.onItemClicked(tempMovie[position]);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(movieData.toArray(new Movie[0])[position].getTitle());
    }

    @Override
    public int getItemCount() {
        return movieData.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(Movie movie);
    }
}

