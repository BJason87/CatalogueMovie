package com.example.bonaventurajason.mycataloguemoviebd;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ComingSoon extends Fragment {

    RecyclerView rvUpcoming;
    MovieAdapter movieAdapter;
    ArrayList<Movie> movies;

    ExecutorService threadPool;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);

        rvUpcoming = (RecyclerView) rootView.findViewById(R.id.rv_upcoming);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvUpcoming.setLayoutManager(layoutManager);

        movies = new ArrayList<>();

        ComingSoon.GetMovies task = new ComingSoon.GetMovies();
        System.out.println("Processing get Movies...");

        threadPool = Executors.newFixedThreadPool(3);
        Future<Integer> future = threadPool.submit(task);
        while (!future.isDone()) {
            System.out.println("Movies not processed yet!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        movieAdapter = new MovieAdapter(getActivity(), movies);
        rvUpcoming.setAdapter(movieAdapter);
        System.out.println("Get Movies Done!");

        threadPool.shutdown();

        return rootView;
    }

    private class GetMovies implements Callable<Integer> {
        @Override
        public Integer call() {
            // Create URL
            URL githubEndpoint = null;
            try {
                githubEndpoint = new URL("https://api.themoviedb.org/3/movie/upcoming?api_key=72fbac04db947004b7b94fe4b9aa6eb4&language=en-US");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // Create connection
            HttpsURLConnection apiConnection;
            try {
                apiConnection = (HttpsURLConnection) githubEndpoint.openConnection();
                if (apiConnection.getResponseCode() == 200) {
                    // Read JSON
                    BufferedReader br = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONArray movieArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < movieArray.length(); i++) {
                        JSONObject movie = movieArray.getJSONObject(i);

                        Movie newMovie = new Movie(movie.getInt("id"),
                                movie.getString("title"),
                                movie.getString("overview"),
                                movie.getString("release_date"),
                                movie.getString("poster_path"),
                                movie.getDouble("popularity"),
                                movie.getBoolean("adult"));

                        movies.add(newMovie);
                    }
                } else {
                    System.out.println("Response code not OK");
                }
                apiConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
