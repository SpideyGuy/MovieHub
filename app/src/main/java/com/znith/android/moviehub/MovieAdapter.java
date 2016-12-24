package com.znith.android.moviehub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by spidey on 12/22/16.
 */

public class MovieAdapter extends BaseAdapter {

    private Context mContext;

    ArrayList<MovieModel> movieList = new ArrayList<>();

    public MovieAdapter(Context c, ArrayList<MovieModel> movies) {
        mContext = c;
        movieList = movies;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomImageView imageView;
        String imageUrl = "http://image.tmdb.org/t/p/w185" +
                "" + movieList.get(position).getImage();
        if (convertView == null) {
            imageView = new CustomImageView(mContext);
            imageView.setScaleType(CustomImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (CustomImageView) convertView;
        }

        Picasso.with(mContext).load(imageUrl).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("id", movieList.get(position).getId());
                bundle.putString("title", movieList.get(position).getTitle());
                bundle.putString("overview", movieList.get(position).getOverview());
                bundle.putDouble("rate", movieList.get(position).getRate());
                bundle.putString("release_date", movieList.get(position).getReleaseDate());
                bundle.putString("image", movieList.get(position).getImage());
                bundle.putString("sliderImage", movieList.get(position).getAltImage());


                Intent detailActivity = new Intent(mContext, MovieDetail.class);
                detailActivity.putExtras(bundle);
                mContext.startActivity(detailActivity);
            }
        });

        return imageView;
    }
}
