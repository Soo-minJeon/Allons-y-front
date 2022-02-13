package com.example.harumub_front

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendAdapter1: RecyclerView.Adapter<RecommendAdapter1.ViewHolder>() {
    // 데이터
    private var movie_images = intArrayOf(R.drawable.spider, R.drawable.gucci, R.drawable.about, R.drawable.ic_launcher_foreground, R.drawable.spider, R.drawable.gucci, R.drawable.about)
    private var movie_titles = arrayOf("제목 1", "제목 2", "제목 3", "제목 4", "제목 5", "제목 6", "제목 7")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter1.ViewHolder {
        // 리사이클러뷰에 들어갈 아이템 레이아웃 설정
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_layout, parent, false)
        return ViewHolder(v)
    }
    
    // 순서에 따라 배열에 데이터 삽입
    override fun onBindViewHolder(holder: RecommendAdapter1.ViewHolder, position: Int) {
        holder.movieImage.setImageResource(movie_images[position])
        holder.movieTitle.text = movie_titles[position]
    }

    override fun getItemCount(): Int {
        return movie_images.size    // 영화의 개수
    }

    inner class ViewHolder(movieView: View): RecyclerView.ViewHolder(movieView) {
        var movieImage: ImageButton
        var movieTitle: TextView

        init {
            movieImage = movieView.findViewById(R.id.movie_image)
            movieTitle = movieView.findViewById(R.id.movie_title)
        }
    }
}