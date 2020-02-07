package com.example.topmovies.data.network;

import com.example.topmovies.Model.RateResponse;
import com.example.topmovies.Model.SessionId;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    //get trending movies
    @GET("trending/movie/day")
    Call<MoviesResponseBody> getTrendingMovies();

    //get guest session id so the user can rate movies
    @GET("authentication/guest_session/new")
    Call<SessionId> getSessionId();

    //here we post user rate value
    @POST("movie/{movie_id}/rating")
    Call<RateResponse> postRate(@Path(value = "movie_id", encoded = true) Long movieId,
                                @Query("guest_session_id") String sessionId, @Query("value") String value);

    //here we call discover to get specific catogery
    @GET("discover/movie")
    Call<MoviesResponseBody> getCatogries(@QueryMap Map<String, String> quires);
    //get top rated movies

    @GET("movie/top_rated")
    Call<MoviesResponseBody> getTopRatedMovies();

    //here we call for search
    @GET("search/movie")
    Call<MoviesResponseBody> getSearchResults(@Query("query") String query);

    //here we call for movie cast
    @GET("movie/{movie_id}/credits")
    Call<CastsResponseBody> getCast(@Path(value = "movie_id", encoded = true) Long movieId);

    //here we call for movie tutorial
    @GET("movie/{video_id}/videos")
    Call<TrailerResponseBody> getVideo(@Path(value = "video_id", encoded = true) Long movieId);

}
