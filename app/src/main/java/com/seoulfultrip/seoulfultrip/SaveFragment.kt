package com.seoulfultrip.seoulfultrip

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.databinding.FragmentSaveBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaveFragment : Fragment() {
    lateinit var binding: FragmentSaveBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar) // toolbar 사용 선언
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        return binding.root
    }

    //파이어베이스에 있는 데이터 불러오기
    override fun onStart() {
        super.onStart()

        //로그인유저만 받는 거 구현 안 함 -> adapter에 구현
//        val user = Firebase.auth.currentUser

        MyApplication.db.collection("place")
            //정렬 안 함
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<PlaceStorage>()
                for (document in result) {
                    val item = document.toObject(PlaceStorage::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.saveRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.saveRecyclerView.adapter = MySaveAdapter(requireContext(), itemList)

            }
            .addOnFailureListener {
                Log.d("데이터 불러오기", "실패")
            }

    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu!!, inflater)
        inflater.inflate(R.menu.menu_saveselect, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.select_button -> {
                // 선택 버튼 구현 예정
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}