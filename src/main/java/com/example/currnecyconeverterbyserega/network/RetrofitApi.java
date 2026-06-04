package com.example.currnecyconeverterbyserega.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    private static final String BASE_URL = "https://open.er-api.com/";
    private static final int TIMEOUT_SECONDS = 15;

    private static volatile RetrofitApi instance;

    private final CurrencyApiService apiService;

    private RetrofitApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(CurrencyApiService.class);
    }

    public static RetrofitApi getInstance() {
        if (instance == null) {
            synchronized (RetrofitApi.class) {
                if (instance == null) {
                    instance = new RetrofitApi();
                }
            }
        }
        return instance;
    }

    public CurrencyApiService getApiService() {
        return apiService;
    }
}
