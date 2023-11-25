package com.seoulfultrip.seoulfultrip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.seoulfultrip.seoulfultrip.databinding.FragmentHomeBinding

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mainActivity = activity as MainActivity

        binding.homeTextbtn.setOnClickListener {
            mainActivity.loadFragment(SearchFragment())
            mainActivity.updateIcons(mainActivity.getBottomNavigationView().menu.findItem(R.id.nav2), R.drawable.search_1)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth()){
            MyApplication.db.collection("path")
                .orderBy("pathDate", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<PathStorage>()
                    for (document in result){
                        val item = document.toObject(PathStorage::class.java)
                        item.docId = document.id
                        itemList.add(item)
                        Log.d("home - itemList","${itemList}")
                    }
                    binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.homeRecyclerView.adapter = MyHomeAdapter(requireContext(),itemList)
                }
                .addOnFailureListener {e ->
                    Log.d("home - 오류","${e.message}")
                    Toast.makeText(requireContext(),"데이터 불러오기 실패",Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Home 새로고침을 위한 코드
    override fun onResume() {
        super.onResume()
    }

    fun refreshAdapter() {
        // 파이어베이스에서 경로 받아온 후 리사이클러뷰 재정렬 필요 .. 구현 예정
    }
}