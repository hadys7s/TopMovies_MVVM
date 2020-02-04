package com.example.topmovies.data.network;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.topmovies.utils.Constants;

import java.util.HashMap;
import java.util.Map;


public class ApiClient {
    static ApiClient instance = null;
    ApiInterface apiInterface;


    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;

    }


    public ApiClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                            Request request = chain.request();
                            HttpUrl url = request.url()
                                    .newBuilder()
                                    .addQueryParameter(Constants.API_KEY_NAME,   Constants.API_KEY_VALUE)
                                    .build();
                            request = request.newBuilder().url(url).build();
                            return chain.proceed(request);

                        })
                .addInterceptor(logging).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);


    }


    public Call<MoviesResponseBody> getPopularMovies() {
        return apiInterface.getPopularMovies();

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
        public  Call<TrailerResponseBody> getTrailer(Long movieId)
    {

        return apiInterface.getVideo(movieId);
    }
    public Call<MoviesResponseBody> getCatogeries(String catogeryId)
    {
        Map<String, String> data = new HashMap<>();
        data.put("sort_by", "popularity.desc");
        data.put("with_genres",catogeryId);
        return apiInterface.getCatogries(data);


    }


}
