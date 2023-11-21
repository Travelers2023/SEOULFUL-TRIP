package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.MyApplication.Companion.db
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectBinding
    val itemList = mutableListOf<PlaceStorage>()
    //lateinit var adapter : MySelectAdapter
    private val adapter = MySelectAdapter(this, itemList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Selecttoolbar) // toolbar 사용 선언
        getSupportActionBar()?.setTitle("장소 선택하기")
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
                binding.saveRecyclerView.adapter = MySelectAdapter(this, itemList)

            }
            .addOnFailureListener {
                Log.d("데이터 불러오기", "실패")
            }

    }


    override fun onRestart() {
        super.onRestart()
        SaveFragment().refreshAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Menu 연걸
        menuInflater.inflate(R.menu.menu_delete, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼
                finish()
            }

            R.id.delete_button -> { // 삭제 구현
                deleteItem()
                finish()
            }

            R.id.next1_button -> {
                val intent = Intent(this,StartplaceActivity::class.java )
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem() {
        // 해당 장소를 Firestore에서 삭제하는 코드 추가
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

        var placeName : String?
        for (index in 0 .. savepname.size - 1) {

            placeName = savepname[index]

            val placeRef = db.collection("place").whereEqualTo("pname", placeName)

            placeRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        // Firestore에서 해당 장소 삭제
                        db.collection("place").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this@SelectActivity,"장소 삭제 완료",Toast.LENGTH_SHORT).show()
                                Log.d("Firestore", "DocumentSnapshot successfully deleted.")
//                                MainActivity().loadFragment(SaveFragment())
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "삭제 실패", e)
                            }
                    }
                } else {
                    Log.d("Firestore", "삭제 에러 :", task.exception)
                }

            }

        }

    }


}