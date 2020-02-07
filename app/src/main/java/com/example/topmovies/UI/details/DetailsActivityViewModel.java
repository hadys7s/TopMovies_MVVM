package com.example.topmovies.UI.details;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.Model.RateResponse;
import com.example.topmovies.Model.SessionId;
import com.example.topmovies.data.network.ApiClient;
import com.example.topmovies.data.network.CastsResponseBody;
import com.example.topmovies.Model.TrailerModel;
import com.example.topmovies.data.network.TrailerResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivityViewModel extends ViewModel {
    MutableLiveData<List<CastModel>> CastList = new MutableLiveData<>();
    MutableLiveData<TrailerModel> videolist = new MutableLiveData<>();
    String videourl;
    MutableLiveData<String> sessionId=new MutableLiveData<>();
    MutableLiveData<Throwable> error = new MutableLiveData<>();
    MutableLiveData<RateResponse> rate = new MutableLiveData<>();


    public void getcastlist(Long MovieId) {
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

    public void getSessionId()
    {
        Call<SessionId> sessionIdCall=ApiClient.getInstance().getSessionId();
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

    public void postrate(Long movieId ,String SessionId, String rateValue)
    {    Call<RateResponse> popularMovies = ApiClient.getInstance().postRate(movieId,SessionId,rateValue);
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
    public void getTrailerUrl(Long MovieId) {

        Call<TrailerResponseBody> video = ApiClient.getInstance().getTrailer(MovieId);
        video.enqueue(new Callback<TrailerResponseBody>() {
            @Override
            public void onResponse(Call<TrailerResponseBody> call, Response<TrailerResponseBody> response) {
                try {
                    if (!response.body().getResults().isEmpty())
                    videolist.postValue(response.body().getResults().get(0));
                    else {

                        videolist.postValue(null);
                    }

                } catch (IndexOutOfBoundsException e)
                {

                    return;
                }




                }

            @Override
            public void onFailure(Call<TrailerResponseBody> call, Throwable t) {



                Log.v("casue=", String.valueOf(t.getCause()));

            }
        });


    }

}
