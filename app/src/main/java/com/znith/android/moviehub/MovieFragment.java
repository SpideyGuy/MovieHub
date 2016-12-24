package com.znith.android.moviehub;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by spidey on 12/23/16.
 */

public class MovieFragment extends Fragment {


    GridView coverImage;

    MovieAdapter movieAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View baseView = inflater.inflate(R.layout.fragment_movie, container, false);


        coverImage = (GridView) baseView.findViewById(R.id.movieGrid);

        if (NetworkUtils.isConnected(getActivity())) {
            new FetchMovieList().execute("popular");
        } else {
            Toast.makeText(getActivity(), "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }

        return baseView;
    }

    private class FetchMovieList extends AsyncTask<String, Void, ArrayList<MovieModel>> {

        private final String LOG_TAG = MovieFragment.class.getSimpleName();

        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Loading");
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String movieList = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuffer buffer;

            try {

                final String BASE_URL = "https://api.themoviedb.org/3/movie/" + params[0];
                final String API_KEY = "api_key";

                Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_KEY, getResources().getString(R.string.api_key)).build();

                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieList = buffer.toString();
                try {
                    return getMovieList(movieList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> movieList) {
            movieAdapter = new MovieAdapter(getActivity(), movieList);
            coverImage.setAdapter(movieAdapter);
            progressDialog.hide();
        }
    }

    private ArrayList<MovieModel> getMovieList(String movieList) throws JSONException {

        final String TMDB_RESULTS = "results";
        final String TMDB_TITLE = "title";
        final String TMDB_ID = "id";
        final String TMDB_IMAGE = "poster_path";
        final String TMDB_ALT_IMAGE = "backdrop_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_VOTE = "vote_average";
        final String TMDB_RELEASE = "release_date";

        ArrayList<MovieModel> movies = new ArrayList<>();

        JSONObject movieListJson = new JSONObject(movieList);
        JSONArray movieArray = movieListJson.getJSONArray(TMDB_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJson = movieArray.getJSONObject(i);
            MovieModel movie = new MovieModel();
            movie.setId(movieJson.getInt(TMDB_ID));
            movie.setTitle(movieJson.getString(TMDB_TITLE));
            movie.setImage(movieJson.getString(TMDB_IMAGE));
            movie.setOverview(movieJson.getString(TMDB_OVERVIEW));
            movie.setRate(movieJson.getDouble(TMDB_VOTE));
            movie.setReleaseDate(movieJson.getString(TMDB_RELEASE));
            movie.setAltImage(movieJson.getString(TMDB_ALT_IMAGE));

            movies.add(movie);
        }
        return movies;
    }

    private void updateMovie() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_most_popular));
        if (NetworkUtils.isConnected(getActivity())) {
            new FetchMovieList().execute(sort_by);
        } else {
            Toast.makeText(getActivity(), "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }
}
