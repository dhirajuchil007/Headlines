package com.example.headlines;

import com.example.headlines.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi  {
    @GET("top-headlines")
    Call<Headlines> getPosts(@Query("country") String country,@Query("apiKey")String apikey);
}
