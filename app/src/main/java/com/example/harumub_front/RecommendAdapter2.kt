package com.example.harumub_front

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_search.view.*
import java.net.URL

class RecommendAdapter2(var userIdList: ArrayList<String>, var titlesList: ArrayList<ArrayList<String>>, var postersList: ArrayList<ArrayList<String>>):
    RecyclerView.Adapter<RecommendAdapter2.ViewHolder>() {

    lateinit var reco2_userId : String
    lateinit var reco2_titleList : ArrayList<String>
    lateinit var reco2_posterList : ArrayList<String>
    var defaultImage = R.drawable.default_poster

    // 데이터
    private var movie_images = intArrayOf(R.drawable.spider, R.drawable.gucci, R.drawable.about, R.drawable.ic_launcher_foreground, R.drawable.spider, R.drawable.gucci, R.drawable.about)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter2.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_collection_layout, parent, false) // RecyclerView에 들어갈 아이템의 레이아웃 설정
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecommendAdapter2.ViewHolder, position: Int) { // 데이터 설정
//        holder.movieImage.setImageResource(movie_images[position])
/*
        var image_task: URLtoBitmapTask = URLtoBitmapTask().apply {
//            url = URL("https://image.tmdb.org/t/p/w500" + postersList[position][0]) // 첫 번째 poster를 대표 이미지로 설정
            url = URL("https://image.tmdb.org/t/p/w500" + "/xoqr4dMbRJnzuhsWDF3XNHQwJ9x.jpg")
        }

        var bitmap: Bitmap = image_task.execute().get()
        holder.movieImage.setImageBitmap(bitmap)
*/
        // 첫 번째 poster를 대표 이미지로 설정
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + postersList[position][0]) // 불러올 이미지 url
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
            .into(holder.movieImage) // 이미지를 넣을 뷰

/*
        // 해당 아이템 클릭시 유사 사용자 추천 리스트로 이동
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserMovieListActivity::class.java)

            reco2_userId = userIdList[position] // 해당 아이템의 유사 사용자 아이디
            reco2_titleList = titlesList[position] // 해당 아이템의 추천 영화 제목 리스트
            reco2_posterList = postersList[position] // 해당 아이템의 추천 영화 포스터 링크 리스트

            intent.putExtra("reco2_userId", reco2_userId)
            intent.putExtra("reco2_titleList", reco2_titleList)
            intent.putExtra("reco2_posterList", reco2_posterList)

            // intent.putExtra("user_id", id)
//            intent.putExtra("reco2_userId", userIdList[position]) // 해당 아이템의 유사 사용자 아이디
//            intent.putExtra("reco2_titleList", titlesList[position]) // 해당 아이템의 추천 영화 제목 리스트
//            intent.putExtra("reco2_posterList", postersList[position]) // 해당 아이템의 추천 영화 포스터 링크 리스트
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
*/
    }

    override fun getItemCount(): Int {
//        return movie_images.size // 영화 개수
        return userIdList.size // 영화 개수
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        var movieImage: ImageButton
        var movieImage: ImageView

        init {
            movieImage = itemView.findViewById(R.id.movie_collection_image) // 영화 이미지 버튼

            // 해당 아이템 클릭시 유사 사용자 추천 리스트로 이동
            itemView.setOnClickListener { // 영화 포스터 이미지 클릭 시
                val position: Int = adapterPosition

                //Toast.makeText(itemView.context, "영화 콜렉션 선택", Toast.LENGTH_LONG).show()

                val intent = Intent(itemView.context, UserMovieListActivity::class.java)

                reco2_userId = userIdList[position] // 해당 아이템의 유사 사용자 아이디
                reco2_titleList = titlesList[position] // 해당 아이템의 추천 영화 제목 리스트
                reco2_posterList = postersList[position] // 해당 아이템의 추천 영화 포스터 링크 리스트

                intent.putExtra("reco2_userId", reco2_userId)
                intent.putExtra("reco2_titleList", reco2_titleList)
                intent.putExtra("reco2_posterList", reco2_posterList)
                itemView.context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}