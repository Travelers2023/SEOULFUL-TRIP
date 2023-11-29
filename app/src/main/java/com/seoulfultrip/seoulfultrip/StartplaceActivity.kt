package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoulfultrip.seoulfultrip.MyApplication.Companion.db
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.databinding.ActivityStartplaceBinding


class StartplaceActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartplaceBinding
    val itemList = mutableListOf<PlaceStorage>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Nexttoolbar) // toolbar 사용 선언
        getSupportActionBar()?.setTitle("출발지 설정하기")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        db.collection("place")
            //정렬 안 함
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(PlaceStorage::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.saveRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.saveRecyclerView.adapter = StartplaceAdapter(this, itemList)

            }
            .addOnFailureListener {
                Log.d("데이터 불러오기", "실패")
            }

//        adapter = MySaveAdapter(this, itemList)
//        binding.saveRecyclerView.adapter = adapter


        //intent.getParcelableArrayListExtra()
//        pname = intent.getStringArrayExtra("a")
//        Log.d("장소이름", "${pname}")



        // 출발지 선택을 위하여 배열 넘겨받기 구현 예정

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Menu 연걸
        menuInflater.inflate(R.menu.menu_next, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                savepname.clear() //화면 넘어가도 배열은 남아있어서 값전달 잘못돼서 배열초기화

            }

            R.id.next2_button -> {
                val intent = Intent(this, PathActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

