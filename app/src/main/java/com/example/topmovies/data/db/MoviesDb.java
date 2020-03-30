package com.example.topmovies.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.topmovies.Model.MoviesModel;

@Database(entities = {MoviesModel.class}, version = 1, exportSchema = false)
public abstract class MoviesDb extends RoomDatabase {
    public static final String DATABASE_NAME = "moviesDb";
    public abstract Dao moviesDao();

}
