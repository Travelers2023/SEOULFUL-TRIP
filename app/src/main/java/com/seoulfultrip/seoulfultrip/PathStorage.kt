package com.seoulfultrip.seoulfultrip

data class PathStorage(
    var docId: String = "",
    var email: String = "",
    var pathName: String = "",   // 경로 이름
    var pathDate: String = "",       // 경로를 만든 날짜
    var pstart: String = "",      // 시작 장소 이름
    var pname1: String = "",
    var pname2: String? = null,
    var pname3: String? = null,
    var pname4: String? = null,
    var pend: String = "",
)
