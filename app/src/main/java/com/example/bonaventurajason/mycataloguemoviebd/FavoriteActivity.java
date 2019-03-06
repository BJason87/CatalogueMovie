package com.example.bonaventurajason.mycataloguemoviebd;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.CONTENT_URI;

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = FavoriteActivity.class.getSimpleName();


    @BindView(R.id.rv_favorite)
    RecyclerView rvFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvFavorite.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<Movie> movies = new ArrayList<>();
        Uri uri = CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    movies.add(new Movie(cursor));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }
            cursor.close();
        }


        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rvFavorite.setAdapter(movieAdapter);
    }
}