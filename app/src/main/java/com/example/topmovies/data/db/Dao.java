package com.example.topmovies.data.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.topmovies.Model.MoviesModel;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMoviesList(List<MoviesModel> moviesList);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavouriteMovie(MoviesModel moviesFav);

    //get cashed movies
    @Query("SELECT * FROM movies")
    List<MoviesModel> getCashedMoviesList();


    @Query("SELECT * FROM movies WHERE favorite= 1")
    List<MoviesModel> getFavouriteMoviesList();

    //get favourite value to check whether the user like the movie or not
    @Query("SELECT favorite FROM movies WHERE mId=:id")
    int getFavValue(Long id);

    //update fav value fwhen user like or dislike movie
    @Query("UPDATE movies SET favorite=:fav WHERE mId = :id ")
    void updateFavourite(int fav, Long id);

    // we remove the old cached movies except user favourites
    @Query("DELETE FROM movies WHERE favorite=0")
    void deleteCachedMovies();


}
