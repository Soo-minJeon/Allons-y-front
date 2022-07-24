package com.example.harumub_front

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendAdapter6(var titles: ArrayList<String>, var posters: ArrayList<String>):
    RecyclerView.Adapter<RecommendAdapter6.ViewHolder>() {

    var defaultImage = R.drawable.default_poster

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter6.ViewHolder {
        // RecyclerView에 들어갈 아이템의 레이아웃 설정
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_layout, parent, false)
        return ViewHolder(v)
    }

    // 순서에 따라 배열에 데이터 삽입
    override fun onBindViewHolder(holder: RecommendAdapter6.ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + posters[position]) // 불러올 이미지 url
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
            .into(holder.movieImage) // 이미지를 넣을 뷰

        holder.movieTitle.text = titles[position]
    }

    override fun getItemCount(): Int {
        return titles.size // 영화 개수
    }

    inner class ViewHolder(movieView: View): RecyclerView.ViewHolder(movieView) {
        var movieImage: ImageView
        var movieTitle: TextView

        init {
            movieImage = movieView.findViewById(R.id.movie_image) // 영화 이미지 버튼
            movieTitle = movieView.findViewById(R.id.movie_title) // 영화 제목
        }
    }
}