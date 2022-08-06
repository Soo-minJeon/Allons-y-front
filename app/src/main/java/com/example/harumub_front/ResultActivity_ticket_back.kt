package com.example.harumub_front

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.Serializable
import java.lang.NumberFormatException
import java.util.ArrayList
import java.util.HashMap
import kotlin.properties.Delegates

class ResultActivity_ticket_back : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface: RetrofitInterface

    // 현재 로그인하고 있는 사용자 아이디, 선택한 영화 제목, 별점 평가, 한줄평
    lateinit var id : String
    lateinit var movie_title : String

    // 추천 정보
    lateinit var reco1_titleArray : ArrayList<String>
    lateinit var reco1_posterArray : ArrayList<String>

    lateinit var reco2_1_userId : String
    lateinit var reco2_2_userId : String
    lateinit var reco2_3_userId : String
    lateinit var reco2_4_userId : String
    lateinit var reco2_5_userId : String

    lateinit var reco2_1_title : ArrayList<String>
    lateinit var reco2_2_title : ArrayList<String>
    lateinit var reco2_3_title : ArrayList<String>
    lateinit var reco2_4_title : ArrayList<String>
    lateinit var reco2_5_title : ArrayList<String>

    lateinit var reco2_1_poster : ArrayList<String>
    lateinit var reco2_2_poster : ArrayList<String>
    lateinit var reco2_3_poster : ArrayList<String>
    lateinit var reco2_4_poster : ArrayList<String>
    lateinit var reco2_5_poster : ArrayList<String>

    lateinit var reco3_titleArray : ArrayList<String>
    lateinit var reco3_posterArray : ArrayList<String>

    lateinit var reco4_year : String
    lateinit var reco4_titleArray : ArrayList<String>
    lateinit var reco4_posterArray : ArrayList<String>

    lateinit var reco6_titleArray : ArrayList<String>
    lateinit var reco6_posterArray : ArrayList<String>
    

    lateinit var totalTicket : LinearLayout
    lateinit var myTitle : TextView
    private lateinit var myHighlight: ImageView

    lateinit var photoFile: File
    lateinit var photoBitmap: Bitmap

    // 서버에서 받아온 데이터 정의
    lateinit var result_date : String
    lateinit var result_title : String
    lateinit var result_poster : String
    lateinit var result_genres : String
    lateinit var result_concentration : String
    lateinit var result_highlight_time :String
    var result_rating by Delegates.notNull<Float>()
    lateinit var result_comment : String
    var result_isRemaked by Delegates.notNull<Boolean>()
    lateinit var result_remake_title : String
    lateinit var result_remake_poster : String
    var result_background_color by Delegates.notNull<Int>()

    var defaultImage = R.drawable.default_poster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_result_ticket_back)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        id = intent.getStringExtra("user_id").toString()
        movie_title = intent.getStringExtra("movie_title").toString()

        reco1_titleArray = intent.getSerializableExtra("reco1_titleArray") as ArrayList<String>
        reco1_posterArray = intent.getSerializableExtra("reco1_posterArray") as ArrayList<String>

        reco2_1_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_2_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_3_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_4_userId = intent.getStringExtra("reco2_1_userId").toString()
        reco2_5_userId = intent.getStringExtra("reco2_1_userId").toString()

        reco2_1_title = intent.getSerializableExtra("reco2_1_title") as ArrayList<String>
        reco2_2_title = intent.getSerializableExtra("reco2_2_title") as ArrayList<String>
        reco2_3_title = intent.getSerializableExtra("reco2_3_title") as ArrayList<String>
        reco2_4_title = intent.getSerializableExtra("reco2_4_title") as ArrayList<String>
        reco2_5_title = intent.getSerializableExtra("reco2_5_title") as ArrayList<String>

        reco2_1_poster = intent.getSerializableExtra("reco2_1_poster") as ArrayList<String>
        reco2_2_poster = intent.getSerializableExtra("reco2_2_poster") as ArrayList<String>
        reco2_3_poster = intent.getSerializableExtra("reco2_3_poster") as ArrayList<String>
        reco2_4_poster = intent.getSerializableExtra("reco2_4_poster") as ArrayList<String>
        reco2_5_poster = intent.getSerializableExtra("reco2_5_poster") as ArrayList<String>

        reco3_titleArray = intent.getSerializableExtra("reco3_titleArray") as ArrayList<String>
        reco3_posterArray = intent.getSerializableExtra("reco3_posterArray") as ArrayList<String>

        reco4_year = intent.getStringExtra("reco4_year").toString()
        reco4_titleArray = intent.getSerializableExtra("reco4_titleArray") as ArrayList<String>
        reco4_posterArray = intent.getSerializableExtra("reco4_posterArray") as ArrayList<String>

        reco6_titleArray = intent.getSerializableExtra("reco6_titleArray") as ArrayList<String>
        reco6_posterArray = intent.getSerializableExtra("reco6_posterArray") as ArrayList<String>


        result_date = intent.getStringExtra("date").toString()
        result_title = intent.getStringExtra("title").toString()
        result_poster = intent.getStringExtra("poster").toString()
        result_genres = intent.getStringExtra("genres").toString()
        result_concentration = intent.getStringExtra("concentration").toString()
        result_highlight_time = intent.getStringExtra("highlight_time").toString()
        result_rating = intent.getFloatExtra("rating", 0.0f)!!.toFloat()
        result_comment = intent.getStringExtra("comment").toString()
        result_isRemaked = intent.getBooleanExtra("remake", false)
        result_remake_title = intent.getStringExtra("remakeTitle").toString()
        result_remake_poster = intent.getStringExtra("remakePoster").toString()

        result_background_color = intent.getIntExtra("background_color", 0).toInt()

        // 리뷰 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("movie_title")) {
            Log.d("ResultActivity",
                "리뷰에서 받아온 id : " + id + " movie_title : " + movie_title)
        }
        else {
            Log.e("ResultActivity", "가져온 데이터 없음")
        }

        totalTicket = findViewById(R.id.total_ticket)
        totalTicket.setOnClickListener(ticketClick())


        myTitle = findViewById<TextView>(R.id.title)
        var myGenres = findViewById<TextView>(R.id.genres)
        var myConPer = findViewById<TextView>(R.id.concentration)
        var myHlTime = findViewById<TextView>(R.id.highlight)

        var myRating = findViewById<RatingBar>(R.id.user_rating)
        var myComment = findViewById<TextView>(R.id.user_comment)

        // var myEmotion = arrayListOf<Any>(3) // 배열에 setImageResource() 적용 안 됨. 아래처럼 사용해야 함
        var emotion1 = findViewById<ImageView>(R.id.emotion1)
        var emotion2 = findViewById<ImageView>(R.id.emotion2)
        var emotion3 = findViewById<ImageView>(R.id.emotion3)

        var myChart = findViewById<LineChart>(R.id.chart)
        myHighlight = findViewById<ImageView>(R.id.img_highlight)
        val myHighlightLayoutParams = myHighlight.layoutParams as ViewGroup.MarginLayoutParams

        var remakeLayout = findViewById<LinearLayout>(R.id.remake_movie_layout)
        var remakeTitle = findViewById<TextView>(R.id.remake_movie_title)
        var remakePoster = findViewById<ImageView>(R.id.remake_movie_poster)

        totalTicket = findViewById(R.id.total_ticket)
        totalTicket.setOnClickListener(ticketClick())
        var myBackground = findViewById<RelativeLayout>(R.id.background)
        myBackground.setBackgroundColor(result_background_color)

//        var btnMain = findViewById<Button>(R.id.back2main)
        var btnList = findViewById<ImageButton>(R.id.back2list)


        var map = HashMap<String, String>()
        map.put("id", id)
        map.put("movieTitle", movie_title)

        val call = retrofitInterface.executeWatchResult(map)
        call!!.enqueue(object : Callback<WatchResult?> {
            override fun onResponse(call: Call<WatchResult?>, response: Response<WatchResult?>) {
                if (response.code() == 200) {
                    //Toast.makeText(this@ResultActivity, "결과 출력 성공", Toast.LENGTH_SHORT).show()

                    val result = response.body()
                    Log.d("감상 영화 정보 : ", "제목 : " + result_title
                        + " 장르 : " + result_genres + " 집중 : " + result_concentration
                        + " 하이라이트 시간 : " + result_highlight_time
                        + " 별점 : " + result_rating + " 한줄평 : " + result_comment
                        + " 감상 날짜 : " + result_date
                        + " 리메이크 여부 : " + result_isRemaked + " 리메이크 작품 : " + result_remake_title
                        + " 리메이크 포스터 : " + result_remake_poster)

                    // 감상했던 영화 정보 불러오기 - 제목
                    myTitle.text = result_title

                    // 영화 장르 - String으로 받아옴 >> 문자열 자르기
                    var genres = result_genres
                    genres = genres
                        .replace("[","")
                        .replace("]", "")
                        .replace("'", "")
                        .replace(" ","")
                    println("부호,공백 > 제거 : $genres") // Action,Fantasy,Family

                    val arrGenres = genres.split(',') // 반점 기준 단어 분리
                    var total = ""
                    val arrSize = arrGenres.size
                    println("장르 총 개수: $arrSize")
                    for(i in 0 until arrSize) { // i: 0 ~ (size-1)
                        if (i == (arrSize-1)) { // 마지막이면 반점 추가 X
                            total += arrGenres[i]
                        } else {
                            total = total + arrGenres[i] + ", "
                        }
                        println("장르: $total")
                    }
                    myGenres.text = total

                    myConPer.text = result_concentration + "%"
                    myHlTime.text = result_highlight_time + "s(초)"

                    // 입력했던 별점, 한줄평 값으로 초기화
                    myRating.rating = result_rating
                    myComment.text = result_comment


                    // 감정 이모티콘 출력 - 서버에서 받아온 감정 배열
                    val emotions = result!!.emotion_count_array // 리스트 중 첫번째 배열 - 감정 배열은 최종 횟수인 하나만 전달받음
                    val counts = intArrayOf( // 감정별 최종 횟수(정수값) 배열
                        emotions[0].HAPPY, emotions[1].SAD, emotions[2].ANGRY, emotions[3].CONFUSED,
                        emotions[4].DISGUSTED, emotions[5].SURPRISED, emotions[6].FEAR
                    )
                    val emotionIndex = arrayOf( // 감정 종류 String 배열
                        "HAPPY", "SAD", "ANGRY", "CONFUSED", "DISGUSTED", "SURPRISED", "FEAR"
                    )
                    val emoji = intArrayOf( // 감정 이미지 Int 배열
                        R.drawable.happy, R.drawable.sad, R.drawable.angry,
                        R.drawable.confused, R.drawable.disgusted, R.drawable.surprised,
                        R.drawable.fear, R.drawable.calm
                    )
                    // 맵 생성
                    val emotionMap = mapOf( // 맵 생성
                        "HAPPY" to counts[0], "SAD" to counts[1], "ANGRY" to counts[2],
                        "CONFUSED" to counts[3], "DISGUSTED" to counts[4],
                        "SURPRISED" to counts[5], "FEAR" to counts[6]
                    )
                    // 맵 -> 리스트 -> 내림차순 정렬 -> 맵
                    val mapSorted = emotionMap.toList().sortedByDescending { it.second }.toMap()

                    // 감정 상위값 순서대로 키값 배열
                    val top3 = Array(7, {" "})
                    var m = 0
                    mapSorted.forEach { (key, value) ->
                        // println("key: "+ key + ", value: "+ value)
                        if(value != 0) {
                            top3[m] = key
                            m += 1
                        }
                    }
                    for(i in 0..2) {
                        for (j in 0..6) {
                            if (i == 0) {
                                if(top3[i] == emotionIndex[j]) emotion1.setImageResource(emoji[j])
                                else if(top3[i] == " ") emotion1.setImageResource(emoji[7]) // value=0: calm
                            }
                            else if (i == 1) {
                                if(top3[i] == emotionIndex[j]) emotion2.setImageResource(emoji[j])
                                else if(top3[i] == " ") emotion2.setImageResource(emoji[7]) // value=0: calm
                            }
                            else if (i == 2) {
                                if(top3[i] == emotionIndex[j]) emotion3.setImageResource(emoji[j])
                                else if(top3[i] == " ") emotion3.setImageResource(emoji[7]) // value=0: calm
                            }
                        }
                    }


                    // 감정 그래프 출력
                    val chart = ArrayList<Entry>() // 감정 차트 배열 > 새 데이터 좌표값 추가 가능

                    // Legend는 차트의 범례 (사용방법 등의 참고사항 설명)
                    val legend = myChart.legend
                    legend.setDrawInside(false)

                    // X축 (아래) - 선 유무, 사이즈, 색상, 축 위치 설정
                    val time = myChart.xAxis
                    time.setDrawAxisLine(false)
                    time.setDrawGridLines(false)
                    time.position = XAxis.XAxisPosition.BOTTOM // x축 데이터(time) 표시 위치
                    time.granularity = 1f   // 데이터 하나당 입자/원소값?
                    time.textSize = 2f
                    time.textColor = Color.rgb(118, 118, 118)
                    time.spaceMin = 0.1f // Chart 맨 왼쪽 간격 띄우기
                    time.spaceMax = 0.1f // Chart 맨 오른쪽 간격 띄우기

                    // Y축 (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
                    val yLeft = myChart.axisLeft
                    yLeft.textSize = 14f
                    yLeft.textColor = Color.rgb(163, 163, 163)
                    yLeft.setDrawAxisLine(false)
                    yLeft.axisLineWidth = 2f
                    yLeft.axisMinimum = 0f // 최솟값
                    yLeft.axisMaximum = 1.1f // 최댓값
                    yLeft.granularity = 0f // 데이터 하나당 입자/원소값?

                    // Y축 (오른쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
                    val yRight = myChart.axisRight
                    yRight.setDrawLabels(false) // label 삭제
                    yRight.textColor = Color.rgb(163, 163, 163)
                    yRight.setDrawAxisLine(false)
                    yRight.axisLineWidth = 2f
                    yRight.axisMinimum = 0f // 최솟값
                    yRight.axisMaximum = 1.1f // 최댓값
                    yRight.granularity = 0f // 데이터 하나당 입자/원소값?

                    // 서버에서 받아온 감정 배열 값 넣기 - calm 차이 해당하는 값 배열들이 전부 온 것
                    val size = result.highlight_array.size
                    val h_time = Array(size, {0F})
                    val h_diff = Array(size, {0F})

                    // for 문으로 값 배열에 넣기
                    for (i in 0 until size) {
                        h_time[i] = result.highlight_array[i].time.toFloat()    // 해당 시간 (0~러닝타임) - string > float
                        h_diff[i] = result.highlight_array[i].emotion_diff      // 해당 감정폭 값 (0~1) - float

                        chart.add(Entry(h_time[i], h_diff[i]))  // (x, y) 값 Float형으로 입력! (x: 시간, y: 감정값)
                    }

                    // 좌표값들이 담긴 엔트리 차트 배열에 대한 데이터셋 생성
                    val lineDataSet = LineDataSet(chart, "감정폭")
                    lineDataSet.setColor(Color.parseColor("#264713"))
                    lineDataSet.lineWidth = 4f
                    lineDataSet.setDrawCircles(false)

                    // 차트데이터 생성
                    val chartData = LineData()
                    chartData.addDataSet(lineDataSet) // 데이터셋을 차트데이터 안에 넣기
                    myChart.data = chartData // 선 그래프에 차트데이터 표시하기
                    myChart.invalidate() // 차트 갱신


                    // 하이라이트 이미지 - s3 버킷에서 에뮬레이터 내 다운로드 => 이미지 출력 => 기기 내 파일 삭제
                    var highlightUrl = id + "_" + movie_title + "_" + result_highlight_time + ".jpg" // Bucket 내 하이라이트 이미지 이름

                    var downloadFile = File(filesDir.absolutePath + "/" + highlightUrl) // pathname: getString(R.string.PATH)
                    downloadWithTransferUtility(highlightUrl, downloadFile) // 하이라이트 이미지 설정을 downloadWithTransferUtility(fileName, file)에서 실행

                    if (result_isRemaked == true) { // 리메이크 작품이 있을 경우
                        remakeLayout.setVisibility(View.VISIBLE) // 리메이크 작품 레이아웃 보이도록 설정

                        remakeTitle.text = result_remake_title
                        Glide.with(applicationContext)
                            .load("https://image.tmdb.org/t/p/w500" + result_remake_poster) // 불러올 이미지 url
                            .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                            .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                            .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
                            .into(remakePoster) // 이미지를 넣을 뷰
                    }
                    else { // 리메이크 작품이 없을 경우
                        remakeLayout.setVisibility(View.GONE) // 리메이크 작품 레이아웃 아예 없는 것처럼 설정  // View.INVISIBLE : 레이아웃 공간은 있지만 보이지 않도록 설정

                        var value = 50
                        var displayMetrics = resources.displayMetrics
                        var dp = Math.round(value * displayMetrics.density) // 단위 dp로 변환

                        myHighlightLayoutParams.bottomMargin = dp // 하이라이트 이미지 layout_marginBottom 설정
                        myHighlight.layoutParams = myHighlightLayoutParams
                    }
                }
                else if (response.code() == 400) {
                    //Toast.makeText(this@ResultActivity, "오류 발생", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WatchResult?>, t: Throwable) {
                //Toast.makeText(this@ResultActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })

/*
        // 메인으로 돌아가는 버튼
        btnMain.setOnClickListener {
            var intent = Intent(
                applicationContext,
                MainActivity2::class.java
            ) // 두번째 인자에 이동할 액티비티
            
            intent.putExtra("user_id", id)

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

            intent.putExtra("reco4_year", reco4_year)
            intent.putExtra("reco4_titleArray", reco4_titleArray)
            intent.putExtra("reco4_posterArray", reco4_posterArray)

            intent.putExtra("reco6_titleArray", reco6_titleArray)
            intent.putExtra("reco6_posterArray", reco6_posterArray)

            startActivityForResult(intent, 0)
        }
*/

        // 리스트 목록으로 이동하는 버튼
        btnList.setOnClickListener {
            var intent = Intent(
                applicationContext,
                WatchListActivity::class.java
            ) // 두번째 인자에 이동할 액티비티

            intent.putExtra("user_id", id)

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

            intent.putExtra("reco4_year", reco4_year)
            intent.putExtra("reco4_titleArray", reco4_titleArray)
            intent.putExtra("reco4_posterArray", reco4_posterArray)

            intent.putExtra("reco6_titleArray", reco6_titleArray)
            intent.putExtra("reco6_posterArray", reco6_posterArray)

            startActivityForResult(intent, 0)
        }
    }

    private fun ticketClick(): View.OnClickListener? {
        return View.OnClickListener() {

            totalTicket.animate().rotationY(180f).setDuration(300).withEndAction{
                totalTicket.translationY = 0f
                val intent = Intent(totalTicket.context, ResultActivity_ticket_front::class.java)
                intent.putExtra("user_id", id)
                intent.putExtra("movie_title", myTitle.text)

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

                intent.putExtra("reco4_year", reco4_year)
                intent.putExtra("reco4_titleArray", reco4_titleArray)
                intent.putExtra("reco4_posterArray", reco4_posterArray)

                intent.putExtra("reco6_titleArray", reco6_titleArray)
                intent.putExtra("reco6_posterArray", reco6_posterArray)

                totalTicket.context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

            }.start()
        }
    }

    // S3 Bucket Download
    fun downloadWithTransferUtility(fileName: String?, file: File?) {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(getString(R.string.AWS_ACCESS_KEY), getString(R.string.AWS_SECRET_KEY)) // IAM User의 (accessKey, secretKey)
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))

        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(this.applicationContext).build()
        TransferNetworkLossHandler.getInstance(this.applicationContext)

        val downloadObserver = transferUtility.download("allonsybucket1/highlight", fileName, file) // (bucket name/folder name, file 이름, file 객체)

        downloadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state === TransferState.COMPLETED) {
                    // Handle a completed download
                    Log.d("S3 Bucket ", "Download Completed!")

                    if (file != null) {
                        // 감상결과 페이지 하이라이트 이미지 설정
                        photoBitmap = BitmapFactory.decodeFile(file!!.absolutePath) // getString(R.string.FILE_PATH)
                        myHighlight.setImageBitmap(photoBitmap)
                        Log.d("하이라이트 이미지 ", "설정 완료")

                        Log.d("파일 경로 : ", file.absolutePath) // getString(R.string.FILE_PATH)

                        // S3 Bucket에서 file 다운로드 후 Emulator에서 삭제
                        file.delete()
                        Log.d("Emulator : ", "파일 삭제")
                    }
                    else {
                        Log.d("Emulator : ", "삭제할 파일이 없습니다.")
                    }
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (current.toDouble() / total * 100.0).toInt()
                Log.d("S3 Bucket", "DOWNLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: java.lang.Exception?) {
                Log.d("S3 Bucket", "DOWNLOAD ERROR - - ID: $id - - EX:$ex")
            }
        })
    }
}
