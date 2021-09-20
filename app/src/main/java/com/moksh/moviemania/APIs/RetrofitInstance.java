package com.moksh.moviemania.APIs;

import com.moksh.moviemania.Activities.Search;
import com.moksh.moviemania.MoviesDetails.MoviesDetails;
import com.moksh.moviemania.PopularMovies.MovieList;
import com.moksh.moviemania.SearchResult.SearchedResult;
import com.moksh.moviemania.TvDetails.TvShowDetails;
import com.moksh.moviemania.TvShows.PopularTvShows;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RetrofitInstance {
    private static final String URL = "https://api.themoviedb.org/";

    public static MoviesService moviesService = null;
    public static MoviesService getMoviesService()
    {
        if (moviesService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            moviesService = retrofit.create(MoviesService.class);
        }
        return  moviesService;
    }

    public interface MoviesService
    {
        @GET("3/movie/popular?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&page=1")
        Call<MovieList> getPopularMovieList();

        @GET("3/discover/movie?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=false&page=1&vote_count.gte=8000&vote_average.gte=8.0&with_watch_monetization_types=flatrate")
        Call<MovieList> getTopRatedMovies();

        @GET("3/tv/popular?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&page=1")
        Call<PopularTvShows>getPopularTvShows();

        @GET("3/discover/tv?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&vote_average.gte=8.0&vote_count.gte=2000&include_null_first_air_dates=false&with_watch_monetization_types=flatrate")
        Call<PopularTvShows>getTopRatedTvShoes();

        @GET("3/movie/{movie_key}?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US")
        Call<MoviesDetails> getMoviesDetails(@Path("movie_key") String movie_id);

        @GET("3/movie/{movie_key}/recommendations?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&page=1")
        Call<MovieList> getRecomendedMovies(@Path("movie_key") String movie_id);

        @GET("3/tv/{tv_key}?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US")
        Call<TvShowDetails> getTvShowsDetails(@Path("tv_key") String tv_id);

        @GET("3/tv/{tv_key}/recommendations?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&page=1")
        Call<PopularTvShows> getRecomendedTvShows(@Path("tv_key") String tv_key);

        @GET("3/search/multi")
        Call<SearchedResult> getSearchedResult(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query, @Query("page") int page, @Query("include_adult") Boolean adult);
//?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&query={query}&page=1&include_adult=true

        @GET("https://api.themoviedb.org/3/discover/movie?api_key=0a9a3315040e03cc51efab97258c9fbb&language=en-US&sort_by=popularity.desc&include_adult=true&include_video=false&page=1&primary_release_date.gte=2021-09-18&with_watch_monetization_types=flatrate")
        Call<MovieList> getUpcomingMovies();
    }


}
