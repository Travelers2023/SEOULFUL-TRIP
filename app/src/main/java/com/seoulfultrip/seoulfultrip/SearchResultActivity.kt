package com.seoulfultrip.seoulfultrip

//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.databinding.ActivitySearchResultBinding
import com.seoulfultrip.seoulfultrip.databinding.PlaceRetrofitBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRetrofitViewHolder(val binding: PlaceRetrofitBinding): RecyclerView.ViewHolder(binding.root)

class PlaceRetrofitAdapter(val context: Context, val datas: MutableList<Items>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
//    companion object {
//        lateinit var model : Items
//    }

    override fun getItemCount(): Int {
        return datas?.size?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = PlaceRetrofitViewHolder(PlaceRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as PlaceRetrofitViewHolder).binding
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val model = datas!![position]

        //xml 아직 정리 안함 (이름, 디자인 추후 수정)
        binding.itemName.text = model.title
        binding.itemType.text = model.category
        binding.itemMemo.text=model.description //*화면에 표시안됨
        binding.itemRoad.text=model.roadAddress

        //저장된 장소는 saveBtn_1로 표시
        val placeName = model.title
        val itemList: MutableList<PlaceStorage>? = null
        val user = Firebase.auth.currentUser
        var data = itemList?.get(position)


//        val placeRef = db.collection("place").whereEqualTo("pname", placeName)

        // 이메일로 장소 이름 걸러오기
        var placeRef = db.collection("place").whereEqualTo("pname", placeName)

        if(user?.email == data?.email) { // 이메일이 같다면 장소 이름을 받아오기
            placeRef = db.collection("place").whereEqualTo("pname", placeName)
        }


        // 로그인한 아이디에서 동일한 장소가 저장되어있는지 확인해야 할 듯 -> 내가 이해한 코드가 아닐까봐 고치지는 않음
        if(user?.email == data?.email) {
            placeRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.isEmpty) {
                        // 동일한 장소가 이미 존재하는 경우
                        binding.saveBtn.visibility = View.GONE
                        binding.saveBtn1.visibility = View.VISIBLE
                    } else {
                        // 동일한 장소가 존재하지 않는 경우
                        binding.saveBtn.visibility = View.VISIBLE
                        binding.saveBtn1.visibility = View.GONE
                    }
                } else {
                    Log.d("Firestore", "Error getting documents: ", task.exception)
                }
            }
        }

        val geocoder = Geocoder(context)

        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            val address = addresses.iterator()
            val a = address.next()
            Log.d("geocode","yy")
            val x = a.latitude
            val y = a.longitude

            //장소저장 눌렀을 때 파이어베이스에 저장
            // saveBtn 버튼 클릭 이벤트
            binding.placestorage.setOnClickListener{
                Log.d("SW","버튼 눌림")
                binding.saveBtn.visibility = View.GONE
                binding.saveBtn1.visibility = View.VISIBLE
                val placeinf = mapOf(
                    "email" to MyApplication.email,
                    "latitude" to x,
                    "longitude" to y,
                    "pname" to model.title,
                    "paddress" to model.roadAddress
                )
                db.collection("place")
                    .add(placeinf)
                    .addOnSuccessListener { documentReference ->
                        Log.d("store", "firebase save : ${documentReference.id}")
                        Toast.makeText(context,"장소 저장 완료",Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e ->
                        Log.d("store", "firebase save error", e)
                    }
            }

// saveBtn1 버튼 클릭 이벤트
            binding.placestorage2.setOnClickListener {
                Log.d("SW","버튼 눌림")
                // saveBtn1을 숨기고 saveBtn을 표시
                binding.saveBtn.visibility = View.VISIBLE
                binding.saveBtn1.visibility = View.GONE

                // 해당 장소를 Firestore에서 삭제하는 코드 추가
                val placeName = model.title
                val placeRef = db.collection("place").whereEqualTo("pname", placeName)

                placeRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            // Firestore에서 해당 장소 삭제
                            db.collection("place").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Log.d("Firestore", "DocumentSnapshot successfully deleted.")
                                    Toast.makeText(context,"장소 삭제 완료",Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Firestore", "삭제 실패", e)
                                }
                        }
                    } else {
                        Log.d("Firestore", "삭제 에러 :", task.exception)
                    }
                }

            }




        }
        geocoder.getFromLocationName(model.roadAddress!!,1,geocodeListener)

        binding.mapbutton.setOnClickListener {

            var mapx = model.mapx
            var mapy = model.mapy
            var roadaddress = model.roadAddress
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra("mapx", mapx)
            intent.putExtra("mapy", mapy)
            intent.putExtra("roadaddress",roadaddress)
            startActivity(context,intent,null)


        }



    }

}


class SearchResultActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var place = intent.getStringExtra("keyword")
        binding.edtProduct2.setText(place)

        //엔터키 이벤트 처리
        binding.edtProduct2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    val inputManager =
                        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        this.currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    false

                    binding.btnSearch2.callOnClick()
                }
            }
            false
        })

        val CLIENT_ID = "2KTCwVgsVBGNBNdgkM5p"
        val CLIENT_SECRET = "N52lPewB0q"
        val BASE_URL_PLACE_API = "https://openapi.naver.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_PLACE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


       // var keyword = binding.edtProduct2.text.toString()

        val api = retrofit.create(PlaceAPI::class.java)
        val callGetSearchPlace = api.getSearchPlace(CLIENT_ID,CLIENT_SECRET,"${place}",5) //PlaceAPI 값 전달

        callGetSearchPlace?.enqueue(object: Callback<GetSearchPlace> {
            override fun onResponse(
                call: Call<GetSearchPlace>,
                response: Response<GetSearchPlace>
            ) { Log.d("결과","성공")

                if(response.isSuccessful){
                    binding.retrofitRecyclerView.layoutManager= LinearLayoutManager(this@SearchResultActivity)
                    binding.retrofitRecyclerView.adapter = PlaceRetrofitAdapter(this@SearchResultActivity,
                        response?.body()!!.items) //GetSearchPlace전달 (구조 확인 잘하기)

                }
            }

            override fun onFailure(call: Call<GetSearchPlace>, t: Throwable) {
                Log.d(" 결과", "실패")
            }
        })



        binding.btnSearch2.setOnClickListener{

            var keyword = binding.edtProduct2.text.toString()

            val api = retrofit.create(PlaceAPI::class.java)
            val callGetSearchPlace = api.getSearchPlace(CLIENT_ID,CLIENT_SECRET,"${keyword}",5) //PlaceAPI 값 전달

            callGetSearchPlace?.enqueue(object: Callback<GetSearchPlace> {
                override fun onResponse(
                    call: Call<GetSearchPlace>,
                    response: Response<GetSearchPlace>
                ) { Log.d("결과","성공")

                    if(response.isSuccessful){
                        binding.retrofitRecyclerView.layoutManager= LinearLayoutManager(this@SearchResultActivity)
                        binding.retrofitRecyclerView.adapter = PlaceRetrofitAdapter(this@SearchResultActivity,
                            response?.body()!!.items) //GetSearchPlace전달 (구조 확인 잘하기)
                    }
                }

                override fun onFailure(call: Call<GetSearchPlace>, t: Throwable) {
                    Log.d(" 결과", "실패")
                }
            })
        }



        binding.backimg.setOnClickListener {

            finish()
            var searchhomefragment = SearchFragment()
            var bundle = Bundle()

            //SearchResultActivity().supportFragmentManager.beginTransaction().replace(R.id.main_layout,searchhomefragment).commit()
           /* supportFragmentManager.beginTransaction()
                .replace(R.id, searchhomefragment)
                .commit()*/
        }

        /*Thread() {
            @Override
            fun run() {
                runOnUiThread(Runnable() {
                    fun run() {

                    }
                })
            }
        }*/


    }
}