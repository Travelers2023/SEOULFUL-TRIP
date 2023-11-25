package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.databinding.ItemPathBinding

class MyPathViewHolder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root) {
}

class MyPathAdapter(val context: Context, val itemList: MutableList<PlaceStorage>): RecyclerView.Adapter<MyPathViewHolder>() {
    lateinit var data: PlaceStorage
    val startPlace: String? = StartplaceAdapter.savestname[0] // 출발지 이름

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPathViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyPathViewHolder(ItemPathBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyPathViewHolder, position: Int) {
        data = itemList.get(position)
        val user = Firebase.auth.currentUser

        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
    }
}