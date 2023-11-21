package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.databinding.ActivityStartplaceBinding
import com.seoulfultrip.seoulfultrip.databinding.ItemSaveBinding
import kotlin.math.log
import com.google.firebase.database.*


class StartplaceActivity : AppCompatActivity() {
    val itemList = mutableListOf<PlaceStorage>()
    val selectItemList = mutableListOf<PlaceStorage>()
    //private lateinit var adapter: MySaveAdapter
    var pname: Array<out String>?= arrayOf("")
    private val adapter = MySelectAdapter(this, itemList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStartplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Nexttoolbar) // toolbar 사용 선언
        getSupportActionBar()?.setTitle("출발지 설정하기")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
/*
        binding.saveRecyclerView.layoutManager = LinearLayoutManager(this)

        //binding.saveRecyclerView.adapter = StartplaceAdapter(this, pname)

      binding.saveRecyclerView.adapter = MySaveAdapter(this, itemList)
        //adapter = MySaveAdapter(this, itemList)
        //binding.saveRecyclerView.adapter = adapter

        //intent.getParcelableArrayListExtra()
        pname = intent.getStringArrayExtra("a")
        Log.d("장소이름", "${pname}")

        //adapter.savepname
*/
        MyApplication.db.collection("place")
            //정렬 안 함
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(PlaceStorage::class.java)
                    item.docId = document.id
                    itemList.add(item)
                    for (i in 0..itemList.size){
                        //if(itemList.get(i).pname==pname)
                            //selectItemList.add(pname)
                    }

                }
                binding.saveRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.saveRecyclerView.adapter = MySelectAdapter(this,itemList)

            }
            .addOnFailureListener {
                Log.d("데이터 불러오기", "실패")
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Menu 연걸
        menuInflater.inflate(R.menu.menu_next, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                finish()
            }

            R.id.next2_button -> {

                val intent = Intent(this, PathActivity::class.java)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }
}

/*
class StartplaceViewHolder(val binding: ItemSaveBinding):  RecyclerView.ViewHolder(binding.root)
class StartplaceAdapter(val context: Context, val datas: Array<out String>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val itemList = mutableListOf<PlaceStorage>()
    private val adapter = MySelectAdapter(context, itemList)
    override fun getItemCount(): Int {
        return datas?.size?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            =StartplaceViewHolder(ItemSaveBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as StartplaceViewHolder).binding
        binding.itemNameView.text = adapter.savepname.iterator().next()

    }
}*/
