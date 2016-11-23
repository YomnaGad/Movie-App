package com.example.android.moviesapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yomna on 9/9/2016.
 */
public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";



    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;


        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_POSTER = "results_poster_path";

        public static final String COLUMN_OVERVIEW = "results_overview";

        public static final String COLUMN_RELEASE_DATA = "results_release_data";

        public static final String COLUMN_TITLE = "results_title";

        public static final String COLUMN_VOTE_AVERAGE = "results_vote_average";

        public static final String COLUMN_PATH_BACK = "path_back";

        public static Uri buildMovieUriWithID(String id) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).appendPath(id).build();
        }




    }
}
