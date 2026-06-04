package com.example.currnecyconeverterbyserega.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.currnecyconeverterbyserega.network.CurrencyResponse;
import com.example.currnecyconeverterbyserega.network.RetrofitApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRepository {

    private static final String PREF_NAME = "currency_converter_prefs";
    private static final String KEY_RATES_JSON = "rates_json";
    private static final String KEY_TIMESTAMP = "rates_timestamp";
    private static final long CACHE_DURATION_MS = 30 * 60 * 1000;

    private static CurrencyRepository instance;

    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private Map<String, Double> cachedRatesInMemory;
    private long lastFetchTimestamp;

    public interface RatesCallback {
        void onSuccess(Map<String, Double> rates, boolean offlineMode, long lastUpdatedTimeMs);

        void onFailure(Throwable t);
    }

    private CurrencyRepository(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadRatesFromPrefs();
    }

    public static synchronized CurrencyRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CurrencyRepository(context);
        }
        return instance;
    }

    private void loadRatesFromPrefs() {
        String json = sharedPreferences.getString(KEY_RATES_JSON, null);
        lastFetchTimestamp = sharedPreferences.getLong(KEY_TIMESTAMP, 0);
        if (json != null) {
            try {
                Type type = new TypeToken<Map<String, Double>>() {}.getType();
                cachedRatesInMemory = gson.fromJson(json, type);
            } catch (Exception e) {
                cachedRatesInMemory = null;
                lastFetchTimestamp = 0;
            }
        }
    }

    private void saveRatesToPrefs(Map<String, Double> rates, long timestamp) {
        cachedRatesInMemory = rates;
        lastFetchTimestamp = timestamp;
        sharedPreferences.edit()
                .putString(KEY_RATES_JSON, gson.toJson(rates))
                .putLong(KEY_TIMESTAMP, timestamp)
                .apply();
    }

    public void fetchRates(boolean forceRefresh, RatesCallback callback) {
        long currentMs = System.currentTimeMillis();
        boolean isCacheValid = cachedRatesInMemory != null
                && currentMs - lastFetchTimestamp < CACHE_DURATION_MS;

        if (!forceRefresh && isCacheValid) {
            callback.onSuccess(cachedRatesInMemory, false, lastFetchTimestamp);
            return;
        }

        RetrofitApi.getInstance().getApiService().getLatestRates().enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && "success".equals(response.body().getResult())) {
                    Map<String, Double> rates = response.body().getRates();
                    long timeMs = System.currentTimeMillis();
                    saveRatesToPrefs(rates, timeMs);
                    callback.onSuccess(rates, false, timeMs);
                } else {
                    handleNetworkFailure(new Exception("Некорректный ответ API"), callback);
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                handleNetworkFailure(t, callback);
            }
        });
    }

    private void handleNetworkFailure(Throwable t, RatesCallback callback) {
        if (cachedRatesInMemory != null) {
            callback.onSuccess(cachedRatesInMemory, true, lastFetchTimestamp);
        } else {
            callback.onFailure(t);
        }
    }

    public double convert(double amount, String fromCurrency, String toCurrency) {
        if (cachedRatesInMemory == null) {
            return 0;
        }
        Double fromRate = cachedRatesInMemory.get(fromCurrency);
        Double toRate = cachedRatesInMemory.get(toCurrency);
        if (fromRate == null || toRate == null || fromRate == 0) {
            return 0;
        }
        return amount * (toRate / fromRate);
    }

    public Map<String, Double> getCachedRatesInMemory() {
        return cachedRatesInMemory;
    }

    public long getLastFetchTimestamp() {
        return lastFetchTimestamp;
    }
}
