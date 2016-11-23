package com.example.android.moviesapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.data.MoviesContract;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {
    private Context mContext;
    private List<Trailers> trailer = new ArrayList<>();
    private List<Review> review = new ArrayList<>();
    private RecyclerView mRecyclerViewTrailer;
    private RecyclerView mRecyclerViewReview;
    private MyRecyclerAdapterTrailers adapterTrailer;
    private MyRecyclerAdapterReviews adapterReview;
   // private FetchTrailerTask taskTrailer = new FetchTrailerTask();
  //  private FetchReviewTask taskReview = new FetchReviewTask();
    private List<String> keys = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private String urlTrailer ;
    private String urlReview;
    private String id ;
    private Button button;
    private Cursor cursor ;
    public  static int position= 0;
    String titleString ;
    String overviewString ;
    Double averageRateString ;
    String releaseString  ;
    String posterString ;
    String idString ;
    String backPath;

    public static String buttonText ;
    public DetailFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        String pref = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_favorite));
        if(pref.equals("favorite") && button.getText().equals("Mark as favorite")) {
            if(hasIndex(position)) {
                TabFragment.moviesList.remove(position);
                TabFragment.adapter.notifyDataSetChanged();
           }
            TabFragment.adapter.notifyDataSetChanged();
        }


//
//
    }
    public boolean hasIndex(int index){
        if(index < TabFragment.moviesList.size())
            return true;
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mRecyclerViewTrailer = (RecyclerView) rootView.findViewById(R.id.recycler_view_trailer);
        mRecyclerViewReview = (RecyclerView) rootView.findViewById(R.id.recycler_view_review);
        button = (Button)rootView.findViewById(R.id.favorite);
        /////////////////////////////////////////
        mRecyclerViewTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerViewTrailer.setItemAnimator(new DefaultItemAnimator());


        mRecyclerViewReview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerViewReview.setItemAnimator(new DefaultItemAnimator());

//        mRecyclerViewReview.addItemDecoration(new SimpleDividerItemDecoration(
//                mContext
//        ));

        Intent intent = getActivity().getIntent();


        Bundle extras;
        if(MainActivity.mTwoPane){
            //kmly
            extras = getArguments();

        }
        else
            extras = intent.getExtras();


        if(extras != null) {
             titleString = extras.getString("EXTRA_TITLE");
             overviewString = extras.getString("EXTRA_OVERVIEW");
            averageRateString = extras.getDouble("EXTRA_AVERAGE_RATE");
             releaseString = extras.getString("EXTRA_RELEASE");
             posterString = extras.getString("EXTRA_POSTER");
             idString = extras.getString("EXTRA_ID");
            backPath = extras.getString("EXTRA_BACKDROP");

//            ((TextView) rootView.findViewById(R.id.title))
//                    .setText("Title: " + titleString);
            ((TextView) rootView.findViewById(R.id.overview))
                    .setText("Overview: " + overviewString);
            ((TextView) rootView.findViewById(R.id.average_rate))
                    .setText( averageRateString.toString());
            ((TextView) rootView.findViewById(R.id.release_date))
                    .setText( releaseString.toString());
            Picasso.with(mContext).load(posterString)
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into((ImageView) rootView.findViewById(R.id.poster));
            Picasso.with(mContext).load(backPath)
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into((ImageView) rootView.findViewById(R.id.path_back));

            id = idString.toString();

            checkButton(rootView, id, button);


            // keda ?
// dol tb3 el condition
            // estny
            try {
                Ion.with(this)
                        .load("http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                // do stuff with the result or error
                                if (result != null && result.has("results")) {
                                    JsonArray ja = result.get("results").getAsJsonArray();
                                    for (int i = 0; i < ja.size(); i++) {
                                        Trailers trals = new Trailers();
                                        trals.setName(ja.get(i).getAsJsonObject().get("name").toString());
                                        trals.setKey(ja.get(i).getAsJsonObject().get("key").toString());
                                        trals.setId(ja.get(i).getAsJsonObject().get("id").toString());

                                        trailer.add(trals);
                                    }
                                }
                                adapterTrailer = new MyRecyclerAdapterTrailers(getContext(), trailer);
                                mRecyclerViewTrailer.setAdapter(adapterTrailer);// l7za
//                                mRecyclerViewTrailer.addItemDecoration(new SimpleDividerItemDecoration(
//                                        mContext
//                                ));

                            }
                        });
            } catch (Exception e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
            try {
                Ion.with(this)
                        .load("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=")
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                // do stuff with the result or error
                                if (result != null && result.has("results")) {
                                    JsonArray ja = result.get("results").getAsJsonArray();
                                    for (int i = 0; i < ja.size(); i++) {
                                        Review reviews = new Review();
                                        reviews.setContent(ja.get(i).getAsJsonObject().get("content").toString());


                                        review.add(reviews);
                                    }
                                }
                                adapterReview = new MyRecyclerAdapterReviews(getContext(), review);

                                mRecyclerViewReview.setAdapter(adapterReview);


                            }
                        });
            } catch (Exception e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }

        }
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (button.getText().toString()){
                    case "Mark as favorite" :
                        ContentValues values = new ContentValues();
                        values.put("_ID", Integer.parseInt(id));
                        values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATA, releaseString);
                        values.put(MoviesContract.MovieEntry.COLUMN_TITLE, titleString);
                        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, overviewString);
                        values.put(MoviesContract.MovieEntry.COLUMN_POSTER, posterString);
                        values.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, averageRateString);
                        values.put(MoviesContract.MovieEntry.COLUMN_PATH_BACK, backPath);
                        getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);
                        cursor = getActivity().getContentResolver()
                                .query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
                        button.setText("unfavorite");

                        if (cursor != null) {
                            cursor.moveToFirst();
                            Log.v("cursor", cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2)
                                    + "," + cursor.getString(3)
                                    + "," + cursor.getString(4) + "," + cursor.getString(5) +  "," + cursor.getString(6));
                        }

                    break;
                    case "unfavorite" :
                        getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, String.valueOf(id), null);
                        cursor = getActivity().getContentResolver()
                                .query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
                        button.setText("Mark as favorite");

//                        TabFragment.moviesList.remove(position);
//                        TabFragment.adapter.notifyDataSetChanged();

                        break;
                        default:
                            break;
                }
                buttonText = button.getText().toString();

            }

        });





        return rootView;
    }



    public void checkButton (View view , String id, Button button){
        cursor = getActivity().getContentResolver()
                .query(MoviesContract.MovieEntry.buildMovieUriWithID(id), null, id, null, null);

        if (cursor != null) {
            button.setText("unfavorite");
            Log.v("cursor id ", id);
        }



    }



}
