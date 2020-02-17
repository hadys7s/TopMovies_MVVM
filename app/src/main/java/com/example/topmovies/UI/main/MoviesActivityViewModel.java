package com.example.topmovies.UI.main;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.topmovies.Model.MoviesModel;
import com.example.topmovies.data.db.MoviesDb;
import com.example.topmovies.data.network.ApiClient;
import com.example.topmovies.data.network.MoviesResponseBody;
import com.example.topmovies.utils.Utils;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivityViewModel extends AndroidViewModel {
    //list for main movies home
    MutableLiveData<List<MoviesModel>> catogiresMoviesList = new MutableLiveData<>();
    // list for search results
    //first call list
    MutableLiveData<List<MoviesModel>> moviesList = new MutableLiveData<>();
    MutableLiveData<List<MoviesModel>> moviesSearchedList = new MutableLiveData<>();
    //list for cached movies
    MutableLiveData<List<MoviesModel>> moviesCashedList = new MutableLiveData<>();
    // list for load more movies when scrolling scrolling
    MutableLiveData<List<MoviesModel>> loadmore = new MutableLiveData<>();
    // live data to observe any error
    MutableLiveData<Throwable> error = new MutableLiveData<>();
    MutableLiveData<String> noFavorites = new MutableLiveData<>();
    MutableLiveData<List<MoviesModel>> favoritesList = new MutableLiveData<>();

    boolean check = false;



    public MoviesActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // build the data base
    MoviesDb newsDB = Room.databaseBuilder(getApplication(), MoviesDb.class, MoviesDb.DATABASE_NAME).allowMainThreadQueries().build();

    public int getFavValue(Long id) {
        return newsDB.moviesDao().getFavValue(id);
    }

    public void getFavouritesMoviesList() {

        if (newsDB.moviesDao().getFavouriteMoviesList()==null||newsDB.moviesDao().getFavouriteMoviesList().isEmpty())
        {
            noFavorites.setValue("No favourites");


        }
        else
        {
            favoritesList.setValue(newsDB.moviesDao().getFavouriteMoviesList());

        }
    }


    public void updateFavouriteValue(long id, int favValue) {
        newsDB.moviesDao().updateFavourite(favValue, id);
    }

    public List<MoviesModel> getCashedMoviesList() {
       return newsDB.moviesDao().getCashedMoviesList();

    }

    private void deleteCashedMoviesList() {

        newsDB.moviesDao().deleteCachedMovies();
    }

    private void cashMoviesList(List<MoviesModel> cashedlist) {
        newsDB.moviesDao().addMoviesList(cashedlist);
    }

    void addfavouriteMovie(MoviesModel moviesFav) {

        newsDB.moviesDao().addFavouriteMovie(moviesFav);
    }




    //call for getting catogery like action,romance etc....
    public void getCategoriesList(String catogeryId,int page) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getCategories(catogeryId,page);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                catogiresMoviesList.setValue(response.body().getResults());
                Log.v("Url", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                error.setValue(t);
            }
        });

    }

    // Call for Top Movies (Home)
    public void getTopMoviesList(int page) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getPopularRatedMovies(page);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                if (Utils.isInternetAvailable(getApplication())) {
                    if (!check) {
                        deleteCashedMoviesList();
                        cashMoviesList(response.body().getResults());
                        check=true;
                        moviesList.setValue(response.body().getResults());
                    }
                    loadmore.setValue(response.body().getResults());
                    Log.v("Url", call.request().url().toString());

                }
                else
                {
                    moviesCashedList.setValue(getCashedMoviesList());
                }

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                moviesCashedList.setValue(getCashedMoviesList());
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
