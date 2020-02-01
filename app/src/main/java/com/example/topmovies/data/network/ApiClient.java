package com.example.topmovies.data.network;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    final static String BASE_URL = "https://api.themoviedb.org/3/";
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
                                    .addQueryParameter("api_key", "4d7840571eb6d9224d4413de05dbf03a")
                                    .build();
                            request = request.newBuilder().url(url).build();
                            return chain.proceed(request);

                        })
                .addInterceptor(logging).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);


    }


    public Call<MoviesResponseBody> getPopularMovies() {
        return apiInterface.getPopularMovies();

    } public Call<MoviesResponseBody> getSearchResults(String query) {
        return apiInterface.getSearchResults(query);

    }

    public Call<CastsResponseBody> getCast(Long movieId) {
        return apiInterface.getCast(movieId);

    }
        public  Call<TrailerResponseBody> getTrailer(Long movieId)
    {

        return apiInterface.getVideo(movieId);
    }


}
