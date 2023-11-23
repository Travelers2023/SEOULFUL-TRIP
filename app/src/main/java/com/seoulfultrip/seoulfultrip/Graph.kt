package com.seoulfultrip.seoulfultrip

import android.util.SparseBooleanArray

data class Node(
    val name: String ,
    var latitude: Double? = null, // 위도
    var longitude: Double? =null) // 경도
data class Edge(val source: Node, val destination: Node, val weight: Int)
class Graph {
    val nodes = SparseBooleanArray(0)
    val edges = mutableListOf<Edge>()
}
