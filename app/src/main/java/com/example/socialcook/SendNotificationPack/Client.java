package com.example.socialcook.SendNotificationPack;
// use to get connect with firebase could messaging and initialize the object of APIService
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit=null;
    public static Retrofit getClient(String url)
    {
        if(retrofit==null)
        {
            retrofit=new
                    Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
