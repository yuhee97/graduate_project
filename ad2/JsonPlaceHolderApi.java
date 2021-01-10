package com.example.ad2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("movies")
    Call<List<GetMovieItem>> getMovieItem();

    @FormUrlEncoded
    @POST("/similarity/")
    Call<List<Post>> postSimilarity(
            @Field("number") String number
    );

    @GET("/rank/")
    Call<List<GetRankItem>> getRankItem();

    @FormUrlEncoded
    @POST("/recommend/")
    Call<List<GetRecommendItem>> getRecommendItem(@Field("isbnlist") ArrayList<String> isbnlist);

    @FormUrlEncoded
    @POST("/info/")
    Call<List<GetBookInfo>> getBookInfo(@Field("isbn") String isbn);


}
