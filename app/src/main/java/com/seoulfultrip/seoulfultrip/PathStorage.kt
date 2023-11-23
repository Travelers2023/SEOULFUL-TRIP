package com.seoulfultrip.seoulfultrip

data class PathStorage(
    val docId: String,
    val email: String,
    val pathName: String,   // 경로 이름
    val pathDate: String,       // 경로를 만든 날짜
    val pstart: String,      // 시작 장소 이름
    val pname1: String,
    val pname2: String? = null,
    val pname3: String? = null,
    val pname4: String? = null,
)
