package com.example.retrofitapi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.retrofitapi.MovieDetailesActivity
import com.example.retrofitapi.R
import com.example.retrofitapi.databinding.ItemMoviesBinding
import com.example.retrofitapi.response.MovieListResponse
import com.example.retrofitapi.response.Result
import com.example.retrofitapi.ulis.Constants.POSTER_BASEURL

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemMoviesBinding
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemMoviesBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result) {
            binding.apply {
                tvMovieName.text = item.title
                tvRate.text = item.vote_average.toString()
                val moviePosterURL = POSTER_BASEURL + item?.poster_path
                ImgMovie.load(moviePosterURL){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                tvLang.text=item.original_language
                tvMovieDateRelease.text = item.release_date

                root.setOnClickListener {
                    val intent = Intent(context, MovieDetailesActivity::class.java)
                    intent.putExtra("id", item?.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
}