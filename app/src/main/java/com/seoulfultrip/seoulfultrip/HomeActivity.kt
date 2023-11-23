package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoulfultrip.seoulfultrip.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), MyHomeAdapter.OnItemClickListener {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = findViewById(R.id.home_cv1)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = createItemList() // RecyclerView에 표시할 아이템 리스트 생성 (임시 데이터)

        val adapter = MyHomeAdapter(context = this, itemList = itemList, listener = this)
        recyclerView.adapter = adapter

    }

    override fun onItemClicked(item: PathStorage) {
        val intent = Intent(this, HomeDetailActivity::class.java)
        startActivity(intent)
    }

    private fun createItemList(): List<PathStorage> {
        val itemList = mutableListOf<PathStorage>()
        // 아이템 리스트 생성...
//        itemList.add(PathStorage("첫째 날", "2023-11-20", "한강공원", "타마"))
//        itemList.add(PathStorage("둘째 날", "2023-11-21", "건대", "홍대"))
        /*return listOf(
            HomePath("첫째 날", "2023-11-20", "한강공원", "타마", R.id.home_line),
            HomePath("둘째 날", "2023-11-21", "건대", "홍대", R.id.home_line)
        )*/
        return itemList
    }
}