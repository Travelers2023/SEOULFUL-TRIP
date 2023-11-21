package com.seoulfultrip.seoulfultrip

data class PlaceStorage(
    var docId: String? = null,
    var email: String? = null,
    var latitude: Double? = null,
    var longitude: Double? =null,
    var paddress: String? = null,
    var pname: String? = null,
    var start: Boolean = false // 출발지인지 표시


)