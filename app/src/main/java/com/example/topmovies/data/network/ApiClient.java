package com.example.topmovies.data.network;

import com.example.topmovies.Model.RateResponse;
import com.example.topmovies.Model.SessionId;
import com.example.topmovies.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    static ApiClient instance = null;
    ApiInterface apiInterface;

    //singleton to take only one instance
    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;

    }


    // here we build our call
    public ApiClient() {
        //for debug the response
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //to add api key to every request
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url()
                            .newBuilder()
                            .addQueryParameter(Constants.API_KEY_NAME, Constants.API_KEY_VALUE)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);

                })
                .addInterceptor(logging).build();


        // here we build the call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);


    }

    public Call<MoviesResponseBody> getPopularMovies() {
        return apiInterface.getTrendingMovies();

    }

    public Call<MoviesResponseBody> getTopRatedMovies() {
        return apiInterface.getTopRatedMovies();

    }

    public Call<MoviesResponseBody> getSearchResults(String query) {
        return apiInterface.getSearchResults(query);

    }

    public Call<CastsResponseBody> getCast(Long movieId) {
        return apiInterface.getCast(movieId);

    }

    public Call<TrailerResponseBody> getTrailer(Long movieId) {

        return apiInterface.getVideo(movieId);
    }

    //get calories require to parameters we attach it into one hashmap
    public Call<MoviesResponseBody> getCategories(String catogeryId) {

        Map<String, String> data = new HashMap<>();
        //sort
        data.put("sort_by", "popularity.desc");
        //catogery id
        data.put("with_genres", catogeryId);
        return apiInterface.getCatogries(data);


    }

    public Call<SessionId> getSessionId() {
        return apiInterface.getSessionId();

    }

    public Call<RateResponse> postRate(Long movieId, String sessionId, String rateValue) {

        return apiInterface.postRate(movieId, sessionId, rateValue);

    }


}
