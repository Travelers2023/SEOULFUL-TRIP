package com.seoulfultrip.seoulfultrip

import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.integrity.p
import com.naver.maps.geometry.LatLng
import com.seoulfultrip.seoulfultrip.StartplaceAdapter.Companion.savestname
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.databinding.ActivityPathBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PathActivity : AppCompatActivity() {
    lateinit var binding: ActivityPathBinding
    lateinit var StartplaceAdapter: StartplaceAdapter
    val startPlace:String? = savestname[0] // 출발지 이름
    val durationarray = mutableListOf<Double>()

    //val placeduration: ArrayMap<String, >
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

        val retrofit = Retrofit.Builder() //retrofit으로 api받아오기 시작
            .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/") //전달할 http
            .addConverterFactory(GsonConverterFactory.create()) //gson
            .build()

        val api = retrofit.create(PathAPI::class.java) ///PathAPI 인터페이스 전달
        //http에 아이디, 키, 시작점의 위도경도, 도착점의 위도경도 전달
        val callGetPath = api.getPath(CLIENT_ID, CLIENT_SECRET, "129.089441, 35.231100","129.084454, 35.228982")

        callGetPath.enqueue(object : Callback<PathPlace> { //PathPlace 데이터클래스 콜백
            override fun onResponse(call: Call<PathPlace>, response: Response<PathPlace>
            ){ //전달이 성공하면 여기시작
                val pathlist = response.body()?.route?.traoptimal //데이터클래스에서 Result_trackoption까지 받음, list형식이라 뒤는 따로 받아와야함
                Log.d("경로", "${pathlist}")
                for (pathdi in pathlist!!){ //pathlist에서 summary.duration받아오기 위해 for문 사용
                    val a = pathdi.summary.duration //시간받아옴
                    Log.d("거리시간", "${a}")
                }
                /* 이거는 경로간의 좌표들 인덱스로 받는건데 (path값 받아오는 거) 혹시 몰라서 주석처리 해놓음
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


        val startdelete = savepname.indexOf(startPlace)
        savepname.removeAt(startdelete)


        var slatitude: Double? = null
        var slongitude: Double? = null
        var flatitude: Double? = null
        var flongitude: Double? = null
        //var callGetPath1 : Call<PathPlace>


        for(index in 0 .. StartplaceAdapter.itemList.size - 1) {
           val num = StartplaceAdapter.itemList.get(index)
            if (startPlace == num.pname) {
               slatitude = num.latitude
                slongitude = num.longitude
                Log.d("도착","${slatitude}, ${slongitude}")
            }
        }

        for(index in 0 .. StartplaceAdapter.itemList.size - 1) {
            val num = StartplaceAdapter.itemList.get(index)
            for(index in 0..savepname.size-1){
                if (savepname[index]== num.pname) {
                    flatitude = num.latitude
                    flongitude = num.longitude
                    Log.d("도착","${flatitude}, ${flongitude}")
                }}
        }


/*
        for(index in 0 .. StartplaceAdapter.itemList.size - 1) {
            val num = StartplaceAdapter.itemList.get(index)
            for(index in 0..savepname.size-1){
            if (savepname[index]== num.pname) {
                flatitude = num.latitude
                flongitude = num.longitude
                val callGetPath1 = api.getPath(CLIENT_ID, CLIENT_SECRET, "${slatitude}, ${slongitude}","${flatitude}, ${flongitude}")
                callGetPath1.enqueue(object : Callback<PathPlace> { //PathPlace 데이터클래스 콜백
                    override fun onResponse(call: Call<PathPlace>, response: Response<PathPlace>
                    ){ //전달이 성공하면 여기시작
                        val pathlist = response.body()?.route?.traoptimal //데이터클래스에서 Result_trackoption까지 받음, list형식이라 뒤는 따로 받아와야함
                        Log.d("경로", "${pathlist}")
                        for (pathdi in pathlist!!){ //pathlist에서 summary.duration받아오기 위해 for문 사용
                            val a = pathdi.summary.duration //시간받아옴
                            Log.d("거리시간", "${a}")
                        }
                    }

                    override fun onFailure(call: Call<PathPlace>, t: Throwable) {
                        Log.d("실패", "실패")
                    }
                })
            }}
        }
*/




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
            R.id.next1_button -> {
                // 다음 구현
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
