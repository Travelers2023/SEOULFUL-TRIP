package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.MySelectAdapter.Companion.savepname
import com.seoulfultrip.seoulfultrip.databinding.ItemSaveBinding

class StartplaceViewHolder(val binding: ItemSaveBinding) : RecyclerView.ViewHolder(binding.root) {
}

class StartplaceAdapter(val context: Context, val itemList: MutableList<PlaceStorage>): RecyclerView.Adapter<StartplaceViewHolder>() {
//    companion object {
//        val startPlace: String? = null
//    }

    val selectItem = SparseBooleanArray(0)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartplaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StartplaceViewHolder(ItemSaveBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: StartplaceViewHolder, position: Int) {
        val data = itemList.get(position)
        val user = Firebase.auth.currentUser

        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

        // 여기 부분 다시 구현.. 받아온 배열의 이름과 같은 장소를 띄우기
        for(index in 0 .. savepname.size - 1) {
            if(savepname[index] == data.pname)
            holder.binding.run {
                if (user?.email == data.email) {
                    itemSaveLayout.visibility = View.VISIBLE
                    itemImageView.visibility = View.VISIBLE
                    itemView.visibility = View.VISIBLE
                    itemNameView.text = "${data.pname}"
                    itemAddressView.text = "${data.paddress}"
                }
            }
        }

        holder.binding.itemSaveLayout.setOnClickListener {
            if (selectItem.get(position, false)){
                selectItem.put(position,false)
                holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#00000000"))
                //Log.d("첫","통과")

                val deletepname = mutableListOf<String?>()
                savepname.forEachIndexed { index, value ->
                    if (value == itemList.get(position).pname){
                        deletepname.add(value)
                        Log.d("삭제","${savepname[index]}")
                    }
                }
                savepname.removeAll(deletepname)
                Log.d("삭제","${savepname}")
            } else{
                selectItem.put(position,true)
                holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#26000000"))
                savepname.add(itemList.get(position).pname)
                Log.d("추가","${savepname}")
            }

        }

    }


}