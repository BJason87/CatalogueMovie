package com.example.bonaventurajason.mycataloguemoviebd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

public class SearchActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    RecyclerView rvSearch;
    EditText searchInput;
    ExecutorService threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvSearch = (RecyclerView) findViewById(R.id.rv_search);
        searchInput = (EditText) findViewById(R.id.search_input);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(layoutManager);
    }

    public void addFilm(View view) {
        movies = new ArrayList<>();
        MovieList task = new MovieList();

        threadPool = Executors.newFixedThreadPool(3);
        Future<Integer> future = threadPool.submit(task);

        while (!future.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rvSearch.setAdapter(movieAdapter);

        threadPool.shutdown();
    }

    private class MovieList implements Callable<Integer> {
        @Override
        public Integer call() {
            // Create URL
            String[] input = searchInput.getText().toString().split(" ");
            String query = input[0];
            for (int i = 1; i < input.length; i++) {
                query += "+" + input[i];
            }
            URL endpoint = null;
            try {
                endpoint = new URL("https://api.themoviedb.org/3/search/movie?api_key=72fbac04db947004b7b94fe4b9aa6eb4&language=en-US&query=" + query);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //Membuat koneksinya
            HttpsURLConnection apiConnection;


            try {
                apiConnection = (HttpsURLConnection) endpoint.openConnection();
                if (apiConnection.getResponseCode() == 200) {
                    //Membaca data JSON
                    BufferedReader br = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    //membuat object dari JSONObject
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONArray movieArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < movieArray.length(); i++) {
                        JSONObject movie = movieArray.getJSONObject(i);

                        //membuat objek newMovie
                        Movie newMovie = new Movie(movie.getInt("id"),
                                movie.getString("title"),
                                movie.getString("overview"),
                                movie.getString("release_date"),
                                movie.getString("poster_path"),
                                movie.getDouble("popularity"),
                                movie.getBoolean("adult"));

                        movies.add(newMovie);
                    }
                }
                apiConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
