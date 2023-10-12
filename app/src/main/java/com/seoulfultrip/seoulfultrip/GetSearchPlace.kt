package com.seoulfultrip.seoulfultrip

data class GetSearchPlace(
    var lastBuildDate: String = "", //검색 결과를 생성한 시간
    var total: Int = 0,             //총 검색 결과 개수
    var start: Int = 0,             //검색 시작 위치
    var display: Int = 0,           //한 번에 표시할 검색 결과 개수
    var items: MutableList<Items>

    )

data class Items(
    var title: String = "",         //이름
    var category: String = "",      //분류 정보
    var description: String = "",   //상세설명

    var telephone: String = "",     //값 반환X, 하위호환성 위해 필요
    var roadAddress: String = "",   //도로명 주소
    var mapx: Int = 0,              //x좌표
    var mapy: Int = 0,              //y좌표

)

