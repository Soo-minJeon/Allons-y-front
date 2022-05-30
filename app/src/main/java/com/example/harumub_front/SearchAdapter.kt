package com.example.harumub_front

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.activity_search.view.imageView
import kotlinx.android.synthetic.main.recyclerview_row.view.*
import java.net.URL
import kotlin.properties.Delegates

//class SearchAdapter(var context: Context, var id: String, var unFilteredlist: ArrayList<String>, var posterList: ArrayList<String>, var runningTimeList: ArrayList<Int>) :
class SearchAdapter(var context: Context, var id: String, var movieList: ArrayList<MovieModel>, var unFilteredlist: ArrayList<String>, var posterList: ArrayList<String>, var runningTimeList: ArrayList<Int>,
                    var reco1_titleArray: ArrayList<String>, var reco1_posterArray: ArrayList<String>,
                    var reco2_1_userId: String, var reco2_2_userId: String, var reco2_3_userId: String,
                    var reco2_4_userId: String, var reco2_5_userId: String,
                    var reco2_1_title : ArrayList<String>, var reco2_2_title : ArrayList<String>, var reco2_3_title : ArrayList<String>,
                    var reco2_4_title : ArrayList<String>, var reco2_5_title : ArrayList<String>,
                    var reco2_1_poster : ArrayList<String>, var reco2_2_poster : ArrayList<String>, var reco2_3_poster : ArrayList<String>,
                    var reco2_4_poster : ArrayList<String>, var reco2_5_poster : ArrayList<String>,
                    var reco3_titleArray : ArrayList<String>, var reco3_posterArray : ArrayList<String>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>(), Filterable {

    var filteredMovieList = ArrayList<MovieModel>()

    var filteredList: ArrayList<String>
    var defaultImage = R.drawable.default_poster

    var running_time by Delegates.notNull<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false) // RecyclerView에 들어갈 아이템의 레이아웃 설정
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = filteredList[position]
    }

    override fun getItemCount(): Int {
//        return filteredList.size // 검색된 영화 개수
        return filteredMovieList.size // 검색된 영화 개수
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById<View>(R.id.textview) as TextView
        }

        fun setItem(item: MovieModel) {
//            for (i: Int in 0..filteredList!!.size - 1) {
            for (i: Int in 0..filteredMovieList.size - 1) {
                if (item.equals(filteredMovieList[i])) {
/*
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + posterList[i]) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                        .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
                        .into(itemView.imageView) // 이미지를 넣을 뷰

                    itemView.textview.text = filteredList[i]

                    running_time = runningTimeList[i]
*/
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w500" + filteredMovieList[i].moviePoster) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                        .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
                        .into(itemView.imageView) // 이미지를 넣을 뷰

                    itemView.textview.text = filteredMovieList[i].movieTitle

                    running_time = filteredMovieList[i].movieRunningTime
                }
            }

            itemView.setOnClickListener { // 영화 클릭 시 토스트 메세지
//                Toast.makeText(itemView.context, itemView.textview.text, Toast.LENGTH_LONG).show()

                // movie title 전달
                var movie_title = itemView.textview.text
                var intent = Intent(itemView.context, WatchAloneActivity::class.java) // WatchAloneActivity로 전달
  //              var intent = Intent(itemView.context, SearchActivity::class.java) // SearchActivity로 전달
                intent.putExtra("user_id", id)
                intent.putExtra("movie_title", movie_title)
                intent.putExtra("running_time", running_time)

                intent.putExtra("reco1_titleArray", reco1_titleArray)
                intent.putExtra("reco1_posterArray", reco1_posterArray)

                intent.putExtra("reco2_1_userId", reco2_1_userId)
                intent.putExtra("reco2_2_userId", reco2_2_userId)
                intent.putExtra("reco2_3_userId", reco2_3_userId)
                intent.putExtra("reco2_4_userId", reco2_4_userId)
                intent.putExtra("reco2_5_userId", reco2_5_userId)

                intent.putExtra("reco2_1_title", reco2_1_title)
                intent.putExtra("reco2_2_title", reco2_2_title)
                intent.putExtra("reco2_3_title", reco2_3_title)
                intent.putExtra("reco2_4_title", reco2_4_title)
                intent.putExtra("reco2_5_title", reco2_5_title)

                intent.putExtra("reco2_1_poster", reco2_1_poster)
                intent.putExtra("reco2_2_poster", reco2_2_poster)
                intent.putExtra("reco2_3_poster", reco2_3_poster)
                intent.putExtra("reco2_4_poster", reco2_4_poster)
                intent.putExtra("reco2_5_poster", reco2_5_poster)

                intent.putExtra("reco3_titleArray", reco3_titleArray)
                intent.putExtra("reco3_posterArray", reco3_posterArray)

                itemView.context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    override fun getFilter(): Filter { // 검색
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()

                if (charString.isEmpty()) {
                    filteredList = unFilteredlist
//                    for (i in 0..(unFilteredlist.size - 1)) {
//                        filteredMovieList?.add(MovieModel(unFilteredlist[i], posterList[i], runningTimeList[i]))
//                    }
                    filteredMovieList = movieList
                }
                else {
                    val filteringList = ArrayList<String>()
                    val filteringMovieList = ArrayList<MovieModel>()

//                    for (name in unFilteredlist) {
                    for (i in 0..(unFilteredlist.size - 1)) {
                        if (unFilteredlist[i].toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(unFilteredlist[i])
//                            filteredMovieList?.add(MovieModel(unFilteredlist[i], posterList[i], runningTimeList[i]))
                            filteringMovieList.add(MovieModel(unFilteredlist[i], posterList[i], runningTimeList[i]))
                        }
                    }

                    filteredList = filteringList
                    filteredMovieList = filteringMovieList
                }

                val filterResults = FilterResults()
//                filterResults.values = filteredList
                filterResults.values = filteredMovieList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) { // 검색 결과
//                filteredList = results.values as ArrayList<String>
                filteredMovieList = results.values as ArrayList<MovieModel>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        val item = filteredMovieList[position]
        holder.setItem(item)
    }

    init {
        filteredList = unFilteredlist
        filteredMovieList = movieList
    }
}