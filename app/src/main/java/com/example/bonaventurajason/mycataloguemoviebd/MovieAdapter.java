package com.example.bonaventurajason.mycataloguemoviebd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;
    private Context context;
    private Button btnDetail;
    private Button btnShare;

    //constructor MovieAdapter

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }
    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_cardview, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvDate.setText(movies.get(position).getDate());
        holder.tvOverview.setText(movies.get(position).getDescription());
        //using picasso for image
        Picasso.with(context).load(movies.get(position).getUrl())
                .into(holder.ivImage);
        //set button detail clickable
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("description", movie.getDescription());
                intent.putExtra("date", movie.getDate());
                intent.putExtra("path", movie.getPath());
                intent.putExtra("audience", movie.getAudience());
                intent.putExtra("adult", movie.isAdult());
                context.startActivity(intent);
            }
        });
        //set button share clickable
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                Movie movie = movies.get(position);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                String text = "Hey, check this movie, i recommend this movie to you!\n" + movie.getTitle()
                        + "\nDate: " + movie.getDate()
                        + "\nImage: " + movie.getUrl();
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movies != null) ? movies.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDate, tvOverview;
        private ImageView ivImage;
        MovieViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.movie_name);
            tvDate = (TextView) itemView.findViewById(R.id.movie_date);
            tvOverview = (TextView) itemView.findViewById(R.id.movie_overview);
            ivImage = (ImageView) itemView.findViewById(R.id.movie_path);
            btnDetail = (Button) itemView.findViewById(R.id.detail);
            btnShare = (Button) itemView.findViewById(R.id.share);
        }
    }
}