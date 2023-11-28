package com.seoulfultrip.seoulfultrip

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import com.google.android.play.integrity.internal.x
import com.google.android.play.integrity.internal.y
import com.naver.maps.geometry.GeoConstants
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.seoulfultrip.seoulfultrip.databinding.ActivityMapBinding
import com.seoulfultrip.seoulfultrip.databinding.ActivitySearchResultBinding


class MapActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)



        val geocoder = Geocoder(this)
        val roadaddress = intent.getStringExtra("roadaddress") //도로명 주소 받기

        val geocodeListener = Geocoder.GeocodeListener { addresses -> //경도 위도 변환
            val address = addresses.iterator() //경도 위도 변환된 거 iterator로 하나씩 받음
            val a =address.next()
            val x = a.latitude
            Log.d("x","x:" + x)
            val y = a.longitude

            val options = NaverMapOptions() //option포함해서 naver지도 띄우기
                .camera(CameraPosition(LatLng(x,y),15.0))  //띄울 위치(경도,위도), 지도 해상도
                .mapType(NaverMap.MapType.Basic) //지도 유형
            val mapFragment = MapFragment.newInstance(options).also {
                supportFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
            mapFragment.getMapAsync(this)


        }

       geocoder.getFromLocationName(roadaddress!!,1,geocodeListener) //주소->경도 위도로 변환

        binding.mapback.setOnClickListener { finish() }

    }

    override fun onRequestPermissionsResult( //위치 허용
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            if (!locationSource.isActivated){
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @UiThread
    override fun onMapReady(naverMap: NaverMap) { //네이버 지도 옵션줘서 띄우려면 onMapReady 필수

        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        val geocoder = Geocoder(this)

        val roadaddress = intent.getStringExtra("roadaddress")

        val geocodeListener = Geocoder.GeocodeListener { addresses ->
            val address = addresses.iterator()
            val a =address.next()
            val x = a.latitude
            Log.d("x","x:" + x)
            val y = a.longitude
            Log.d("y","y:" + y)



            val options = NaverMapOptions()
                .camera(CameraPosition(LatLng(x,y),15.0))
                .mapType(NaverMap.MapType.Basic)

                MapFragment.newInstance(options)



            runOnUiThread{
                val marker = Marker() //지도에 마커표시
                marker.position = LatLng(x, y)
                marker.map = naverMap
                Log.d("성공","마커")

            }

        }


        geocoder.getFromLocationName(roadaddress!!,5,geocodeListener)
    }


companion object{
    private const val LOCATION_PERMISSION_REQUEST_CODE=1000
}
}