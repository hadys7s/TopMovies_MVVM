package com.example.topmovies.UI.details;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.topmovies.Model.CastModel;
import com.example.topmovies.data.network.ApiClient;
import com.example.topmovies.data.network.CastsResponseBody;
import com.example.topmovies.Model.TrailerModel;
import com.example.topmovies.data.network.TrailerResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DetailsActivityViewModel extends ViewModel {
    MutableLiveData<List<CastModel>> CastList = new MutableLiveData<>();
    MutableLiveData<TrailerModel> videolist = new MutableLiveData<>();
    String videourl;

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
                Log.v(TAG, t.getCause().toString());


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
