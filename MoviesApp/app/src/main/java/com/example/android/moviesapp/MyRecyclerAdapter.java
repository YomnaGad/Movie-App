package com.example.android.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Yomna on 8/15/2016.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
    private List<Movies> movies;
    private AppCompatActivity mContext;

    public MyRecyclerAdapter(AppCompatActivity context, List<Movies> movies) {
        this.movies = movies;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Movies moviesItem = movies.get(i);
          final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185//";
        String posterPath = moviesItem.getPoster_path();
        Uri buildUri = Uri.parse(POSTER_BASE_URL).buildUpon().appendPath(posterPath).build();
        String url = buildUri.toString();
        //Download image using picasso library
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185//"+
                moviesItem.getPoster_path())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imageView);
        View.OnClickListener clickListener = new View.OnClickListener() {
            TextView textView ;
            @Override
            public void onClick(View view) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getPosition();
                // t2sod da ?
                // aha
                DetailFragment.position = position;
                Movies moviesItem = movies.get(position);
               // Toast.makeText(mContext, moviesItem.getTitle(), Toast.LENGTH_SHORT).show();
                textView = (TextView)view.findViewById(R.id.overview);

               // methodStartActivity(mContext, moviesItem);
              //  Intent intent = new Intent(mContext, DetailActivity.class).putExtra(Intent.EXTRA_TEXT,moviesItem.getTitle() );


                if(MainActivity.mTwoPane == false) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_TITLE", moviesItem.getTitle());
                    extras.putString("EXTRA_OVERVIEW", moviesItem.getOverview());
                    extras.putDouble("EXTRA_AVERAGE_RATE", moviesItem.getVote_average());
                    extras.putString("EXTRA_RELEASE", moviesItem.getRelease_date());
                    extras.putString("EXTRA_POSTER", "http://image.tmdb.org/t/p/w185//" +
                            moviesItem.getPoster_path());
                    extras.putString("EXTRA_ID", moviesItem.getId());
                    extras.putString("EXTRA_BACKDROP", "http://image.tmdb.org/t/p/w185//" +
                            moviesItem.getBackdrop_path());
                    intent.putExtras(extras);
                    mContext.startActivity(intent);
                }
                else {
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_TITLE", moviesItem.getTitle());
                    extras.putString("EXTRA_OVERVIEW", moviesItem.getOverview());
                    extras.putDouble("EXTRA_AVERAGE_RATE", moviesItem.getVote_average());
                    extras.putString("EXTRA_RELEASE", moviesItem.getRelease_date());
                    extras.putString("EXTRA_POSTER", "http://image.tmdb.org/t/p/w185//" +
                            moviesItem.getPoster_path());
                    extras.putString("EXTRA_ID", moviesItem.getId());
                    extras.putString("EXTRA_BACKDROP", "http://image.tmdb.org/t/p/w185//" +
                            moviesItem.getBackdrop_path());

                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setArguments(extras);

                    mContext.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, detailFragment)
                            .commit();


                }

            }


        };


        customViewHolder.imageView.setOnClickListener(clickListener);


        customViewHolder.imageView.setTag(customViewHolder);
    }


    @Override
    public int getItemCount() {
        return (null != movies ? movies.size() : 0);
    }




    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
       // protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
          //  this.textView = (TextView) view.findViewById(R.id.title);
        }
    }

}