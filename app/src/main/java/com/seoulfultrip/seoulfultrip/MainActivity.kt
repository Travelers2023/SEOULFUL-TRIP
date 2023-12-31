package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.seoulfultrip.seoulfultrip.MyApplication.Companion.auth
import com.seoulfultrip.seoulfultrip.MyApplication.Companion.db
import com.seoulfultrip.seoulfultrip.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home page"

        if (MyApplication.checkAuth()) {
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
                        supportActionBar?.title = "Save"
                        updateIcons(item, R.drawable.star_1)
                    }
                    R.id.nav4 -> {
                        loadFragment(MyFragment())
                        supportActionBar?.title = "My page"
                        updateIcons(item, R.drawable.my_1)
                    }
                }
                true
            }

            // Set the default fragment to load when the activity is created
            loadFragment(HomeFragment())
            bottomNavigationView.selectedItemId = R.id.nav1
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        db.collection("users").document(auth.uid.toString())
            .get()
            .addOnSuccessListener { }
    }

    override fun onStart() {
        super.onStart()
        if (MyApplication.checkAuth()) {
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
                        supportActionBar?.title = "Save"
                        updateIcons(item, R.drawable.star_1)
                    }
                    R.id.nav4 -> {
                        loadFragment(MyFragment())
                        supportActionBar?.title = "My page"
                        updateIcons(item, R.drawable.my_1)
                    }
                }
                true
            }
        } else {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    public fun updateIcons(selectedItem: MenuItem, selectedIconRes: Int) {
        selectedItem.setIcon(selectedIconRes)
        val menu = binding.bottomNavigationView.menu
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

    public fun loadFragment(fragment: Fragment, message: Int = 0) {
        val bundle = Bundle()
        bundle.putInt("message", message)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getBottomNavigationView(): BottomNavigationView {
        return bottomNavigationView
    }

}
