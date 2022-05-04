package com.example.harumub_front

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.view.*
import kotlinx.android.synthetic.main.activity_search.view.imageView
import kotlinx.android.synthetic.main.recyclerview_row.view.*
import java.net.URL

/**
 * Created by 규열 on 2018-02-13.
 */
class SearchAdapter(var context: Context, var unFilteredlist: ArrayList<String>, var posterList: ArrayList<String>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>(), Filterable {

    var filteredList: ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false) // RecyclerView에 들어갈 아이템의 레이아웃 설정
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = filteredList[position]
    }

    override fun getItemCount(): Int {
        return filteredList.size // 검색된 영화 개수
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById<View>(R.id.textview) as TextView
        }
        fun setItem(item: String) {
/*
            if(item.equals("About Times")){
                itemView.imageView.setImageResource(R.drawable.about)
                itemView.textview.text = "About Times"
            }
            else if(item.equals("Gucci")) {
                itemView.imageView.setImageResource(R.drawable.gucci)
                itemView.textview.text ="Gucci"
            }
            else if(item.equals("Spider Man3")) {
                itemView.imageView.setImageResource(R.drawable.spider)
                itemView.textview.text ="Spider Man3"
            }
*/
            for (i: Int in 0..filteredList!!.size - 1) {
                if (item.equals(filteredList[i])) {
                    var image_task: URLtoBitmapTask = URLtoBitmapTask().apply {
                        url = URL("https://image.tmdb.org/t/p/w500" + posterList[i])
                    }

                    var bitmap: Bitmap = image_task.execute().get()
                    itemView.imageView.setImageBitmap(bitmap)

                    itemView.textview.text = filteredList[i]
                }
            }

            itemView.setOnClickListener { // 영화 클릭 시 토스트 메세지
                Toast.makeText(itemView.context, itemView.textview.text, Toast.LENGTH_LONG).show()

                // movie title 전달
                var movie_title = itemView.textview.text
//                var intent = Intent(itemView.getContext(), WatchAloneActivity::class.java) // WatchAloneActivity로 전달
                var intent = Intent(itemView.getContext(), SearchActivity::class.java) // SearchActivity로 전달
                intent.putExtra("movie_title", movie_title)
            }
        }
    }

    override fun getFilter(): Filter { // 검색
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                filteredList = if (charString.isEmpty()) {
                    unFilteredlist
                } else {
                    val filteringList = ArrayList<String>()
                    for (name in unFilteredlist) {
                        if (name.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name)
                        }
                    }
                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) { // 검색 결과
                filteredList = results.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        val item = filteredList[position]
        holder.setItem(item)
    }

    init {
        filteredList = unFilteredlist
    }
}