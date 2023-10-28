package com.seoulfultrip.seoulfultrip

import com.google.firebase.firestore.GeoPoint

data class PlaceStorage(
    var docId: String? = null,
    var email: String? = null,
    var latitude: Double? = null,
    var longitude: Double? =null,
    var paddress: String? = null,
    var pname: String? = null

)
