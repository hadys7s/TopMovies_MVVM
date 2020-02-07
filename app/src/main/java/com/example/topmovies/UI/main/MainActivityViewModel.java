package com.example.topmovies.UI.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.data.db.MoviesDb;
import com.example.topmovies.data.network.ApiClient;
import com.example.topmovies.data.network.MoviesResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    //list for main movies home
    MutableLiveData<List<MoviesModel>> moviesList = new MutableLiveData<>();
    // list for search results
    MutableLiveData<List<MoviesModel>> moviesSearchedList = new MutableLiveData<>();
    //list for cached movies
    MutableLiveData<List<MoviesModel>> moviesCashedList = new MutableLiveData<>();
    // list for load more movies when scrolling scrolling
    MutableLiveData<List<MoviesModel>> loadmore = new MutableLiveData<>();
    // live data to observe any error
    MutableLiveData<Throwable> error = new MutableLiveData<>();


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // build the data base
    MoviesDb newsDB = Room.databaseBuilder(getApplication(), MoviesDb.class, MoviesDb.DATABASE_NAME).allowMainThreadQueries().build();

    public int getFavValue(Long id) {
        return newsDB.moviesDao().getFavValue(id);
    }

    public List<MoviesModel> getFavouritesMoviesList() {
        return newsDB.moviesDao().getFavouriteMoviesList();
    }


    public void updateFavouriteValue(long id, int favValue) {
        newsDB.moviesDao().updateFavourite(favValue, id);


    }

    public void getCashedMoviesList() {
        moviesCashedList.setValue(newsDB.moviesDao().getCashedMoviesList());

    }

    public void deleteCashedMoviesList() {

        newsDB.moviesDao().deleteCachedMovies();
    }

    public void cashMoviesList(List<MoviesModel> cashedlist) {
        newsDB.moviesDao().addMoviesList(cashedlist);
    }

    void addfavouriteMovie(MoviesModel moviesFav) {

        newsDB.moviesDao().addFavouriteMovie(moviesFav);
    }

    // the call for the main movies in home
    public void getMoviesList() {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getPopularMovies();
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                moviesList.setValue(response.body().getResults());
                Log.v("Url", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                error.setValue(t);

            }
        });

    }

    //call for getting catogery like action,romance etc....
    public void getCategoriesList(String catogeryId) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getCategories(catogeryId);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                moviesList.setValue(response.body().getResults());
                Log.v("Url", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                error.setValue(t);


            }
        });

    }

    // Call for Top Movies (Home)
    public void getTopMoviesList() {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getTopRatedMovies();
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                loadmore.setValue(response.body().getResults());
                Log.v("Url", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                error.setValue(t);


            }
        });

    }

    // Call for get search results
    public void getSearchResults(String query) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getSearchResults(query);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                moviesCashedList.setValue(response.body().getResults());
                Log.v("Url2", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {


                error.setValue(t);


            }
        });

    }


}
