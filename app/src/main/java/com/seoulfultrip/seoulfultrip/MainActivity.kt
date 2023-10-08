package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.seoulfultrip.seoulfultrip.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home page"

        if(MyApplication.checkAuth()){bottomNavigationView = findViewById(R.id.bottomNavigationView)
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav1 -> {
                        updateIcons(item, R.drawable.home_1)
                        loadFragment(HomeFragment())
                        supportActionBar?.title = "Home page"
                    }
                    R.id.nav2 -> {
                        loadFragment(SearchFragment())
                        supportActionBar?.title = "Search"
                        updateIcons(item, R.drawable.search_1)
                    }
                    R.id.nav3 -> {
                        loadFragment(SaveFragment())
                        supportActionBar?.title = "Character"
                        updateIcons(item, R.drawable.star_1)
                    }
                    R.id.nav4 -> {
                        loadFragment(MyFragment())
                        supportActionBar?.title = "Review"
                        updateIcons(item, R.drawable.my_1)
                    }
                }
                true
            }
            // Set the default fragment to load when the activity is created
            loadFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.nav1
        }
        else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

//        MyApplication.db.collection("users").document(auth.uid.toString())
//            .get()
//            .addOnSuccessListener {  }

    }
    override fun onStart() {
        // Intent에서 finish() 돌아올 때 실행
        // onCreate -> onStart
        super.onStart()

//            updateUserLevelBasedOnReviewCount(auth.uid.toString())
//
        if(MyApplication.checkAuth()) {

            bottomNavigationView = findViewById(R.id.bottomNavigationView)
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav1 -> {
                        updateIcons(item, R.drawable.home_1)
                        loadFragment(HomeFragment())
                        supportActionBar?.title = "Home page"
                    }

                    R.id.nav2 -> {
                        loadFragment(SearchFragment())
                        supportActionBar?.title = "Search"
                        updateIcons(item, R.drawable.search_1)
                    }

                    R.id.nav3 -> {
                        loadFragment(SaveFragment())
                        supportActionBar?.title = "Character"
                        updateIcons(item, R.drawable.star_1)
                    }

                    R.id.nav4 -> {
                        loadFragment(MyFragment())
                        supportActionBar?.title = "Review"
                        updateIcons(item, R.drawable.my_1)
                    }
                }
                true

            }
        }else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
    }
    private fun updateIcons(selectedItem: MenuItem, selectedIconRes: Int) {
        // 선택된 항목의 아이콘을 선택된 아이콘으로 변경합니다.
        selectedItem.setIcon(selectedIconRes)
        // 다른 항목들의 아이콘을 기본 아이콘으로 되돌립니다.
        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item != selectedItem) {
                when (item.itemId) {
                    R.id.nav1 -> item.setIcon(R.drawable.home)
                    R.id.nav2 -> item.setIcon(R.drawable.search)
                    R.id.nav3 -> item.setIcon(R.drawable.star)
                    R.id.nav4 -> item.setIcon(R.drawable.my)
                }
            }
        }
    }
    public fun loadFragment(fragment: Fragment, message : Int = 0) {
        val bundle = Bundle()
        bundle.putInt("message", message)
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_layout, fragment)
        transaction.addToBackStack(null) // Optional: Add the fragment to the back stack
        transaction.commit()
    }

}



