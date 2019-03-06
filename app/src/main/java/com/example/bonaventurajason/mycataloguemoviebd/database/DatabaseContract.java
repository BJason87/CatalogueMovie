package com.example.bonaventurajason.mycataloguemoviebd.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

//    Daftar nama tabel dalam database
    public static String TABLE_FAVORITE_MOVIE = "favorite_movie";

    public static final class FavoriteMovieColumns implements BaseColumns {
        public static String MOVIE_ID = "movie_id";
        public static String TITLE = "title";
        public static String OVERVIEW = "description";
        public static String DATE = "date";
        public static String URL = "url";
        public static String POPULARITY = "audience";
        public static String ADULT = "adult";
    }

    public static final String AUTHORITY = "com.example.bonaventurajason.mycataloguemoviebd";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE_MOVIE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

}
