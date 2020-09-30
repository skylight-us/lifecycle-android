package com.github.slondy.lifestyleapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface MyAPI {

    @POST("/lifestyle/schedule/")
    Call<PostItem> post_posts(@Body PostItem post);
    //Call <PostItem> post_posts(@FieldMap HashMap<String, Object> param);
    @PATCH("/lifestyle/schedule/{pk}/")
    Call<PostItem> patch_posts(@Path("pk") int pk, @Body PostItem post);

    @DELETE("/lifestyle/schedule/{pk}/")
    Call<PostItem> delete_posts(@Path("pk") int pk);

    @GET("/lifestyle/schedule/")
    Call<List<PostItem>> get_posts();

    @GET("/lifestyle/schedule/{pk}/")
    Call<PostItem> get_post_pk(@Path("pk") int pk);
}