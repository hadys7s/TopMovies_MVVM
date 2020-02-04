package com.example.topmovies.UI.Adapters;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.MoviesModel;

public interface OnItemClicked {
    void onListItemCLicked(MoviesModel moviesModel);
    void onlistitemclickedcast(CastModel moviesModel);
}
