package com.example.topmovies.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("trending/movie/day")
    Call<MoviesResponseBody> getPopularMovies();
    @GET("search/movie")
    Call<MoviesResponseBody> getSearchResults(@Query("query") String query);
   @GET("movie/{movie_id}/credits")
    Call<CastsResponseBody> getCast (@Path(value = "movie_id",encoded = true) Long movieId);
   @GET("movie/{video_id}/videos")
    Call<TrailerResponseBody> getVideo (@Path(value = "video_id",encoded = true) Long movieId);

}
