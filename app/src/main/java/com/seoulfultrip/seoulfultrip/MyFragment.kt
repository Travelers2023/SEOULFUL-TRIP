package com.seoulfultrip.seoulfultrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root

    }

    private fun logout() {
        MyApplication.auth.signOut()
        MyApplication.email = null
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }
}