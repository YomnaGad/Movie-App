package com.example.android.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yomna on 9/8/2016.
 */
public class MyRecyclerAdapterReviews extends RecyclerView.Adapter <MyRecyclerAdapterReviews.CustomViewHolder> {
private List<Review> reviewItemList;
private Context mContext;

public MyRecyclerAdapterReviews(Context context, List<Review> reviewItemList) {
        this.reviewItemList = reviewItemList;
        this.mContext = context;

        }


@Override
public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
        }

@Override
public void onBindViewHolder(CustomViewHolder holder, final int position) {




        holder.textView.setText(reviewItemList.get(position).getContent());

        }

@Override
public int getItemCount() {
        return (null != reviewItemList ? reviewItemList.size() : 0);
        }

public class CustomViewHolder extends RecyclerView.ViewHolder {
    // protected ImageView imageView;
    protected TextView textView;

    public CustomViewHolder(View view) {
        super(view);
        // this.imageView = (ImageView) view.findViewById(R.id.trailer_image);
        this.textView = (TextView) view.findViewById(R.id.list_item_review_textview);
    }
}

}

