package com.example.topmovies.UI.main;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.MoviesModel;

public interface OnItemClicked {
    //on movie item click to take user to details activity
    void onListItemCLicked(MoviesModel moviesModel);
}
