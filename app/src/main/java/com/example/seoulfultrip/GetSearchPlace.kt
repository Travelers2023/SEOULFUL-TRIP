package com.example.seoulfultrip

data class GetSearchPlace(
    var lastBuildDate: String = "",
    var total: Int = 0,
    var start: Int = 0,
    var display: Int = 0,
    var items: MutableList<Items>

    )

data class Items(
    var title: String = "", //이름
    var category: String = "", //분류 정보
    var description: String = "",//상세설명

    var telephone: String = "", //값 반환X, 하위호환성 위해 필요
    var roadAddress: String = "", //도로명 주소
    var mapx: Int = 0, //x좌표
    var mapy: Int = 0, //y좌표

)


//data class GetSearchPlace(val searchp:MutableList<Items>)