package com.moksh.moviemania.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moksh.moviemania.APIs.RetrofitInstance;
import com.moksh.moviemania.Misclenious.GenereAdapter;
import com.moksh.moviemania.Misclenious.GenreAdapterTv;
import com.moksh.moviemania.R;
import com.moksh.moviemania.TvDetails.Genre;
import com.moksh.moviemania.TvDetails.Season;
import com.moksh.moviemania.TvDetails.TvShowDetails;
import com.moksh.moviemania.TvShows.PopularTvShows;
import com.moksh.moviemania.TvShows.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowsDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner;
    LinearLayout customToolbar;
    ImageView movieImage,movieBackdrop;
    TextView movieName,time,rating,overview,watchOn1,episodes;
    RecyclerView genereRecyclerView,recomendedMovies;
    String id = "";
    List<Season> seasons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_details);

        //        setting our views
        watchOn1 = findViewById(R.id.watch_on_1);
        movieImage = findViewById(R.id.movie_image);
        movieBackdrop = findViewById(R.id.tv_backdrop);
        movieName = findViewById(R.id.movie_name);
        time = findViewById(R.id.time);
        rating = findViewById(R.id.rating);
        overview = findViewById(R.id.overview);
        episodes = findViewById(R.id.episodes);
        spinner = findViewById(R.id.spinnerTextSize);
        genereRecyclerView = findViewById(R.id.genere_recyclerView);
        genereRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));

        //        adding custom toolbar
        customToolbar = findViewById(R.id.toolbar);
        View toolbarView = getLayoutInflater().inflate(R.layout.custom_toolbar,null);
        ImageView backButton= toolbarView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TvShowsDetails.super.onBackPressed();
            }
        });
        TextView textView = toolbarView.findViewById(R.id.movie_name_toolbar);
        customToolbar.addView(toolbarView);

        id = getIntent().getStringExtra("movie_id");




//        calling apis
        Call<TvShowDetails> tvShowDetailsCall = RetrofitInstance.getMoviesService().getTvShowsDetails(id);
        tvShowDetailsCall.enqueue(new Callback<TvShowDetails>() {
            @Override
            public void onResponse(Call<TvShowDetails> call, Response<TvShowDetails> response) {
                try{
                    TvShowDetails tvShowDetails = response.body();
                    seasons = tvShowDetails.getSeasons();
                    movieName.setText(tvShowDetails.getName());
                    time.setText(tvShowDetails.getFirstAirDate().substring(0,4));
                    rating.setText(tvShowDetails.getVoteAverage().toString().substring(0,3));
                    textView.setText(tvShowDetails.getName());
                    overview.setText(tvShowDetails.getOverview());
                    String backdropImageUrl = "https://image.tmdb.org/t/p/w500/" + tvShowDetails.getBackdropPath();
                    String movieImageUrl = "https://image.tmdb.org/t/p/w500/" + tvShowDetails.getPosterPath();
                    Glide.with(getBaseContext())
                            .load(backdropImageUrl)
                            .centerCrop()
                            .into(movieBackdrop);

                    Glide.with(getBaseContext())
                            .load(movieImageUrl)
                            .centerCrop()
                            .into(movieImage);
                    List<Genre> genreList = tvShowDetails.getGenres();
                    GenreAdapterTv Genreadapter = new GenreAdapterTv(getBaseContext(),genreList);
                    genereRecyclerView.setAdapter(Genreadapter);
                    Genreadapter.notifyDataSetChanged();

                    //        spinner
                    int noOfSeason = tvShowDetails.getNumberOfSeasons();
                    String[] arr = new String[noOfSeason];
                    for (int i =1; i<=noOfSeason; i++)
                    {
                        arr[i-1] = "Season "+i;
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item,arr);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    Log.e("failure cause",e.toString());
                }
            }

            @Override
            public void onFailure(Call<TvShowDetails> call, Throwable t) {
                Toast.makeText(getBaseContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
        spinner.setOnItemSelectedListener(this);

//            Recomended shows details
        String link = "https://api.themoviedb.org/3/tv/"+id+"/watch/providers?api_key=0a9a3315040e03cc51efab97258c9fbb";
        backgroundThread task = new backgroundThread();
        task.execute(link);
        Call<PopularTvShows> popularTvShowsCall = RetrofitInstance.getMoviesService().getRecomendedTvShows(id);
        popularTvShowsCall.enqueue(new Callback<PopularTvShows>() {
            @Override
            public void onResponse(Call<PopularTvShows> call, Response<PopularTvShows> response) {
                recomendedMovies =findViewById(R.id.RecomndedMovieRecyclerView);
                recomendedMovies.setLayoutManager(new LinearLayoutManager(getBaseContext(),RecyclerView.HORIZONTAL,false));
                List<Result> results = response.body().getResults();
                TvShowsAdapter adapter = new TvShowsAdapter(getBaseContext(),results);
                recomendedMovies.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularTvShows> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long longnumber) {
        if(parent.getId() == R.id.spinnerTextSize){
            if(seasons.get(position).getSeasonNumber() > 0)
            {
                episodes.setText("Episodes: "+seasons.get(position).getEpisodeCount().toString());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            }
        }
    }
}