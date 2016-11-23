package com.example.android.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yomna on 9/7/2016.
 */
public class MyRecyclerAdapterTrailers extends RecyclerView.Adapter<MyRecyclerAdapterTrailers.CustomViewHolder> {
    private List<Trailers> trailerItemList;
    private Context mContext;

    public MyRecyclerAdapterTrailers(Context context, List<Trailers> trailerItemList) {
        this.trailerItemList = trailerItemList;
        this.mContext = context;
        Log.v("ooooo", String.valueOf(this.trailerItemList.size()));
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        Log.v("ooooo", trailerItemList.get(position).getName());


        holder.textView.setText(trailerItemList.get(position).getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = trailerItemList.get(position).getKey().substring(1, trailerItemList.get(position).getKey().length()-1);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                        +key)));

                Log.v("Key Value",trailerItemList.get(position).getKey());


            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = trailerItemList.get(position).getKey().substring(1, trailerItemList.get(position).getKey().length()-1);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                        +key)));

                Log.v("Key Value",trailerItemList.get(position).getKey());


            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != trailerItemList ? trailerItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
         protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
             this.imageView = (ImageView) view.findViewById(R.id.trailer_image);
            this.textView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }

}
