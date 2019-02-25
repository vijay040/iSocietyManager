package com.mmcs.societymaintainance.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Singleton {
    private static String baseUrL = "";
    private static String devURL="https://incezo.com/api/";

    private RetrofitApi api;

    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    public RetrofitApi getApi() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(devURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                /* .addConverterFactory(GsonConverterFactory.create(gson))*/
                .build();
        return retrofit.create(RetrofitApi.class);
    }


}
