package com.example.harumub_front

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.github.mikephil.charting.charts.LineChart
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

    // 현재 로그인하고 있는 사용자 아이디, 선택한 영화 아이디, 별점 평가, 한줄평
    private val id = intent.getStringExtra("user_id")
    private val movie_title = intent.getStringExtra("movie_title")
    private val ratings = intent.getStringExtra("user_rating")
    private val comments = intent.getStringExtra("user_comment")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_result)

        retrofitBuilder = RetrofitBuilder
        retrofitInterface = retrofitBuilder.api

        // 리뷰 페이지에서 전달받은 인텐트 데이터 확인
        if (intent.hasExtra("user_id") && intent.hasExtra("movie_title")
            && intent.hasExtra("user_rating") && intent.hasExtra("user_comment")
        ) {
            Log.d(
                "WatchAloneActivity",
                "리뷰에서 받아온 id : $id , movie title : $movie_title \n rating : $ratings , comment : $comments "
            )
        } else {
            Log.e("WatchAloneActivity", "가져온 데이터 없음")
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
        var myHighlight = findViewById<ImageView>(R.id.img_highlight)

        var btnMain = findViewById<Button>(R.id.back2main)
        var btnList = findViewById<Button>(R.id.back2list)


        var map = HashMap<String, String>()
        map.put("id", id!!)
        map.put("movie_title", movie_title!!)

        val call = retrofitInterface.executeWatchResult(map)
        call!!.enqueue(object : Callback<WatchResult?> {
            override fun onResponse(call: Call<WatchResult?>, response: Response<WatchResult?>) {
                if (response.code() == 200) {
                    Toast.makeText(this@ResultActivity, "결과 출력 성공", Toast.LENGTH_SHORT).show()

                    val result =
                        response.body() // 받아온 객체 배열 : title, poster, genres, emotion_array, highlight_array

                    // 감상했던 영화 정보 불러오기
                    myTitle.setText(result?.title)
                    myGenres.setText(result?.genres)
                    myConPer.setText(result?.concentration)
                    // myHlTime.setText(result?.highlight) // highlight가 배열로 옴....

                    // 영화 포스터 출력 - 웹에서 url로 가져오기
                    var posterUrl = result?.poster
                    var result_url = "https://image.tmdb.org/t/p/w500"
                    var image_task: URLtoBitmapTask = URLtoBitmapTask().apply {
                        url = URL(result_url + posterUrl)
                    }
                    var bitmap: Bitmap = image_task.execute().get()
                    myPoster.setImageBitmap(bitmap)

                    // 입력했던 별점, 한줄평 값으로 초기화
                    myRating.rating = result?.rating!!
                    myComment.setText(result.comment)


                    // 감정 이모티콘 출력 - 서버에서 받아온 감정 배열
                    // 도저히 키값을 받아오는 방법을 모르겠음 - keys(), getString()
                    // https://devsnote.com/writings/2338

//                    val jsonArray = JSONTokener(response).nextValue() as JSONArray
//
//                    val emotions = result.emotion_array // 리스트
//                    val first = emotions[0] // 리스트 중 첫번째 배열
//                    val h = first.get("HAPPY").toString()
//                    val h = first.getString("HAPPY")
//                    val c = first.HAPPY // 감정 배열 첫번째의 HAPPY 횟수

                    val emotion_count = intArrayOf(
                        // class 에서 받아올 때 List<Emotion> 말고 그냥 Emotion 형태로 받아오면
                        // result.emotion_array.HAPPY 이런 식으로 받아올 수 있음
                        // 어짜피 감정 배열은 한 개만 올 거라서
                        // 대신 서버랑 주고 받는 데이터타입 얘기해봐야 함
                        result.emotion_array[0].HAPPY, result.emotion_array[0].SAD,
                        result.emotion_array[0].ANGRY, result.emotion_array[0].CONFUSED,
                        result.emotion_array[0].DISGUSTED, result.emotion_array[0].SURPRISED,
                        result.emotion_array[0].FEAR
                    )

                    val emoji = intArrayOf(
                        R.drawable.happy, R.drawable.sad, R.drawable.angry,
                        R.drawable.confused, R.drawable.disgusted, R.drawable.surprised,
                        R.drawable.fear, R.drawable.calm
                    )
                    /**
                    var arrSorted = arr.sortedDescending() // 내림차순 정렬
                    for (i in 0..2) {
                        if (arrSorted[i] != 0) {
                            for (j in 0..6) {
                                if (i == 0) {
                                    if (arrSorted[i] == arr[j]) emotion1.setImageResource(emoji[j])
                                    else emotion1.setImageResource(emoji[7]) // calm 이미지
                                } else if (i == 1) {
                                    if (arrSorted[i] == arr[j]) emotion2.setImageResource(emoji[j])
                                    else emotion2.setImageResource(emoji[7]) // calm 이미지
                                } else if (i == 2) {
                                    if (arrSorted[i] == arr[j]) emotion3.setImageResource(emoji[j])
                                    else emotion3.setImageResource(emoji[7]) // calm 이미지
                                }
                            }
                        }
                    }
                    **/

                    // 감상 결과 - 감정 그래프 출력
                    val chart1 = ArrayList<Entry>() // 감정 차트 배열 > 새 데이터 좌표값 추가 가능
                    val chart2 = ArrayList<Entry>()
                    val chart3 = ArrayList<Entry>()

                    // 서버에서 받아온 감정 배열 값 넣기 - calm 차이 해당하는 값 배열들이 전부 온 것
                    // 그래서 어느 장면이 하이라이트인지 구하는 코드 필요
                    var highlightUrl = result.highlight_array[0].time.toString()
                    val arrHE = result.highlight_array[0].emotion
                    val arrHD = result.highlight_array[0].emotion_diff

                    // for문으로 배열에 넣기 - 수정 필요
                    chart1.add(Entry(1F, 2F)) // (x, y) 값 Float형으로 입력! (x: 시간, y: 감정값)
                    chart2.add(Entry(1F, 6F))
                    chart3.add(Entry(2F, 8F))

                    // 좌표값들이 담긴 엔트리 차트 배열에 대한 데이터셋 생성
                    // 서버에서 받아온 배열에 따른 3가지 감정 그래프
                    val lineDataSet1 = LineDataSet(chart1, arrHE) // ex) "Happy"
                    val lineDataSet2 = LineDataSet(chart2, arrHE) // ex) "Sad"
                    val lineDataSet3 = LineDataSet(chart3, arrHE) // ex) "Surprised"

                    // 차트데이터 생성
                    val chartData = LineData()
                    chartData.addDataSet(lineDataSet1) // 데이터셋을 차트데이터 안에 넣기
                    chartData.addDataSet(lineDataSet2)
                    chartData.addDataSet(lineDataSet3)
                    myChart.data = chartData // 선 그래프에 차트데이터 표시하기
                    myChart.invalidate() // 차트 갱신

                    // 하이라이트 이미지 > s3 버킷에서 다운로드 후 출력
                    // 수정 필요
                    downloadWithTransferUtility("Emotion", highlightUrl) // bucket folder name(Emotion/Eye), file name

                    // 다운로드 된 이미지 파일 불러오기기
                    myHighlight.setImageResource(R.drawable.highlight)


                    // 메인으로 돌아가는 버튼
                    btnMain.setOnClickListener {
                        var intent = Intent(
                            applicationContext,
                            MainActivity2::class.java
                        ) // 두번째 인자에 이동할 액티비티
                        intent.putExtra("user_id", id)
                        startActivityForResult(intent, 0)
                    }
                    // 리스트 목록으로 이동하는 버튼
                    btnList.setOnClickListener {
                        var intent = Intent(
                            applicationContext,
                            WatchListActivity::class.java
                        ) // 두번째 인자에 이동할 액티비티
                        intent.putExtra("user_id", id)
                        startActivityForResult(intent, 0)
                    }

                } else if (response.code() == 400) {
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
    }

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
        val downloadObserver = transferUtility.download(fileName, File(filesDir.absolutePath + "/" + fileName))
        // object: "SomeFile.mp4", 두 번째 파라미터: Local 경로 File 객체  ex) .download("SomeFile.mp4", File(filesDir.absolutePath + "/SomeFile.mp4"))

        // import 관련 오류 나는 것 같음 - 혼자 보기에서 가져온 코드이니 참고해서 수정 필요
        /*
        downloadOberver.setTransferListener(object : TransferListener { // 다운로드 과정을 알 수 있도록 Listener 추가
            override fun onStateChanged(id: Int, state: Transfer) {
                if (state == Transfer.COMPLETED) {
                    Log.d("S3", "DOWNLOAD Completed!")
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
        */
    }
}
