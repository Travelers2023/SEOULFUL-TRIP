package com.seoulfultrip.seoulfultrip

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.databinding.ActivityHomeDetailBinding

class HomeDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeDetailBinding
    val itemList = mutableListOf<PathStorage>()
    val docList = mutableListOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar = binding.Hometoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        var docId = intent.getStringExtra("docId")
        Log.d("문서확인","${docId}")
        val user = Firebase.auth.currentUser

        if(MyApplication.checkAuth()){
            MyApplication.db.collection("path")
                .orderBy("pathDate", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->

                    for (document in result){
                        val item = document.toObject(PathStorage::class.java)
                        Log.d("문서확인2","${document.id}")
                        item.docId = document.id
                        Log.d("문서확인3","${item}")
                            if (item.docId==docId){
                                itemList.add(item)

                                Log.d("home - itemList","${itemList}")
                                supportActionBar?.setTitle("${itemList.get(0).pathName}")
                                listvisible()
                            }

                    }
                }
                .addOnFailureListener {e ->
                    Log.d("home - 오류","${e.message}")
                    Toast.makeText(this,"데이터 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
        }






    }

    override fun onRestart() {
        super.onRestart()
        SaveFragment().refreshAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun listvisible(){
        //for (index in 0..itemList.size-1) { //최종리스트에 있는 장소 갯수만큼 view생성 / 출발지와 그 다음 장소는 필수
        val num = itemList.get(0)
        var count: Int
        if (num.pname4!="sample text : 아이템 장소 이름"){ count = 4 }
        else if (num.pname3!="sample text : 아이템 장소 이름") {count=3}
        else if (num.pname2!="sample text : 아이템 장소 이름") {count=2}
        else if (num.pname1!="sample text : 아이템 장소 이름") {count=1}
        else{count=0}

        for (index in 0..count) {
            when (index) {
                0 -> {
                    binding.itemNameView1.setText(num.pstart)
                    binding.itemNameView1.visibility = View.VISIBLE
                    binding.itemImageView1.visibility = View.VISIBLE
                    binding.itemPathline1.visibility = View.VISIBLE
                }

                1 -> {
                    binding.itemNameView2.setText(num.pname1)
                    binding.itemNameView2.visibility = View.VISIBLE
                    binding.itemImageView2.visibility = View.VISIBLE
                    binding.itemPathline1.visibility = View.VISIBLE
                }

                2 -> {
                    binding.itemNameView3.setText(num.pname2)
                    binding.itemNameView3.visibility = View.VISIBLE
                    binding.itemImageView3.visibility = View.VISIBLE
                    binding.itemPathline2.visibility = View.VISIBLE
                }

                3 -> {
                    binding.itemNameView4.setText(num.pname3)
                    binding.itemNameView4.visibility = View.VISIBLE
                    binding.itemImageView4.visibility = View.VISIBLE
                    binding.itemPathline3.visibility = View.VISIBLE
                }


                4 -> {
                    binding.itemNameView5.setText(num.pname4)
                    binding.itemNameView5.visibility = View.VISIBLE
                    binding.itemImageView5.visibility = View.VISIBLE
                    binding.itemPathline4.visibility = View.VISIBLE
                }
            }
        }


    }

}