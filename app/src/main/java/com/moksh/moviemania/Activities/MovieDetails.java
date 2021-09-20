package com.moksh.moviemania.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.moksh.moviemania.APIs.RetrofitInstance;
import com.moksh.moviemania.Misclenious.GenereAdapter;
import com.moksh.moviemania.MoviesDetails.Genre;
import com.moksh.moviemania.MoviesDetails.MoviesDetails;
import com.moksh.moviemania.PopularMovies.MovieList;
import com.moksh.moviemania.PopularMovies.Result;
import com.moksh.moviemania.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity{
    LinearLayout customToolbar;
    ImageView movieImage,movieBackdrop;
    TextView movieName,time,rating,overview,watchOn1;
    RecyclerView genereRecyclerView,recomendedMovies;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

//        adding custom toolbar
        customToolbar = findViewById(R.id.toolbar);
        View toolbarView = getLayoutInflater().inflate(R.layout.custom_toolbar,null);
        ImageView backButton= toolbarView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetails.super.onBackPressed();
            }
        });
        TextView textView = toolbarView.findViewById(R.id.movie_name_toolbar);
        customToolbar.addView(toolbarView);

//        setting our views
        watchOn1 = findViewById(R.id.watch_on_1);
        movieImage = findViewById(R.id.movie_image);
        movieBackdrop = findViewById(R.id.movie_backdrop);
        movieName = findViewById(R.id.movie_name);
        time = findViewById(R.id.time);
        rating = findViewById(R.id.rating);
        overview = findViewById(R.id.overview);
        genereRecyclerView = findViewById(R.id.genere_recyclerView);
        genereRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        id = getIntent().getStringExtra("movie_id");

//                getting watch provider manually
        String link = "https://api.themoviedb.org/3/movie/"+id+"/watch/providers?api_key=0a9a3315040e03cc51efab97258c9fbb";
        backgroundThread task = new backgroundThread();
        task.execute(link);
        Call<MoviesDetails> movieDetailsCall = RetrofitInstance.getMoviesService().getMoviesDetails(id);
        movieDetailsCall.enqueue(new Callback<MoviesDetails>() {
            @Override
            public void onResponse(Call<MoviesDetails> call, Response<MoviesDetails> response) {
                MoviesDetails movieDetails = response.body();
                String movieImageUrl = "https://image.tmdb.org/t/p/w500/"+movieDetails.getPosterPath();
                String movieBackdropUrl = "https://image.tmdb.org/t/p/w500/"+movieDetails.getBackdropPath();
                Glide.with(getBaseContext())
                        .load(movieImageUrl)
                        .into(movieImage);
                Glide.with(getBaseContext())
                        .load(movieBackdropUrl)
                        .into(movieBackdrop);
                textView.setText(movieDetails.getTitle());
                movieName.setText(movieDetails.getTitle());
                rating.setText(movieDetails.getVoteAverage().toString());
                time.setText(movieDetails.getReleaseDate().substring(0,4)+"  "+movieDetails.getRuntime()+"m");
                overview.setText(movieDetails.getOverview());
                List<Genre> genreList = movieDetails.getGenres();
                GenereAdapter adapter = new GenereAdapter(getBaseContext(),genreList);
                genereRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MoviesDetails> call, Throwable t) {

            }
        });

        recomendedMovies = findViewById(R.id.RecomndedMovieRecyclerView);
        recomendedMovies.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        Call<MovieList> recomendedCall = RetrofitInstance.getMoviesService().getRecomendedMovies(id);
        recomendedCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                List<Result>results = response.body().getResults();
                MoviesAdapter adapter = new MoviesAdapter(getBaseContext(),results);
                recomendedMovies.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });



    }

    class backgroundThread extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.connect();
                InputStream is = httpsURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int data = reader.read();
                while (data != -1)
                {
                    char ouput = (char) data;
                    result += ouput;
                    data = reader.read();
                }
            } catch (Exception e)
            {
                Log.i("error",e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                String sub = s.substring(4);
                JSONObject resultsObject = new JSONObject(sub);
                String results = resultsObject.getString("results");
                JSONObject arObject = new JSONObject(results);
                String ar = arObject.getString("AR");
                JSONObject flatrateObject = new JSONObject(ar);
                String flatrate = flatrateObject.getString("flatrate");
                JSONArray jsonArray = new JSONArray(flatrate);
                JSONObject watchObject = jsonArray.getJSONObject(0);
                String WatchProvider = watchObject.getString("provider_name");
                Log.i("watchProvider",WatchProvider);
                watchOn1.setText("Available on "+WatchProvider);
            } catch (Exception e) {
                Log.i("json error",e.toString());
                watchOn1.setText("Available in Theaters");

            }
        }
    }
}