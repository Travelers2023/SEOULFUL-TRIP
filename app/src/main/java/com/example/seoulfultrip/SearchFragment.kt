package com.example.seoulfultrip

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seoulfultrip.databinding.FragmentSearchBinding
import com.example.seoulfultrip.databinding.ItemRetrofitBinding
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MyRetrofitViewHolder(val binding: ItemRetrofitBinding): RecyclerView.ViewHolder(binding.root)
class MyRetrofitAdapter(val context: Context, val datas: MutableList<Items>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
        return datas?.size?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyRetrofitViewHolder(ItemRetrofitBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyRetrofitViewHolder).binding

       // val model = datas!!.searchp[position]
        val model = datas!![position]
        binding.itemName.text = model.title
        binding.itemType.text = model.category
        binding.itemMemo.text=model.description
        binding.itemRoad.text=model.roadAddress

        //holder.binditems(datas.searchp.get(position))

    }

}

class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        val mutableList: MutableList<Items>

        val CLIENT_ID = "2KTCwVgsVBGNBNdgkM5p"
        val CLIENT_SECRET = "N52lPewB0q"
        val BASE_URL_PLACE_API = "https://openapi.naver.com/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_PLACE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    binding.btnSearch.setOnClickListener{

        var keyword = binding.edtProduct.text.toString()

        val api = retrofit.create(PlaceAPI::class.java)
        val callGetSearchPlace = api.getSearchPlace(CLIENT_ID,CLIENT_SECRET,"${keyword}",5)

        callGetSearchPlace?.enqueue(object: Callback<GetSearchPlace>{
            override fun onResponse(
                call: Call<GetSearchPlace>,
                response: Response<GetSearchPlace>
            ) { Log.d("결과","성공")
                val body = response?.body()?.items
                val gson = GsonBuilder().create()

                //val a = gson.fromJson(body, PlaceAPI::class.java)


                if(response.isSuccessful){
                    binding.retrofitRecyclerView.layoutManager= LinearLayoutManager(context)
                    binding.retrofitRecyclerView.adapter = MyRetrofitAdapter(requireContext(),
                        response?.body()!!.items)
                }
            }

            override fun onFailure(call: Call<GetSearchPlace>, t: Throwable) {
                Log.d(" 결과", "실패")
            }
        })

    }





        mutableList = mutableListOf<Items>()
        binding.retrofitRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.retrofitRecyclerView.adapter = MyRetrofitAdapter(requireContext(), mutableList)

        return binding.root








       // return inflater.inflate(R.layout.fragment_search, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}