package com.example.harumub_front

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.net.URL

class WatchListAdapter(var id: String, var titles: ArrayList<String>, var posters: ArrayList<String>):
    RecyclerView.Adapter<WatchListAdapter.ViewHolder>() {

    var defaultImage = R.drawable.spider

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchListAdapter.ViewHolder {
        // RecyclerView에 들어갈 아이템의 레이아웃 설정
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_layout, parent, false)
        return ViewHolder(v)
    }

    // 순서에 따라 배열에 데이터 삽입
    override fun onBindViewHolder(holder: WatchListAdapter.ViewHolder, position: Int) {
/*
        var image_task: URLtoBitmapTask = URLtoBitmapTask().apply {
//            url = URL("https://image.tmdb.org/t/p/w500" + posters[position])
            url = URL("https://image.tmdb.org/t/p/w500" + "/xoqr4dMbRJnzuhsWDF3XNHQwJ9x.jpg")
        }

        var bitmap: Bitmap = image_task.execute().get()
        holder.movieImage.setImageBitmap(bitmap)
*/
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView
        var movieTitle: TextView

        init {
            movieImage = itemView.findViewById(R.id.movie_image) // 영화 이미지 버튼
            movieTitle = itemView.findViewById(R.id.movie_title) // 영화 제목

            // 해당 아이템 클릭시 결과 페이지로 이동
            itemView.setOnClickListener {
                val position: Int = adapterPosition

                Toast.makeText(itemView.context, titles[position], Toast.LENGTH_LONG).show() // 영화 클릭 시 토스트 메시지

                var movie_title = titles[position]
                val intent = Intent(itemView.context, ResultActivity::class.java)
                intent.putExtra("user_id", id)
                intent.putExtra("movie_title", movie_title)
                itemView.context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}