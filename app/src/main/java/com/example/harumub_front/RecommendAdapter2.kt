package com.example.harumub_front

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecommendAdapter2: RecyclerView.Adapter<RecommendAdapter2.ViewHolder>() {

    private var movie_images = intArrayOf(R.drawable.spider, R.drawable.gucci, R.drawable.about, R.drawable.ic_launcher_foreground, R.drawable.spider, R.drawable.gucci, R.drawable.about)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter2.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_collection_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecommendAdapter2.ViewHolder, position: Int) {
        holder.movieImage.setImageResource(movie_images[position])
    }

    override fun getItemCount(): Int {
        return movie_images.size
    }

    inner class ViewHolder(movieCollectionView: View): RecyclerView.ViewHolder(movieCollectionView) {
        var movieImage: ImageButton

        init {
            movieImage = movieCollectionView.findViewById(R.id.movie_collection_image)

            movieCollectionView.setOnClickListener {
//                val position: Int = adapterPosition

//                Toast.makeText(movieCollectionView.context, "영화 선택", Toast.LENGTH_LONG).show()
                Log.d("영화 : ", "선택")
            }
        }
    }
}