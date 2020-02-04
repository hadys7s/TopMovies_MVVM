package com.example.topmovies.data.network;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    @GET("trending/movie/day")
    Call<MoviesResponseBody> getPopularMovies();
  /*  */

    @GET("discover/movie")
    Call<MoviesResponseBody> getCatogries(@QueryMap Map<String,String> quires);

    @GET("movie/top_rated")
    Call<MoviesResponseBody> getTopRatedMovies();

    @GET("search/movie")
    Call<MoviesResponseBody> getSearchResults(@Query("query") String query);
   @GET("movie/{movie_id}/credits")
    Call<CastsResponseBody> getCast (@Path(value = "movie_id",encoded = true) Long movieId);
   @GET("movie/{video_id}/videos")
    Call<TrailerResponseBody> getVideo (@Path(value = "video_id",encoded = true) Long movieId);

}
