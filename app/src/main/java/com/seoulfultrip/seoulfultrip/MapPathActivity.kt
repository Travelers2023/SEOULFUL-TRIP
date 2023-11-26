package com.seoulfultrip.seoulfultrip

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.play.core.integrity.p
import com.google.android.play.integrity.internal.x
import com.google.android.play.integrity.internal.y
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.seoulfultrip.seoulfultrip.PathActivity.Companion.itemList
import com.seoulfultrip.seoulfultrip.databinding.ActivityMapPathBinding
import com.seoulfultrip.seoulfultrip.PathActivity.Companion.pnamelist

class MapPathActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    //var itemList = mutableListOf<PlaceStorage>()
    var slatitude: Double? = 0.0 //출발지 위도
    var slongitude: Double? = 0.0//출발지 경도
    var longitudelist = mutableListOf<Double?>()
    var latitudelist = mutableListOf<Double?>()
    /*
        companion object{
            lateinit var naverMap: NaverMap
        }
        private lateinit var mapView: MapView*/
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMapPathBinding.inflate(layoutInflater)
        setContentView(binding.root)


        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        /*
                mapView = binding.mapView
                mapView.onCreate(savedInstanceState)
                mapView.getMapAsync(this)*/


        //mapFragment.getMapAsync(this)

Log.d("d", "${itemList.get(0)}")
        //Log.d("d", "${num}")

            for(i in 0..pnamelist.size-1) {
                for (index in 0..itemList.size - 1) {
                    val num = itemList.get(index)
                    Log.d("d", "${num}")
                    if (pnamelist[i] == num.pname) {
                        slongitude = num.longitude
                        slatitude = num.latitude
                        longitudelist.add(slongitude)
                        latitudelist.add(slatitude)
                        Log.d("출발지${index}", " ${slongitude},${slatitude}")
                    }
                }
            }




        val options = NaverMapOptions()
            .camera(CameraPosition(LatLng(latitudelist[0]!!, longitudelist[0]!!),15.0))
            .mapType(NaverMap.MapType.Basic)
        val mapFragment = MapFragment.newInstance(options).also {
            supportFragmentManager.beginTransaction().add(R.id.mappath, it).commit()
        }
        mapFragment.getMapAsync(this)

        MapFragment.newInstance(options)


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


            /*var ad = CameraPosition(LatLng(x,y),10.0)
            Log.d("ad","ad:" + ad)
            naverMap.cameraPosition = ad*/
        val path = PathOverlay()

        //for (index in 0 until pnamelist.size)
        when(pnamelist.size) {
            1 -> {
                path.coords = listOf(
                    LatLng(latitudelist[0]!!, longitudelist[0]!!)
                )
                path.map = naverMap
                runOnUiThread{
                    val marker = Marker() //지도에 마커표시
                    marker.position = LatLng(latitudelist[0]!!, longitudelist[0]!!)
                    marker.map = naverMap
                    Log.d("성공","마커")

                }
            }

            2 -> {
                path.coords = listOf(
                    LatLng(latitudelist[0]!!, longitudelist[0]!!),
                    LatLng(latitudelist[1]!!, longitudelist[1]!!)
                )
                path.map = naverMap
                runOnUiThread{
                    val marker = Marker() //지도에 마커표시
                    marker.position = LatLng(latitudelist[0]!!, longitudelist[0]!!)
                    marker.map = naverMap
                    val marker2 = Marker() //지도에 마커표시
                    marker2.position = LatLng(latitudelist[1]!!, longitudelist[1]!!)
                    marker2.map = naverMap
                    Log.d("성공","마커")

                }
            }

            3 -> {

                path.coords = listOf(
                    LatLng(latitudelist[0]!!, longitudelist[0]!!),
                    LatLng(latitudelist[1]!!, longitudelist[1]!!),
                    LatLng(latitudelist[2]!!, longitudelist[2]!!)
                )
                path.map = naverMap
                runOnUiThread{
                    val marker = Marker() //지도에 마커표시
                    marker.position = LatLng(latitudelist[0]!!, longitudelist[0]!!)
                    marker.map = naverMap
                    val marker2 = Marker() //지도에 마커표시
                    marker2.position = LatLng(latitudelist[1]!!, longitudelist[1]!!)
                    marker2.map = naverMap
                    val marker3 = Marker() //지도에 마커표시
                    marker3.position = LatLng(latitudelist[2]!!, longitudelist[2]!!)
                    marker3.map = naverMap
                    Log.d("성공","마커")

                }
            }

            4 -> {

                path.coords = listOf(
                    LatLng(latitudelist[0]!!, longitudelist[0]!!),
                    LatLng(latitudelist[1]!!, longitudelist[1]!!),
                    LatLng(latitudelist[2]!!, longitudelist[2]!!),
                    LatLng(latitudelist[3]!!, longitudelist[3]!!)
                )
                path.map = naverMap
                val marker = Marker() //지도에 마커표시
                marker.position = LatLng(latitudelist[0]!!, longitudelist[0]!!)
                marker.map = naverMap
                val marker2 = Marker() //지도에 마커표시
                marker2.position = LatLng(latitudelist[1]!!, longitudelist[1]!!)
                marker2.map = naverMap
                val marker3 = Marker() //지도에 마커표시
                marker3.position = LatLng(latitudelist[2]!!, longitudelist[2]!!)
                marker3.map = naverMap
                val marker4 = Marker() //지도에 마커표시
                marker4.position = LatLng(latitudelist[3]!!, longitudelist[3]!!)
                marker4.map = naverMap
                Log.d("성공","마커")
            }

            5 -> {

                path.coords = listOf(
                    LatLng(latitudelist[0]!!, longitudelist[0]!!),
                    LatLng(latitudelist[1]!!, longitudelist[1]!!),
                    LatLng(latitudelist[2]!!, longitudelist[2]!!),
                    LatLng(latitudelist[3]!!, longitudelist[3]!!),
                    LatLng(latitudelist[4]!!, longitudelist[4]!!)
                )
                path.map = naverMap

                path.map = naverMap
                val marker = Marker() //지도에 마커표시
                marker.position = LatLng(latitudelist[0]!!, longitudelist[0]!!)
                marker.map = naverMap
                val marker2 = Marker() //지도에 마커표시
                marker2.position = LatLng(latitudelist[1]!!, longitudelist[1]!!)
                marker2.map = naverMap
                val marker3 = Marker() //지도에 마커표시
                marker3.position = LatLng(latitudelist[2]!!, longitudelist[2]!!)
                marker3.map = naverMap
                val marker4 = Marker() //지도에 마커표시
                marker4.position = LatLng(latitudelist[3]!!, longitudelist[3]!!)
                marker4.map = naverMap
                val marker5 = Marker() //지도에 마커표시
                marker5.position = LatLng(latitudelist[4]!!, longitudelist[4]!!)
                marker5.map = naverMap
                Log.d("성공","마커")
            }
        }



        val options = NaverMapOptions()
                .camera(CameraPosition(LatLng(latitudelist[0]!!, longitudelist[0]!!),15.0))
                .mapType(NaverMap.MapType.Basic)

            MapFragment.newInstance(options)


            //mapFragment.getMapAsync(this)



        }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE=1000
    }
}