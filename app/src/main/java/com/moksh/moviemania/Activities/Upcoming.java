package com.moksh.moviemania.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moksh.moviemania.APIs.RetrofitInstance;
import com.moksh.moviemania.PopularMovies.MovieList;
import com.moksh.moviemania.R;
import com.moksh.moviemania.SearchResult.SearchedResult;
import com.moksh.moviemania.SearchResult.SearchedResultAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Upcoming extends AppCompatActivity {
    LinearLayout toolbar;
    RecyclerView upcomingMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        //        Bottom navigation code STARTS here
//        Initialissing And Assign Variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set the Text Selected
        bottomNavigationView.setSelectedItemId(R.id.upcoming);

//        performing item click
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(),Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.upcoming:
                        return true;
                }
                return false;
            }
        });
        //        adding custom toolbar
        toolbar = findViewById(R.id.upcoming_toolbar);
        View toolbarView = getLayoutInflater().inflate(R.layout.custom_toolbar,null);
        ImageView backButton= toolbarView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upcoming.super.onBackPressed();
            }
        });
        TextView textView = toolbarView.findViewById(R.id.movie_name_toolbar);
        textView.setText("Upcoming Movies");
        toolbar.addView(toolbarView);

//          calling api
        upcomingMovies = findViewById(R.id.upcoming_movies_toolbar);
        upcomingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Call<MovieList> movieListCall = RetrofitInstance.getMoviesService().getUpcomingMovies();
        movieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
            MovieList movieList = response.body();
            upComingAdapter adapter = new upComingAdapter(getBaseContext(),movieList.getResults());
            upcomingMovies.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });
    }
}