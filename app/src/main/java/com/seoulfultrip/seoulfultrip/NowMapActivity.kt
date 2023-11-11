package com.seoulfultrip.seoulfultrip

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.JoiningGroup.PE
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import com.google.android.play.integrity.internal.x
import com.google.android.play.integrity.internal.y
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.seoulfultrip.seoulfultrip.databinding.ActivityNowMapBinding

class NowMapActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNowMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //지도 연결
        val mapFragment = MapFragment.newInstance().also {
            supportFragmentManager.beginTransaction().add(R.id.mapnow, it).commit()
        }
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        binding.mapbacknow.setOnClickListener { finish() }

    }

    override fun onRequestPermissionsResult( //위치 허용
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                naverMap.locationTrackingMode=LocationTrackingMode.Follow
                Toast.makeText(this, "위치권한 확인 중", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "위치권한 허가", Toast.LENGTH_LONG).show()
            }
        }
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
    override fun onMapReady(naverMap: NaverMap){
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)


    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE=1000
    }
}
