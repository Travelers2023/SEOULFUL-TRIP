package com.seoulfultrip.seoulfultrip

data class Node(
    val name: String ,
    var latitude: Double? = null, // 위도
    var longitude: Double? =null) // 경도
data class Edge(val source: Node, val destination: Node, val weight: Int)
class Graph {
    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()
}
