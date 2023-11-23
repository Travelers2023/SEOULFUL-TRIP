package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.databinding.ItemPathBinding
import com.seoulfultrip.seoulfultrip.databinding.ItemSaveBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.PriorityQueue

class MyPathViewHolder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root) {
}

class MyPathAdapter(val context: Context, val itemList: MutableList<PlaceStorage>): RecyclerView.Adapter<MyPathViewHolder>() {
    lateinit var data : PlaceStorage
    val startPlace:String? = StartplaceAdapter.savestname[0] // 출발지 이름

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPathViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyPathViewHolder(ItemPathBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyPathViewHolder, position: Int) {
        data = itemList.get(position)
        val user = Firebase.auth.currentUser

        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

        // 여기 부분 다시 구현.. 받아온 배열의 이름과 같은 장소를 띄우기
        for (index in 0 until savepname.size) {
            if (savepname[index] == data.pname) {
                holder.binding.run {
                    if (user?.email == data.email) {
                        itemPathLayout.visibility = View.VISIBLE
                        itemImageView.visibility = View.VISIBLE
                        itemView.visibility = View.VISIBLE
                        itemNameView.text = "${data.pname}"
                        itemAddressView.text = "${data.paddress}"
                    }
                }
            }
        }

        Log.d("PathAdapter-1번째 선택지","${data.pname}")
//        calculateShortestPath(data.pname.toString())

    }
    /*

    // 다익스트라 알고리즘을 사용하여 장소를 최단 경로에 따라 정렬
    private fun calculateShortestPath(startPlace: String) {
        val graph = buildGraph() // 아래에서 구현한 함수

        if(startPlace.isNotBlank()){
            val startNode = graph.nodes.find { it.name == "${startPlace}" }
            Log.d("시작점","${startPlace}")

            if (startNode != null) {
                val shortestPath = dijkstra(graph, startNode)

                // 최단 경로에 따라 itemList를 재정렬
                itemList.sortBy { shortestPath.indexOfFirst { node -> node.name == it.pname } }
                notifyDataSetChanged()
            }
        }

    }

    //다익스트라 알고리즘 코드
    fun dijkstra(graph: Graph, startNode: Node): List<Node> {
        val distances = mutableMapOf<Node, Int>()
        val previous = mutableMapOf<Node, Node?>()

        for (node in graph.nodes) {
            distances[node] = Int.MAX_VALUE
            previous[node] = null
        }

        distances[startNode] = 0

        val unvisitedNodes = HashSet(graph.nodes)

        while (unvisitedNodes.isNotEmpty()) {
            val currentNode = unvisitedNodes.minByOrNull { distances[it]!! }!!
            unvisitedNodes.remove(currentNode)

            for (edge in graph.edges.filter { it.source == currentNode }) {
                val potentialDistance = distances[currentNode]!! + edge.weight
                if (potentialDistance < distances[edge.destination]!!) {
                    distances[edge.destination] = potentialDistance
                    previous[edge.destination] = currentNode
                }
            }
        }

        // 최단 경로를 반환합니다.
        val path = mutableListOf<Node>()
        var currentNode: Node? = graph.nodes.last()
        while (currentNode != null) {
            path.add(currentNode)
            currentNode = previous[currentNode]
        }
        path.reverse()

        return path
    }

    private fun buildGraph(): Graph {
        val graph = Graph()
        //위도, 경도 받아오기
        for (placeName in savepname) {
            val node = Node(placeName!!)
            graph.nodes.add(node)
        }

        for (i in 0 until graph.nodes.size) {
            for (j in i + 1 until graph.nodes.size) {
                val edgeWeight = getEdgeWeight(graph.nodes[i], graph.nodes[j]) // 시간 정보를 가져오는 함수
                val edge = Edge(graph.nodes[i], graph.nodes[j], edgeWeight)
                graph.edges.add(edge)
            }
        }

        return graph
    }

    private fun getEdgeWeight(startNode: Node, endNode: Node): Int {
        // API를 통해 경로의 시간 정보를 받아오는 코드
        val CLIENT_ID = "ylvy2f6syf"
        val CLIENT_SECRET = "6kWgp5OQ0jqstBFDg1AGMtisZdzUkJy9P6PI57AT"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PathAPI::class.java)

        var startLati: Double? = null
        var startLong: Double? = null
        var endLati: Double? = null
        var endLong: Double? = null

        if(data.pname == startNode.name) {
//            val startLati: Double? = data.latitude
            startLati = data.latitude
            startLong = data.longitude
        }
        if(data.pname == endNode.name) {
            endLati = data.latitude
            endLong = data.longitude
        }

        val callGetPath = api.getPath(CLIENT_ID, CLIENT_SECRET, "${startLati},${startLong}", "${endLati},${endLong}")
        Log.d("시작점 위경도","${startLati},${startLong}")
//        val callGetPath = api.getPath(CLIENT_ID, CLIENT_SECRET, "129.089441, 35.231100","129.084454, 35.228982")

        var a: Int = 0
        callGetPath.enqueue(object : Callback<PathPlace> { //PathPlace 데이터클래스 콜백
            override fun onResponse(call: Call<PathPlace>, response: Response<PathPlace>
            ){ //전달이 성공하면 여기 시작
                val pathlist = response.body()?.route?.traoptimal //데이터클래스에서 Result_trackoption까지 받음, list형식이라 뒤는 따로 받아와야함
                Log.d("경로", "${pathlist}")
                for (pathdi in pathlist!!){ //pathlist에서 summary.duration받아오기 위해 for문 사용
                    a = pathdi.summary.duration //시간받아옴
                    Log.d("거리 시간", "${a}")

//                    val edge = Edge(startNode, endNode, a)
//                    buildGraph().edges.add(edge)
                }

                //이거는 경로 간의 좌표들 인덱스로 받는건데 (path값 받아오는 거) 혹시 몰라서 주석처리 해놓음
                val path_container : MutableList<LatLng>? = mutableListOf(LatLng(0.1,0.1))
                for(path_cords in pathlist!!){
                    for(path_cords_xy in path_cords.path){
                        //구한 경로를 하나씩 path_container에 추가해줌
                        path_container?.add(LatLng(path_cords_xy[1], path_cords_xy[0]))
                        Log.d("경로1", "${path_container}")
                    }
                }
            }

            override fun onFailure(call: Call<PathPlace>, t: Throwable) {
                Log.d("실패", "실패")
            }
        })
        return a
    }

//    private fun calculateEdgeWeight(duration: Int): Int {
//        return duration
//    }

*/


}
