package com.example.topmovies.UI.details;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.RateResponse;
import com.example.topmovies.Model.SessionId;
import com.example.topmovies.Model.TrailerModel;
import com.example.topmovies.data.network.ApiClient;
import com.example.topmovies.data.network.CastsResponseBody;
import com.example.topmovies.data.network.TrailerResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivityViewModel extends ViewModel {
    // list for cast
    MutableLiveData<List<CastModel>> CastList = new MutableLiveData<>();
    // list for trailers
    MutableLiveData<TrailerModel> videoList = new MutableLiveData<>();
    // live data for session id
    MutableLiveData<String> sessionId = new MutableLiveData<>();
    // live data for error
    MutableLiveData<Throwable> error = new MutableLiveData<>();
    // live data for rate the movie
    MutableLiveData<RateResponse> rate = new MutableLiveData<>();


    // Call for cast list
    public void getCastlist(Long MovieId) {
        Call<CastsResponseBody> cast = ApiClient.getInstance().getCast(MovieId);
        cast.enqueue(new Callback<CastsResponseBody>() {
            @Override
            public void onResponse(Call<CastsResponseBody> call, Response<CastsResponseBody> response) {
                CastList.postValue(response.body().getCast());
                Log.v("Url=", call.request().url().toString());
            }

            @Override
            public void onFailure(Call<CastsResponseBody> call, Throwable t) {


            }
        });


    }

    // call to get session id so the user can access the rate
    public void getSessionId() {
        Call<SessionId> sessionIdCall = ApiClient.getInstance().getSessionId();
        sessionIdCall.enqueue(new Callback<SessionId>() {
            @Override
            public void onResponse(Call<SessionId> call, Response<SessionId> response) {
                sessionId.setValue(response.body().getGuestSessionId());
            }

            @Override
            public void onFailure(Call<SessionId> call, Throwable t) {

            }
        });
    }

    //call for post the rate
    public void postRate(Long movieId, String SessionId, String rateValue) {
        Call<RateResponse> popularMovies = ApiClient.getInstance().postRate(movieId, SessionId, rateValue);
        popularMovies.enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                rate.setValue(response.body());
                Log.v("Url", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                error.setValue(t);


            }
        });

    }

    // call to get video key for youtube Api
    public void getTrailerUrl(Long MovieId) {

        Call<TrailerResponseBody> video = ApiClient.getInstance().getTrailer(MovieId);
        video.enqueue(new Callback<TrailerResponseBody>() {
            @Override
            public void onResponse(Call<TrailerResponseBody> call, Response<TrailerResponseBody> response) {
                try {
                    if (!response.body().getResults().isEmpty())
                        videoList.postValue(response.body().getResults().get(0));
                    else {

                        videoList.postValue(null);
                    }

                } catch (IndexOutOfBoundsException e) {

                    return;
                }


            }

            @Override
            public void onFailure(Call<TrailerResponseBody> call, Throwable t) {


                Log.v("cause = ", String.valueOf(t.getCause()));

            }
        });


    }

}
