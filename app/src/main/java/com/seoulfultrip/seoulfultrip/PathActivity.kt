package com.seoulfultrip.seoulfultrip
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.integrity.p
import com.naver.maps.geometry.LatLng
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.StartplaceAdapter.Companion.savestname
import com.seoulfultrip.seoulfultrip.databinding.ActivityPathBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PathActivity : AppCompatActivity() {
    lateinit var binding: ActivityPathBinding
    var startPlace:String? = savestname[0] // 출발지 이름
    lateinit var adapter: MyPathAdapter
    var itemList = mutableListOf<PlaceStorage>()
    var durationarray = mutableListOf<Int?>()
    var durationpname :MutableMap<Int?, String?> = mutableMapOf()
    var savepname2 = mutableListOf<String?>()
    var pathari : MutableMap<String?, Boolean> = mutableMapOf()
    var pnamelong: MutableMap<String?, Double?> = mutableMapOf()
    var slatitude: Double? = 0.0
    var slongitude: Double? = 0.0
    var flatitude: Double? = 0.0
    var flongitude: Double? = 0.0
    var pnamelist= mutableListOf<String?>()
    var pathName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathBinding.inflate(layoutInflater)
        pathName = binding.pathName.text.toString()
        setContentView(binding.root)

        setSupportActionBar(binding.Pathtoolbar) // toolbar 사용 선언
        getSupportActionBar()?.setTitle("${pathName}") // 사용자가 설정한 경로 이름으로 변경 (추후 수정)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        MyApplication.db.collection("place")
            //정렬 안 함
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(PlaceStorage::class.java)
                    item.docId = document.id
                    itemList.add(item)
                    Log.d("d", " ${itemList.size}")
                }
                    //binding.pathRecyclerView.layoutManager = LinearLayoutManager(this)
                    //binding.pathRecyclerView.adapter = MyPathAdapter(this, itemList)



                    for (index in 0 until savepname.size) {
                        if (startPlace != savepname.get(index)) {
                            savepname2.add(savepname[index])
                        }

                    }
                    pnamelist.add(startPlace)
                    Log.d("d", " ${savepname2}")

                    for (index in 0..itemList.size - 1) {
                        val num = itemList.get(index)
                        if (startPlace == num.pname) {
                            slongitude = num.longitude
                            slatitude = num.latitude
                            Log.d("시작도착${index}", " ${slongitude},${slatitude}")
                        }
                    }

                    for (index in 0..itemList.size - 1) {
                        val num = itemList.get(index)
                        for (index in 0..savepname2.size - 1) {
                            if (savepname2[index] == num.pname) {
                                flongitude = num.longitude
                                flatitude = num.latitude
                                Log.d("끝도착${index}", " ${flongitude}, ${flatitude}")
                                pnamelong.put(num.pname, flongitude)
                                apistart(slongitude, slatitude, flongitude, flatitude, num.pname)
                            }
                        }
                    }
                }

            .addOnFailureListener {
                Log.d("데이터 불러오기", "실패")
            }



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
            R.id.next1_button -> {  //저장 버튼을 누르면...
                // 생성된 경로 파이어베이스에 저장
                // 성공적으로 저장되면...
                Toast.makeText(this,"${pathName} 경로가 저장되었습니다.",Toast.LENGTH_SHORT).show()
                // 홈 프레그먼트로 이동
                val intent = Intent(this@PathActivity, MainActivity::class.java)
                intent.putExtra("fragmentToLoad", "HomeFragment") // HomeFragment로 이동하기 위한 식별자 전달
                startActivity(intent)
                finish() // AuthActivity 종료 (선택 사항)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun apistart( slongitude: Double?,slatitude:Double?, flongitude: Double?, flatitude:Double?, pname:String?) {


        var b: String? = ""
        val CLIENT_ID = "ylvy2f6syf"
        val CLIENT_SECRET = "6kWgp5OQ0jqstBFDg1AGMtisZdzUkJy9P6PI57AT"

        Log.d("시작도착22","${slatitude}, ${slongitude}")
        Log.d("끝도착22","${flatitude}, ${flongitude}")

        val retrofit = Retrofit.Builder() //retrofit으로 api받아오기 시작
            .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/") //전달할 http
            .addConverterFactory(GsonConverterFactory.create()) //gson
            .build()

        val api = retrofit.create(PathAPI::class.java) ///PathAPI 인터페이스 전달
        //http에 아이디, 키, 시작점의 위도경도, 도착점의 위도경도 전달
        val callGetPath = api.getPath(CLIENT_ID, CLIENT_SECRET, "${slongitude},${slatitude}", "${flongitude},${flatitude}")
        //val callGetPath = api.getPath(CLIENT_ID, CLIENT_SECRET, "37.5507254, 126.9256382", "37.5648094, 126.98108970000001")
        callGetPath.enqueue(object : Callback<PathPlace> { //PathPlace 데이터클래스 콜백
            override fun onResponse(call: Call<PathPlace>, response: Response<PathPlace>
            ) { //전달이 성공하면 여기시작
                val pathlist = response.body()?.route?.traoptimal //데이터클래스에서 Result_trackoption까지 받음, list형식이라 뒤는 따로 받아와야함
                //Log.d("경로", "${pathlist}")
                for (pathdi in pathlist!!) { //pathlist에서 summary.duration받아오기 위해 for문 사용
                    var time = pathdi.summary.duration
                    durationarray.add(time)
                    durationpname.put(time,pname)
                    Log.d("거리시간", "${time}")
                    Log.d("거리시간", "${durationpname}")
                    if(durationarray.size==savepname2.size){
                        var a = timecalculate()
                        Log.d("결괴","${a}")
                        b = durationpname.get(a)
                        pnamelist.add(b)
                        Log.d("결괴","${b}")
                        arrayreset(b)
                        if(savepname2.size==1){
                            pnamelist.add(savepname2[0])

                            Log.d("최종리스트", "${pnamelist}")
                            for (index in 0..pnamelist.size-1) {
                                binding.itemNameView1.visibility=View.VISIBLE
                                binding.itemImageView1.visibility=View.VISIBLE
                                binding.itemNameView2.visibility=View.VISIBLE
                                binding.itemImageView2.visibility=View.VISIBLE
                                binding.itemPathline1.visibility=View.VISIBLE
                                when(index) {
                                    0 -> binding.itemNameView1.setText(pnamelist[index])
                                    1 -> {binding.itemNameView2.setText(pnamelist[index])
                                            binding.itemPathline1.visibility=View.VISIBLE}
                                }
                                if (pnamelist.get(index)!=null){
                                    when(index) {
                                        2 -> {binding.itemNameView3.setText(pnamelist[index])
                                                binding.itemNameView3.visibility=View.VISIBLE
                                                binding.itemImageView3.visibility=View.VISIBLE
                                                binding.itemPathline2.visibility=View.VISIBLE}

                                        3 -> {binding.itemNameView4.setText(pnamelist[index])
                                            binding.itemNameView4.visibility=View.VISIBLE
                                            binding.itemImageView4.visibility=View.VISIBLE
                                            binding.itemPathline3.visibility=View.VISIBLE}

                                        4 -> {binding.itemNameView5.setText(pnamelist[index])
                                            binding.itemNameView5.visibility=View.VISIBLE
                                            binding.itemImageView5.visibility=View.VISIBLE
                                            binding.itemPathline4.visibility=View.VISIBLE}
                                    }
                            }}

                            return continue}
                        else{pstart(b)}

                    }

                } //시간받아옴
            }
            override fun onFailure(call: Call<PathPlace>, t: Throwable) {
                Log.d("실패", "실패")
            }
        })
        //Log.d("거리시간2", "${b}")
        //return time
    }

    private fun timecalculate(): Int? {

        var min = durationarray[0]
        for (index in 1..durationarray.size-1) {
            if(min!! > durationarray[index]!!){
                min=durationarray[index]
            }
        }
        durationarray.clear()
        return min


        }



    private fun arrayreset(pname: String?){

        savepname2.remove(pname)
        Log.d("d", " ${savepname2}")
    }

    private fun pstart(pname: String?){
        for (index in 0..itemList.size - 1) {
            val num = itemList.get(index)
            if (pname == num.pname) {
                slongitude = num.longitude
                slatitude = num.latitude
                Log.d("시작도착${index}", " ${slongitude},${slatitude}")
            }
        }
        for (index in 0..itemList.size - 1) {
            val num = itemList.get(index)
            for (index in 0..savepname2.size - 1) {
                if (savepname2[index] == num.pname) {
                    flongitude = num.longitude
                    flatitude = num.latitude
                    Log.d("끝도착${index}", " ${flongitude}, ${flatitude}")
                    pnamelong.put(num.pname, flongitude)
                    Log.d("apistart", "apistart다시")
                    apistart(slongitude, slatitude, flongitude, flatitude, num.pname)

                }
            }
        }
    }



}

