package com.example.android.moviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Yomna on 9/10/2016.
 */
public class MoviesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;
    static final int MOVIES = 100;
    static final int MOVIES_WITH_ID = 200;
    private static final String sMovieWithID =
            MoviesContract.MovieEntry.TABLE_NAME+
                    "." + MoviesContract.MovieEntry._ID + " =";
    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MoviesContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIE + "/*", MOVIES_WITH_ID);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = null ;
        if(sUriMatcher.match(uri)== MOVIES){

            cursor = db.rawQuery("select* from "+ MoviesContract.MovieEntry.TABLE_NAME,null);


        }
        if (sUriMatcher.match(uri)== MOVIES_WITH_ID) {
            cursor = db.rawQuery("select* from "+ MoviesContract.MovieEntry.TABLE_NAME+
                    " where "+ MoviesContract.MovieEntry._ID + "= " + selection,null);
        }
        if(cursor.isAfterLast())
            return null ;
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(sUriMatcher.match(uri)== MOVIES){
            db.insert(MoviesContract.MovieEntry.TABLE_NAME,null,values);
        }
        else {
          //  throw new android.database.SQLException("Error", uri);

        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if(sUriMatcher.match(uri)== MOVIES){
            db.delete(MoviesContract.MovieEntry.TABLE_NAME,"_ID=" + selection,null);
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
