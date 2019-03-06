package com.example.bonaventurajason.mycataloguemoviebd;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.ADULT;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.DATE;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.MOVIE_ID;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.POPULARITY;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.TITLE;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.FavoriteMovieColumns.URL;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.getColumnInt;
import static com.example.bonaventurajason.mycataloguemoviebd.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    public int id;
    public String title;
    public String description;
    public String date;
    public String url;
    public String path;
    public double audience;
    public boolean adult;

    public Movie(int id, String title, String overview, String date, String url, double popularity, boolean adult) {
        this.id = id;
        this.title = title;
        this.description = overview;
        this.date = date;
        this.path = url;
        this.url = "http://image.tmdb.org/t/p/w185/" + this.path;
        this.audience = popularity;
        this.adult = adult;
    }
    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, MOVIE_ID);
        this.title = getColumnString(cursor, TITLE);
        this.description = getColumnString(cursor, OVERVIEW);
        this.date = getColumnString(cursor, DATE);
        this.path = getColumnString(cursor, URL);
        this.url = "http://image.tmdb.org/t/p/w185/" + this.path;
        this.audience = getColumnInt(cursor, POPULARITY);
        this.adult = getColumnInt(cursor, ADULT) > 0;
    }


    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getUrl() {
        return url;
    }
    public String getPath() {
        return path;
    }
    public double getAudience() {
        return audience;
    }
    public boolean isAdult() {
        return adult;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                ", audience=" + audience +
                ", adult=" + adult +
                '}';
    }

    public Movie(Parcel in) {
        String[] data = new String[9];

        in.readStringArray(data);
        this.id = Integer.valueOf(data[0]);
        this.title = data[1];
        this.description = data[2];
        this.date = data[3];
        this.path = data[4];
        this.url = "http://image.tmdb.org/t/p/w185/" + this.path;
        this.audience = Double.valueOf(data[7]);
        this.adult = Boolean.valueOf(data[8]);
    }


    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(this.id),
                this.title,
                this.description,
                this.date,
                this.path,
                String.valueOf(this.audience),
                String.valueOf(this.adult)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
