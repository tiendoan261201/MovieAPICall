package com.example.retrofitapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitapi.adapter.MovieAdapter
import com.example.retrofitapi.api.APIService
import com.example.retrofitapi.api.ApiClient
import com.example.retrofitapi.databinding.ActivityMainBinding
import com.example.retrofitapi.response.MovieListResponse
import com.example.retrofitapi.response.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val movieAdapter by lazy { MovieAdapter() }
    private val api : APIService by lazy {
        ApiClient().getClient().create(APIService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarMovies.visibility= View.VISIBLE
            val callMovieApi = api.getPopularMovie(1)
            callMovieApi.enqueue(object : Callback<MovieListResponse>{
                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    prgBarMovies.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                            response.body()?.let { itBody ->
                                itBody.results.let { itData ->
                                    if (itData.isNotEmpty()) {
                                        movieAdapter.differ.submitList(itData)
                                        //Recycler
                                        rlMovies.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = movieAdapter
                                        }
                                    }
                                }
                            }
                        }
                        in 300..399 -> {
                            Log.d("Response Code", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("Response Code", " Server error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }

            })

        }
    }
}