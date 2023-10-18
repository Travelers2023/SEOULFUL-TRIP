package com.seoulfultrip.seoulfultrip

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoulfultrip.seoulfultrip.GetSearchPlace
import com.seoulfultrip.seoulfultrip.Items
import com.seoulfultrip.seoulfultrip.PlaceAPI
import com.seoulfultrip.seoulfultrip.R
import com.seoulfultrip.seoulfultrip.databinding.ActivityMainBinding
import com.seoulfultrip.seoulfultrip.databinding.ActivitySearchResultBinding
import com.seoulfultrip.seoulfultrip.databinding.PlaceRetrofitBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PlaceRetrofitViewHolder(val binding: PlaceRetrofitBinding): RecyclerView.ViewHolder(binding.root)

class PlaceRetrofitAdapter(val context: Context, val datas: MutableList<Items>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
        return datas?.size?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = PlaceRetrofitViewHolder(PlaceRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as PlaceRetrofitViewHolder).binding

        val model = datas!![position]

        //xml 아직 정리 안함 (이름, 디자인 추후 수정)
        binding.itemName.text = model.title
        binding.itemType.text = model.category
        binding.itemMemo.text=model.description //*화면에 표시안됨
        binding.itemRoad.text=model.roadAddress

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


        val place = intent.getStringExtra("keyword")

        binding.edtProduct2.setText(place)

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


    }
}