package com.moksh.moviemania.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moksh.moviemania.APIs.RetrofitInstance;
import com.moksh.moviemania.PopularMovies.MovieList;
import com.moksh.moviemania.PopularMovies.Result;
import com.moksh.moviemania.R;
import com.moksh.moviemania.TvShows.PopularTvShows;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    RecyclerView popularMovie,topRatedMovies,popularTvShows,topRatedTvShows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Bottom navigation code STARTS here
//        Initialissing And Assign Variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set the Text Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

//        performing item click
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.upcoming:
                        startActivity(new Intent(getApplicationContext(),Upcoming.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

//        bottom navigation ends here

//       PopularMovieThread popularMovieThread = new PopularMovieThread();
//       TopRatedMovieThread topRatedMovieThread = new TopRatedMovieThread();
//       PopularTvShowsThread popularTvShowsThread = new PopularTvShowsThread();
//       TopRatedTvShowsThread topRatedTvShowsThread = new TopRatedTvShowsThread();
//       popularMovieThread.start();
//       topRatedMovieThread.start();
//       popularTvShowsThread.start();
//       topRatedTvShowsThread.start();
        //        intialising popular movies recycler view
        popularMovie = findViewById(R.id.popular_movies_recyclerView);
        popularMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));
        Call<MovieList> movieListCall = RetrofitInstance.getMoviesService().getPopularMovieList();
        movieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                MoviesAdapter adapter = new MoviesAdapter(getBaseContext(),movieList.getResults());
                popularMovie.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i("error",t.toString());
            }
        });

        //          Initialising top Rated movies Recycler View
        topRatedMovies = findViewById(R.id.top_rated_movies_recyclerView);
        topRatedMovies.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));

//        calling our api
        Call<MovieList> topRatedMovieList = RetrofitInstance.getMoviesService().getTopRatedMovies();
        topRatedMovieList.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                MoviesAdapter adapter = new MoviesAdapter(getBaseContext(),movieList.getResults());
                topRatedMovies.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i("error",t.toString());
            }
        });

        //        Initialising popular Tv shows
        popularTvShows = findViewById(R.id.popular_tvShows_recyclerView);
        popularTvShows.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));

//        calling our api
        Call<PopularTvShows> popularTvShowsCall = RetrofitInstance.moviesService.getPopularTvShows();
        popularTvShowsCall.enqueue(new Callback<PopularTvShows>() {
            @Override
            public void onResponse(Call<PopularTvShows> call, Response<PopularTvShows> response) {
                PopularTvShows popularTvShowsObject = response.body();
                TvShowsAdapter adapter = new TvShowsAdapter(getBaseContext(),popularTvShowsObject.getResults());
                popularTvShows.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularTvShows> call, Throwable t) {

            }
        });

        //        Initialising popular Tv shows
        topRatedTvShows = findViewById(R.id.top_rated_tvShows_recyclerView);
        topRatedTvShows.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.HORIZONTAL,false));

//        calling our api
        Call<PopularTvShows> topRatedTvShowsCall = RetrofitInstance.moviesService.getTopRatedTvShoes();
        topRatedTvShowsCall.enqueue(new Callback<PopularTvShows>() {
            @Override
            public void onResponse(Call<PopularTvShows> call, Response<PopularTvShows> response) {
                PopularTvShows topRatedTvShowsObject = response.body();
                TvShowsAdapter adapter = new TvShowsAdapter(getBaseContext(),topRatedTvShowsObject.getResults());
                topRatedTvShows.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularTvShows> call, Throwable t) {

            }
        });
    }
}