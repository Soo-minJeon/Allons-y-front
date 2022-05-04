package com.example.harumub_front

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL

class RecommendAdapter3(var titles: ArrayList<String>, var posters: ArrayList<String>):
    RecyclerView.Adapter<RecommendAdapter3.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter3.ViewHolder {
        // RecyclerView에 들어갈 아이템의 레이아웃 설정
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_layout, parent, false)
        return ViewHolder(v)
    }

    // 순서에 따라 배열에 데이터 삽입
    override fun onBindViewHolder(holder: RecommendAdapter3.ViewHolder, position: Int) {
        var image_task: URLtoBitmapTask = URLtoBitmapTask().apply {
//            url = URL("https://image.tmdb.org/t/p/w500" + posters[position])
            url = URL("https://image.tmdb.org/t/p/w500" + "/xoqr4dMbRJnzuhsWDF3XNHQwJ9x.jpg")
        }

        var bitmap: Bitmap = image_task.execute().get()
        holder.movieImage.setImageBitmap(bitmap)

        holder.movieTitle.text = titles[position]
    }

    override fun getItemCount(): Int {
        return titles.size // 영화 개수
    }

    inner class ViewHolder(movieView: View): RecyclerView.ViewHolder(movieView) {
        var movieImage: ImageButton
        var movieTitle: TextView

        init {
            movieImage = movieView.findViewById(R.id.movie_image) // 영화 이미지 버튼
            movieTitle = movieView.findViewById(R.id.movie_title) // 영화 제목
        }
    }
}