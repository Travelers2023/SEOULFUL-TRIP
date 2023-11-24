package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.databinding.ItemPathBinding

class MyPathViewHolder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root) {
}

class MyPathAdapter(val context: Context, val itemList: MutableList<PlaceStorage>): RecyclerView.Adapter<MyPathViewHolder>() {
    lateinit var data : PlaceStorage
    val startPlace:String? = StartplaceAdapter.savestname[0] // 출발지 이름

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

        // 여기 부분 다시 구현.. 받아온 배열의 이름과 같은 장소를 띄우기
        for (index in 0 until savepname.size) {
            if (savepname[index] == data.pname) {
                holder.binding.run {
                    if (user?.email == data.email) {
                        itemPathLayout.visibility = View.VISIBLE
                        itemImageView.visibility = View.VISIBLE
                        itemView.visibility = View.VISIBLE
                        itemNameView.text = "${data.pname}"
                        itemAddressView.text = "${data.paddress}"
                    }
                }
            }
        }

        Log.d("PathAdapter-1번째 선택지","${data.pname}")

    }


}
