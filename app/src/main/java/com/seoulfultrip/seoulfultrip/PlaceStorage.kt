package com.seoulfultrip.seoulfultrip

import com.google.firebase.firestore.GeoPoint

data class PlaceStorage(
    var docId: String? = null,
    var email: String? = null,
    var latitude: Double? = null,
    var longitude: Double? =null,
    var paddress: String? = null,
    var pname: String? = null,
    var selected: Boolean = false, // 선택됐는지 표시
    var start: Boolean = false // 출발지인지 표시

)
