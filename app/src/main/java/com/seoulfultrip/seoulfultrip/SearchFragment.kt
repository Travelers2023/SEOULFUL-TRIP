package com.seoulfultrip.seoulfultrip

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.seoulfultrip.seoulfultrip.databinding.FragmentSearchBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val REQUEST_LOCATION = 1

    @TargetApi(Build.VERSION_CODES.P)
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    )

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



        binding.btnSearch.setOnClickListener{

            var keyword = binding.edtProduct.text.toString()
            if(keyword == null){
                Toast.makeText(this.context,"검색어를 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(getActivity(), SearchResultActivity::class.java)
                intent.putExtra("keyword", keyword)
                binding.edtProduct.text.clear()
                startActivity(intent)
            }

        }

        //엔터키 이벤트 처리
        binding.edtProduct.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> binding.btnSearch.callOnClick()
            }
            false
        })

        //맵 activity연결 버튼
        binding.nowmapbutton.setOnClickListener{
            val intent = Intent(getActivity(), NowMapActivity::class.java)
            startActivity(intent)
        }

        //map fragment 연결 버튼
        binding.nowmapbutton2.setOnClickListener {
            getLocation()
            binding.mapFragment.visibility=View.VISIBLE

            /*
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            val fragment = MapFragment()
            transaction.add(R.id.map_fragment, fragment)
            transaction.commit()*/


        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requestLocation()
    }

    fun requestLocation(){
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                PERMISSIONS[0]
            )!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                PERMISSIONS[1]
            )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                context as Activity,
                PERMISSIONS,
                REQUEST_LOCATION
            )
        }
    }

    private fun getLocation(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        //현재 위치 위도/경도로 받아오기
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener { success: Location ->
            success?.let {
                    location ->
                Log.d("현재 위치","${location.latitude}")
                val x = location.latitude
                Log.d("현재 위치","${location.longitude}")
                val y = location.longitude


                val bundle = Bundle()
                bundle.putDouble("latitude",x)
                bundle.putDouble("longitude",y)

                val fragmentmap = MapFragment()
                fragmentmap.arguments = bundle
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.map_fragment, fragmentmap)
                transaction.commit()

            }
        }

            .addOnFailureListener { fail->
                Log.d("현재 위치","${fail.localizedMessage}")
            }

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