package com.seoulfultrip.seoulfultrip

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoulfultrip.seoulfultrip.databinding.ActivityStartplaceBinding
import kotlin.math.log

class StartplaceActivity : AppCompatActivity() {
    val itemList = mutableListOf<PlaceStorage>()
    var pname: Array<out String>?= arrayOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStartplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Nexttoolbar) // toolbar 사용 선언
        getSupportActionBar()?.setTitle("출발지 설정하기")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        binding.saveRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.saveRecyclerView.adapter = MySaveAdapter(this, itemList)

        //intent.getParcelableArrayListExtra()
        pname = intent.getStringArrayExtra("a")
        Log.d("장소이름", "${pname}")


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
                // 다음 구현
            }
        }
        return super.onOptionsItemSelected(item)
    }
}