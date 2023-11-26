package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seoulfultrip.seoulfultrip.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    lateinit var binding:FragmentMyBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(inflater, container, false)

        if(MyApplication.checkAuth()){
            binding.useremail.text = "${MyApplication.email}"
            binding.username.text = "${MyApplication.name()}"
            MyApplication.db.collection("users").document(MyApplication.auth.uid.toString())
                .update(mapOf("userName" to "${MyApplication.name()}"))
                .addOnSuccessListener { Log.d("SWProject-username","${MyApplication.name()}")}
                .addOnFailureListener { e ->
                    Log.d("SWProject-username","${e.message}")
                }
        }

        binding.logoutBtn.setOnClickListener{
            logout()
        }

        val mainActivity = activity as MainActivity

        binding.sample1.setOnClickListener{
            //val newFragment = HomeFragment()
            /*val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.h_fragment, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()*/

            mainActivity.loadFragment(HomeFragment())
            mainActivity.updateIcons(mainActivity.getBottomNavigationView().menu.findItem(R.id.nav1), R.drawable.home_1)
        }

        binding.sample2.setOnClickListener {
            mainActivity.loadFragment(SaveFragment())
            mainActivity.updateIcons(mainActivity.getBottomNavigationView().menu.findItem(R.id.nav3), R.drawable.star_1)
        }

        return binding.root

    }

    private fun logout() {
        MyApplication.auth.signOut()
        MyApplication.email = null
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }
}