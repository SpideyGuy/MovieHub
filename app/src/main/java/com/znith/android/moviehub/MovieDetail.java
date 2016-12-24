package com.znith.android.moviehub;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private ImageView mSlideImageView;
    private ImageView mCoverImage;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mRate;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        String title = bundle.getString("title");
        String overview = bundle.getString("overview");
        Double rate = bundle.getDouble("rate");
        String releaseDate = bundle.getString("release_date");
        String altImage = bundle.getString("sliderImage");
        String image = bundle.getString("image");

        String coverImageUrl = "http://image.tmdb.org/t/p/w500" + image;
        String sliderImageUrl = "http://image.tmdb.org/t/p/w500" + altImage;

        mTitle = (TextView) findViewById(R.id.title);
        mOverview = (TextView) findViewById(R.id.overview);
        mRate = (TextView) findViewById(R.id.rate);
        mReleaseDate = (TextView) findViewById(R.id.releaseDate);
        mCoverImage = (ImageView) findViewById(R.id.coverImage);
        mSlideImageView = (ImageView) findViewById(R.id.backdrop);

        mTitle.setText(title);
        mOverview.setText(overview);
        mReleaseDate.setText(releaseDate);
        mRate.setText(rate.toString() + "/10");
        Picasso.with(MovieDetail.this).load(coverImageUrl).into(mCoverImage);
        Picasso.with(MovieDetail.this).load(sliderImageUrl).into(mSlideImageView);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            MovieDetail.this.finish();
        }

        return true;
    }
}
