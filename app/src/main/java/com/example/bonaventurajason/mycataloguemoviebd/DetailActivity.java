package com.example.bonaventurajason.mycataloguemoviebd;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.CONTENT_URI;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.ADULT;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.DATE;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.MOVIE_ID;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.POPULARITY;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.TITLE;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    // bind
    @BindView(R.id.image_movie_extras) ImageView image_extras;
    @BindView(R.id.title_movie_extras) TextView title_extras;
    @BindView(R.id.date_movie_extras) TextView date_extras;
    @BindView(R.id.popularity_movie_extras) TextView popularity_extras;
    @BindView(R.id.adult_movie_extras) TextView adult_extras;
    @BindView(R.id.overview_movie_extras) TextView overview_extras;
    @BindView(R.id.favorite) ImageView image_favorite;

    Movie movie;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        movie = new Movie(getIntent().getExtras().getInt("id"), getIntent().getExtras().getString("title"),
                getIntent().getExtras().getString("description"), getIntent().getExtras().getString("date"),
                getIntent().getExtras().getString("path"), getIntent().getExtras().getDouble("audience"),
                getIntent().getExtras().getBoolean("adult"));
        setTitle();
        setOverview();
        setDate();
        setPoster();
        setAdult();
        setPopularity();
        toggleFavorite();
    }

    @SuppressLint("SetTextI18n")

    public void setTitle() {
        //set title
        title_extras.setText(movie.getTitle());

    }
    public void setOverview() {
        //set description
        overview_extras.setText(movie.getDescription());

    }
    public void setDate() {
        //set date
        date_extras.setText(getString(R.string.release_date)+" " + movie.getDate());

    }
    public void setPoster() {
        //set poster using picasso
        Picasso.with(this).load(movie.getUrl())
                .into(image_extras);

    }
    public void setPopularity() {
        //set audience
        popularity_extras.setText(getString(R.string.popularity)+" " +String.valueOf(movie.getAudience()));

    }
    public void setAdult() {
        //set adult with two options, parental guidance and general audiences
        if (movie.isAdult()) {
            adult_extras.setText(R.string.adult);
        } else {
            adult_extras.setText(R.string.nonAdult);
        }
    }

    public void toggleFavorite() {
        // on/off star
        Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
        cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if(cursor.getCount() > 0){
                image_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                cursor.close();
            }

        }
        else image_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }
    public void newQueryFavorite(View view) {

        // insert new favorites to database
        toggleFavorite();
        Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
        cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {

            if (cursor.getCount() > 0) {
                uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
                getContentResolver().delete(uri, null, null);
                Toast.makeText(this, "Remove " + movie.getTitle() + " from favorite", Toast.LENGTH_SHORT).show();
                cursor.close();
            } else {
                uri = CONTENT_URI;
                ContentValues contentValues = new ContentValues();
                contentValues.put(MOVIE_ID, movie.getId());
                contentValues.put(TITLE, movie.getTitle());
                contentValues.put(OVERVIEW, movie.getDescription());
                contentValues.put(DATE, movie.getDate());
                contentValues.put(URL, movie.getPath());
                contentValues.put(POPULARITY, movie.getAudience());
                contentValues.put(ADULT, movie.isAdult());
                getContentResolver().insert(uri, contentValues);
                Toast.makeText(this, "Add " + movie.getTitle() + " to favorite", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
