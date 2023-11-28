package com.seoulfultrip.seoulfultrip

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.constraintlayout.motion.widget.Debug.getLocation

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.play.integrity.internal.x
import com.google.android.play.integrity.internal.y
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationSource
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.seoulfultrip.seoulfultrip.databinding.FragmentMapBinding
import com.seoulfultrip.seoulfultrip.databinding.FragmentSearchBinding
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class MapFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    @TargetApi(Build.VERSION_CODES.P)
   private var latitude: Double = 0.0
   private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            //현재 위치 위도/경도 받아옴
            latitude = it.getDouble("latitude")
            Log.d("위치","${latitude}")
            longitude =  it.getDouble("longitude")
            Log.d("위치","${longitude}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMapBinding.inflate(inflater, container, false)


        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val options = NaverMapOptions() //option포함해서 naver지도 띄우기
            .camera(CameraPosition(LatLng(latitude,longitude ),15.0))  //띄울 위치(경도,위도), 지도 해상도
            .mapType(NaverMap.MapType.Basic) //지도 유형
        val mapFragment = com.naver.maps.map.MapFragment.newInstance(options).also {
            childFragmentManager.beginTransaction().add(R.id.mapf, it).commit()
        }
        mapFragment.getMapAsync(this)


/*

        val data = arguments?.getDouble("latitude")
        Log.d("위치","${data}")*/


        //requestLocation()

        /*
        val mapFragment = com.naver.maps.map.MapFragment.newInstance().also {
            childFragmentManager.beginTransaction().add(R.id.mapf, it).commit()
        }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)*/

/*
        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            when (it) {
                true -> {
                    ContextCompat.checkSelfPermission(requireContext(), per1)
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        PERMISSIONS,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                    val mapFragment = com.naver.maps.map.MapFragment.newInstance().also {
                        childFragmentManager.beginTransaction().add(R.id.mapf, it).commit()
                    }
                    ContextCompat.checkSelfPermission(requireContext(), per1)
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        PERMISSIONS,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                    mapFragment.getMapAsync(this)
                    locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
                    Toast.makeText(requireContext(), "권한허가", Toast.LENGTH_LONG).show()
                }

                false -> {
                    Toast.makeText(requireContext(), "권한거부", Toast.LENGTH_LONG).show()
                }
            }
        }


        requestPermission.launch(
            per1
        )
        requestPermission.launch(
            per2
        )*/


        //{Toast.makeText(requireContext(), "권한허가",Toast.LENGTH_LONG).show()},
        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {


        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        naverMap.uiSettings.isIndoorLevelPickerEnabled = true


        val options = NaverMapOptions() //option포함해서 naver지도 띄우기
            .camera(CameraPosition(LatLng(latitude,longitude ),15.0))  //띄울 위치(경도,위도), 지도 해상도
            .mapType(NaverMap.MapType.Basic) //지도 유형

        com.naver.maps.map.MapFragment.newInstance(options)


            val marker = Marker() //지도에 마커표시
            marker.position = LatLng(latitude,longitude)
            marker.map = naverMap
            Log.d("성공","마커")



    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val LOCATION_PERMISSION_REQUEST_CODE=1000
    }
}