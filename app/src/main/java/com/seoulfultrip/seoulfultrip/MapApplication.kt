package com.seoulfultrip.seoulfultrip

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class MapApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("ylvy2f6syf")
    }
}