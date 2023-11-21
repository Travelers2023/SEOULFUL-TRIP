package com.seoulfultrip.seoulfultrip

data class PathPlace(
val route : Result_trackoption,
val message : String,
val code : Int
)
data class Result_trackoption(
    val traoptimal : List<Result_path>
)
data class Result_path(
    val summary : Result_distance,
    val path : List<List<Double>>
)
data class Result_distance(
    val duration : Int
)



