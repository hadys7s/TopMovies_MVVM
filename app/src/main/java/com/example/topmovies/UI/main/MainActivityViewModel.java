package com.example.topmovies.UI.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.data.network.ApiClient;
import com.example.topmovies.data.network.MoviesResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivityViewModel extends ViewModel {
    MutableLiveData<List<MoviesModel>> moviesList = new MutableLiveData<>();

    public void getMoviesList() {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getPopularMovies();
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                moviesList.setValue(response.body().getResults());
                Log.v("Ur[l=", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {

                Log.v(TAG, t.getCause().toString());


            }
        });

    }
    public void getSearchResults(String query) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getSearchResults(query);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                moviesList.setValue(response.body().getResults());
                Log.v("Ur[l=", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {


                Log.v(TAG, t.getCause().toString());


            }
        });

    }


}
