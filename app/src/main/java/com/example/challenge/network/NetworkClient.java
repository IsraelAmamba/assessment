package com.example.challenge.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkClient {

    private static NetworkClient networkClient;
    private OkHttpClient okHttpClient;

    //make this constructor private so that it cannot be instantiated outside this class
    private  NetworkClient(){
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //retry the connection on certains instances
                .build();
    }

    //provide a single interface to instantiate this class
    public  static NetworkClient getInstance(){
        if (networkClient == null){
            networkClient = new NetworkClient();
        }

        return networkClient;
    }

    //provide just one client with which to make http calls
    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

}
