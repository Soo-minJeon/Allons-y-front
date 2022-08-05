package com.example.harumub_front

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.net.URL

class RecommendAdapter2(var id: String, var userIdList: ArrayList<String>, var titlesList: ArrayList<ArrayList<String>>, var postersList: ArrayList<ArrayList<String>>,
                        var reco1_titleArray: ArrayList<String>, var reco1_posterArray: ArrayList<String>,
                        var reco3_titleArray: ArrayList<String>, var reco3_posterArray: ArrayList<String>,
                        var reco4_year: String, var reco4_titleArray: ArrayList<String>, var reco4_posterArray: ArrayList<String>,
                        var reco6_titleArray: ArrayList<String>, var reco6_posterArray: ArrayList<String>):
    RecyclerView.Adapter<RecommendAdapter2.ViewHolder>() {

    lateinit var reco2_userId : String
    lateinit var reco2_titleList : ArrayList<String>
    lateinit var reco2_posterList : ArrayList<String>
    var defaultImage = R.drawable.default_poster

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter2.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_collection_layout, parent, false) // RecyclerView에 들어갈 아이템의 레이아웃 설정
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecommendAdapter2.ViewHolder, position: Int) { // 데이터 설정
        Glide.with(holder.itemView.context)
            .load(mergeBitmapImg(postersList[position])) // 불러올 이미지 url  // 4 개의 poster를 합쳐서 만든 하나의 대표 이미지
            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
            .into(holder.movieImage) // 이미지를 넣을 뷰
    }

    override fun getItemCount(): Int {
        return userIdList.size // 영화 개수
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView

        init {
            movieImage = itemView.findViewById(R.id.movie_collection_image) // 영화 이미지 버튼

            // 해당 아이템 클릭시 유사 사용자 추천 리스트로 이동
            itemView.setOnClickListener { // 영화 포스터 이미지 클릭 시
                val position: Int = adapterPosition

                //Toast.makeText(itemView.context, "영화 컬렉션 선택", Toast.LENGTH_LONG).show()

                val intent = Intent(itemView.context, UserMovieListActivity::class.java)

                reco2_userId = userIdList[position] // 해당 아이템의 유사 사용자 아이디
                reco2_titleList = titlesList[position] // 해당 아이템의 추천 영화 제목 리스트
                reco2_posterList = postersList[position] // 해당 아이템의 추천 영화 포스터 링크 리스트

                intent.putExtra("user_id", id)

                intent.putExtra("reco2_userId", reco2_userId)
                intent.putExtra("reco2_titleList", reco2_titleList)
                intent.putExtra("reco2_posterList", reco2_posterList)

                intent.putExtra("reco1_titleArray", reco1_titleArray)
                intent.putExtra("reco1_posterArray", reco1_posterArray)

                intent.putExtra("reco2_1_userId", userIdList[0])
                intent.putExtra("reco2_2_userId", userIdList[1])
                intent.putExtra("reco2_3_userId", userIdList[2])
                intent.putExtra("reco2_4_userId", userIdList[3])
                intent.putExtra("reco2_5_userId", userIdList[4])

                intent.putExtra("reco2_1_title", titlesList[0])
                intent.putExtra("reco2_2_title", titlesList[1])
                intent.putExtra("reco2_3_title", titlesList[2])
                intent.putExtra("reco2_4_title", titlesList[3])
                intent.putExtra("reco2_5_title", titlesList[4])

                intent.putExtra("reco2_1_poster", postersList[0])
                intent.putExtra("reco2_2_poster", postersList[1])
                intent.putExtra("reco2_3_poster", postersList[2])
                intent.putExtra("reco2_4_poster", postersList[3])
                intent.putExtra("reco2_5_poster", postersList[4])

                intent.putExtra("reco3_titleArray", reco3_titleArray)
                intent.putExtra("reco3_posterArray", reco3_posterArray)

                intent.putExtra("reco4_year", reco4_year)
                intent.putExtra("reco4_titleArray", reco4_titleArray)
                intent.putExtra("reco4_posterArray", reco4_posterArray)

                intent.putExtra("reco6_titleArray", reco6_titleArray)
                intent.putExtra("reco6_posterArray", reco6_posterArray)

                itemView.context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}

// 4 개의 poster 합쳐서 하나의 대표 이미지 만드는 함수
private fun mergeBitmapImg(posterList: ArrayList<String>): Bitmap {
    var image_task1: URLtoBitmapTask = URLtoBitmapTask().apply {
        url = URL("https://image.tmdb.org/t/p/w500" + posterList[0]) // 첫 번째 poster
    }
    var bitmap1: Bitmap = image_task1.execute().get()

    var image_task2: URLtoBitmapTask = URLtoBitmapTask().apply {
        url = URL("https://image.tmdb.org/t/p/w500" + posterList[1]) // 두 번째 poster
    }
    var bitmap2: Bitmap = image_task2.execute().get()

    var image_task3: URLtoBitmapTask = URLtoBitmapTask().apply {
        url = URL("https://image.tmdb.org/t/p/w500" + posterList[2]) // 세 번째 poster
    }
    var bitmap3: Bitmap = image_task3.execute().get()

    var image_task4: URLtoBitmapTask = URLtoBitmapTask().apply {
        url = URL("https://image.tmdb.org/t/p/w500" + posterList[3]) // 네 번째 poster
    }
    var bitmap4: Bitmap = image_task4.execute().get()

    var bitmapList = ArrayList<Bitmap>()
    bitmapList.add(bitmap1)
    bitmapList.add(bitmap2)
    bitmapList.add(bitmap3)
    bitmapList.add(bitmap4)

    var collection_poster : Bitmap = Bitmap.createBitmap(bitmapList[0].width, bitmapList[0].height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(collection_poster)

    bitmapList[0] = Bitmap.createScaledBitmap(bitmapList[0], bitmapList[0].width / 2, bitmapList[0].height / 2, true) // width = width / 2  // height = height / (width / (width / 2))
    bitmapList[1] = Bitmap.createScaledBitmap(bitmapList[1], bitmapList[1].width / 2, bitmapList[1].height / 2, true)
    bitmapList[2] = Bitmap.createScaledBitmap(bitmapList[2], bitmapList[2].width / 2, bitmapList[2].height / 2, true)
    bitmapList[3] = Bitmap.createScaledBitmap(bitmapList[3], bitmapList[3].width / 2, bitmapList[3].height / 2, true)

    canvas.drawBitmap(bitmapList[0], 0.toFloat(), 0.toFloat(), null)
    canvas.drawBitmap(bitmapList[1], (collection_poster.width / 2).toFloat(), 0.toFloat(), null)
    canvas.drawBitmap(bitmapList[2], 0.toFloat(), (collection_poster.height / 2).toFloat(), null)
    canvas.drawBitmap(bitmapList[3], (collection_poster.width / 2).toFloat(), (collection_poster.height / 2).toFloat(), null)

    return collection_poster
}
