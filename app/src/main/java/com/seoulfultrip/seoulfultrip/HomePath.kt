package com.seoulfultrip.seoulfultrip

data class HomePath(
    val pathName: String,   // 경로 이름
    val date: String,       // 경로를 만든 날짜
    val start: String,      // 시작 장소 이름
    val end: String,        // 마지막 장소 이름
    val imageResource: Int  // 이미지 리소스 ID (정수)를 저장할 필드
)
