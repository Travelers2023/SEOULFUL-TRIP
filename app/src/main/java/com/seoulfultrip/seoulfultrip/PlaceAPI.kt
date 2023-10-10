package com.example.seoulfultrip

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PlaceAPI {
    @GET("v1/search/local.json")
    fun getSearchPlace(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,          //검색 전달
        @Query("display") display: Int? = null, //한 번에 표시할 검색 결과 개수(기본값: 1, 최댓값: 5)
        @Query("start") start: Int? = null      //검색 시작 위치(기본값: 1, 최댓값: 1)
    ): Call<GetSearchPlace>
}