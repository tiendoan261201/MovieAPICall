package com.example.retrofitapi.api

import com.example.retrofitapi.response.MovieDetails
import com.example.retrofitapi.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page")page:Int): Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Call<MovieDetails>

}