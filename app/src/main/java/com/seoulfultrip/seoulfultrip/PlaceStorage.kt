package com.seoulfultrip.seoulfultrip

data class PlaceStorage(
    var docId: String? = null,
    var email: String? = null,
    var latitude: Double? = null, // 위도
    var longitude: Double? =null, // 경도
    var paddress: String? = null, // 도로명 주소
    var pname: String? = null, // 장소 이름
    var start: Boolean = false // 출발지인지 표시


)