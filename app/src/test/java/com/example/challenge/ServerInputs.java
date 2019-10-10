package com.example.challenge;

import com.example.challenge.model.FortuneMessage;
import com.example.challenge.network.NetworkClient;
import com.google.gson.Gson;

import static org.junit.Assert.assertTrue;
import  org.junit.*;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ServerInputs {
    private  String serverResponse;

    @Test
    public  void checkServerResponseFormat(){
        NetworkClient client = NetworkClient.getInstance();
        OkHttpClient okHttpClient = client.getOkHttpClient();

        Request request = new Request.Builder()
                .url("http://api.acme.international/fortune")
                .build();
        try {
            Response response =  okHttpClient.newCall(request).execute();
            serverResponse = response.body().string();

            Gson gson = new Gson();
            FortuneMessage message = gson.fromJson(serverResponse, FortuneMessage.class);

            assertTrue(message instanceof FortuneMessage);
        }catch (Exception ex){

        }


    }
}
