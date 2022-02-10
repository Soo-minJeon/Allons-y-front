package com.example.harumub_front

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() , TextWatcher {
    var recyclerView: RecyclerView? = null
    var editText: EditText? = null
    var adapter: SearchAdapter? = null
    var items = ArrayList<String>()
    var poster = arrayOf(R.drawable.about, R.drawable.gucci, R.drawable.spider)
    var title = arrayOf("About Times", "Gucci", "Spider Man3")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById<View>(R.id.recylcerview) as RecyclerView
        editText = findViewById<View>(R.id.search_edt) as EditText
        editText!!.addTextChangedListener(this)

        for(i: Int in 0..poster.size-1) {
            items.add(title[i])
        }

        adapter = SearchAdapter(applicationContext, items)
        recyclerView!!.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = adapter
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        adapter?.getFilter()?.filter(charSequence)
    }

    override fun afterTextChanged(editable: Editable) {}
}