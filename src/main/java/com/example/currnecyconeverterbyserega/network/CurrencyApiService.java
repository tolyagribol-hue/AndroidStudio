package com.example.currnecyconeverterbyserega.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyApiService {

    @GET("v6/latest/USD")
    Call<CurrencyResponse> getLatestRates();
}
