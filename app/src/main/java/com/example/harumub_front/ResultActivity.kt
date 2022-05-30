package com.example.harumub_front

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer
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
import kotlinx.android.synthetic.main.activity_watch_alone.*
import kotlinx.android.synthetic.main.fragment_addreview.*
import kotlinx.android.synthetic.main.fragment_result.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

class ResultActivity : AppCompatActivity() {
    private lateinit var retrofitBuilder: RetrofitBuilder
    private lateinit var retrofitInterface: RetrofitInteface

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

    private lateinit var myHighlight: ImageView

    lateinit var photoFile: File
    lateinit var photoBitmap: Bitmap

    var defaultImage = R.drawable.default_poster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_result)

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

        // 리뷰 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("movie_title")) {
            Log.d("ResultActivity",
                "리뷰에서 받아온 id : " + id + " movie_title : " + movie_title)
        }
        else {
            Log.e("ResultActivity", "가져온 데이터 없음")
        }

        var myTitle = findViewById<TextView>(R.id.title)
        var myPoster = findViewById<ImageView>(R.id.poster)
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

        var btnMain = findViewById<Button>(R.id.back2main)
        var btnList = findViewById<Button>(R.id.back2list)


        var map = HashMap<String, String>()
        map.put("id", id)
        map.put("movieTitle", movie_title)

        val call = retrofitInterface.executeWatchResult(map)
        call!!.enqueue(object : Callback<WatchResult?> {
            override fun onResponse(call: Call<WatchResult?>, response: Response<WatchResult?>) {
                if (response.code() == 200) {
                    //Toast.makeText(this@ResultActivity, "결과 출력 성공", Toast.LENGTH_SHORT).show()

                    val result = response.body()
                    Log.d("감상 영화 정보 : ", "제목 : " + result!!.title
                        + " 장르 : " + result.genres + " 집중 : " + result.concentration
                        + " 하이라이트 시간 : " + result.highlight_time
                        + " 별점 : " + result.rating + " 한줄평 : " + result.comment)

                    // 감상했던 영화 정보 불러오기 - 제목
                    myTitle.text = result.title

                    // 영화 장르 - String으로 받아옴 >> 문자열 자르기
                    var genres = result.genres
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

                    myConPer.text = result.concentration + "%"
                    myHlTime.text = result.highlight_time + "s(초)"

                    // 영화 포스터 출력 - 웹에서 url로 가져오기
                    var posterUrl = result?.poster
/*
                    var result_url = "https://image.tmdb.org/t/p/w500"
                    var image_task: URLtoBitmapTask = URLtoBitmapTask().apply {
                        url = URL(result_url + posterUrl)
                    }
                    var bitmap: Bitmap = image_task.execute().get()
                    myPoster.setImageBitmap(bitmap)
*/
                    Glide.with(applicationContext)
                        .load("https://image.tmdb.org/t/p/w500" + posterUrl) // 불러올 이미지 url
                        .placeholder(defaultImage) // 이미지 로딩 시작하기 전 표시할 이미지
                        .error(defaultImage) // 로딩 에러 발생 시 표시할 이미지
                        .fallback(defaultImage) // 로드할 url이 비어있을(null 등) 경우 표시할 이미지
                        .into(myPoster) // 이미지를 넣을 뷰

                    // 입력했던 별점, 한줄평 값으로 초기화
                    myRating.rating = result.rating
                    myComment.text = result.comment


                    // 감정 이모티콘 출력 - 서버에서 받아온 감정 배열
                    val emotions = result.emotion_count_array // 리스트 중 첫번째 배열 - 감정 배열은 최종 횟수인 하나만 전달받음
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
                        // println("key: "+ key)
                        top3[m] = key
                        m += 1
                    }
                    for(i in 0..2) {
                        for (j in 0..6) {
                            if (i == 0) {
                                if(top3[i] == emotionIndex[j]) emotion1.setImageResource(emoji[j])
                            }
                            else if (i == 1) {
                                if(top3[i] == emotionIndex[j]) emotion2.setImageResource(emoji[j])
                            }
                            else if (i == 2) {
                                if(top3[i] == emotionIndex[j]) emotion3.setImageResource(emoji[j])
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


//                    var highlight_movie_title = movie_title.replace(" ", "") // 영화 제목 공백 제거
                    // 하이라이트 이미지 - s3 버킷에서 에뮬레이터 내 다운로드 => 이미지 출력 => 기기 내 파일 삭제
                    var highlightUrl = id + "_" + movie_title + "_" + result.highlight_time + ".jpg" // Bucket 내 하이라이트 이미지 이름
//                    var highlightUrl = id + "_" + highlight_movie_title + "_" + result.highlight_time + ".jpg" // Bucket 내 하이라이트 이미지 이름
    //                downloadWithTransferUtility("Highlight", highlightUrl) // bucket folder name(Emotion/Eye), file name

                    var downloadFile = File(filesDir.absolutePath + "/" + highlightUrl)
//                    var path = "/data/data/com.example.harumub_front/img" // path 설정
//                    var downloadFile = File(path + "/" + highlightUrl) // 설정한 path로 다운로드 파일 생성
                    downloadWithTransferUtility(highlightUrl, downloadFile) // 하이라이트 이미지 설정을 downloadWithTransferUtility(fileName, file)에서 실행

                }
                else if (response.code() == 400) {
                    Toast.makeText(this@ResultActivity, "오류 발생", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WatchResult?>, t: Throwable) {
                Toast.makeText(
                    this@ResultActivity, t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
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

            startActivityForResult(intent, 0)
        }

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

            startActivityForResult(intent, 0)
        }
    }

/*
    // s3 버킷에서 이미지 파일 다운로드 구현
    fun downloadWithTransferUtility(s3Bucket_FolderName: String?, fileName: String?) {
        // Cognito 샘플 코드. CredentialsProvider 객체 생성
        val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext,
            "자격 증명 풀 ID", // 자격 증명 풀 ID
            Regions.AP_NORTHEAST_2 // Region
        )
//        val awsCredentials: AWSCredentials = BasicAWSCredentials("access_Key", "secret_Key") // IAM User의 (accessKey, secretKey)
//        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.US_EAST_1))
        TransferNetworkLossHandler.getInstance(applicationContext) // 반드시 호출해야 한다.

        // TransferUtility 객체 생성
        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .defaultBucket(s3Bucket_FolderName) // 디폴트 버킷 이름.
            .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)))
            .build()

        // 다운로드 실행
        photoFile = File(filesDir.absolutePath + "/" + fileName)
        val downloadObserver = transferUtility.download(fileName, photoFile)
        // 첫 번째 파라미터: object = "SomeFile.mp4"
        // 두 번째 파라미터: Local 경로 File 객체
        // ex) .download("SomeFile.mp4", File(filesDir.absolutePath + "/SomeFile.mp4"))

        downloadObserver.setTransferListener(object : TransferListener { // 다운로드 과정을 알 수 있도록 Listener 추가
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.d("S3", "DOWNLOAD Completed!")

                    // (1) URI로 이미지 출력하는 방법

                    var filePath = "filesDir.absolutePath + \"/\" + fileName"   // 파일 실제 경로
                    var uri = Uri.parse(filePath) // uri string
                    myHighlight.setImageURI(uri)

                    // S3 Bucket에서 다운 받은 file을 Emulator에서 삭제
                    var file = File(filePath) // (pathname) 다운로드 된 이미지 파일 객체
                    if (file != null) {
                        file.delete()
                        Log.d("Emulator : ", "파일 삭제")
                    } else {
                        Log.d("Emulator : ", "삭제할 파일이 없습니다.")
                    }

                    // (2) Bitmap 으로 이미지 출력?
                    // (1), (2)번 중 선택해서 수정 부탁
                   photoBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                try {
                    val done = (((current.toDouble() / total) * 100.0).toInt()) // as Int
                    Log.d("S3", "DOWNLOAD - - ID: $id, percent done = $done")
                }
                catch (e: Exception) {
                    Log.d("S3", "Trouble calculating progress percent", e)
                }
            }
            override fun onError(id: Int, ex: Exception) {
                Log.d("S3", "DOWNLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })
    }
*/

    fun downloadWithTransferUtility(fileName: String?, file: File?) {
        val awsCredentials: AWSCredentials = BasicAWSCredentials("access_Key", "secret_Key") // IAM User의 (accessKey, secretKey)
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
                        photoBitmap = BitmapFactory.decodeFile(file!!.absolutePath)
                        myHighlight.setImageBitmap(photoBitmap)
                        Log.d("하이라이트 이미지 ", "설정 완료")

                        Log.d("파일 경로 : ", file.absolutePath)

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
