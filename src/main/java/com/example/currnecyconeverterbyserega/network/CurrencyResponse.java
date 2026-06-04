package com.example.currnecyconeverterbyserega.network;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CurrencyResponse {

    @SerializedName("result")
    private String result;

    @SerializedName("base_code")
    private String baseCode;

    @SerializedName("time_last_update_unix")
    private long timeLastUpdateUnix;

    @SerializedName("rates")
    private Map<String, Double> rates;

    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public long getTimeLastUpdateUnix() {
        return timeLastUpdateUnix;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}
