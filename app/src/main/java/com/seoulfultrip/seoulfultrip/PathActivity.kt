package com.seoulfultrip.seoulfultrip

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.integrity.p
import com.naver.maps.geometry.LatLng
import com.seoulfultrip.seoulfultrip.databinding.ActivityPathBinding
import com.seoulfultrip.seoulfultrip.ui.theme.SEOULFULTRIPTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PathActivity : AppCompatActivity() {
    lateinit var binding: ActivityPathBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathBinding.inflate(layoutInflater)
        var pathName: String? = null
        pathName = binding.pathName.text.toString()
        setContentView(binding.root)

        setSupportActionBar(binding.Pathtoolbar) // toolbar 사용 선언
        getSupportActionBar()?.setTitle("${pathName}") // 사용자가 설정한 경로 이름으로 변경 (추후 수정)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

//        if (pathName.length > 15)
//            Toast.makeText(this,"경로 이름은 최대 15자 입니다.",Toast.LENGTH_SHORT).show()

        val CLIENT_ID = "ylvy2f6syf"
        val CLIENT_SECRET = "6kWgp5OQ0jqstBFDg1AGMtisZdzUkJy9P6PI57AT"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PathAPI::class.java)
        val callGetPath = api.getPath(CLIENT_ID, CLIENT_SECRET, "129.089441, 35.231100","129.084454, 35.228982")

        callGetPath.enqueue(object : Callback<PathPlace>{
            override fun onResponse(call: Call<PathPlace>, response: Response<PathPlace>
            ){
                val pathlist = response.body()?.route?.traoptimal

                Log.d("경로", "${pathlist}")
                for (pathdi in pathlist!!){
                    val a = pathdi.summary.duration
                    Log.d("거리시간", "${a}")
                }
/*
                val path_container : MutableList<LatLng>? = mutableListOf(LatLng(0.1,0.1))
                for(path_cords in pathlist!!){
                    for(path_cords_xy in path_cords.path){
                        //구한 경로를 하나씩 path_container에 추가해줌
                        path_container?.add(LatLng(path_cords_xy[1], path_cords_xy[0]))
                        Log.d("경로1", "${path_container}")
                    }
                }*/
            }

            override fun onFailure(call: Call<PathPlace>, t: Throwable) {
                Log.d("실패", "실패")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Menu 연걸
        menuInflater.inflate(R.menu.menu_delete, menu)

        // 메뉴 삭제 버튼 비활성화
        val deleteMenuItem = binding.Pathtoolbar.menu.findItem(R.id.delete_button)
        deleteMenuItem.isVisible = false

        val NextMenuItem = binding.Pathtoolbar.menu.findItem(R.id.next1_button)
        NextMenuItem.title = "저장"

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                finish()
            }

            R.id.next1_button -> {
                // 다음 구현
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
