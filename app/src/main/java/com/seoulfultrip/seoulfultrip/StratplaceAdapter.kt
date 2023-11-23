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
companion object {
    val savestname = mutableListOf<String?>()
}
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

        for(index in 0 .. savepname.size - 1) {
            if (savepname[index] == data.pname) {
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
        }
        //savepname.clear()

        holder.binding.itemSaveLayout.setOnClickListener {
            if (savestname.size==1){ //선택지 하나만 받아오기
                selectItem.put(position,true)
                holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#00000000"))
            }
            if (selectItem.get(position, false)){
                selectItem.put(position,false)
                holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#00000000"))
                //Log.d("첫","통과")

                val deletepname = mutableListOf<String?>()
                savestname.forEachIndexed { index, value ->
                    if (value == itemList.get(position).pname){
                        deletepname.add(value)
                        Log.d("삭제st","${savestname[index]}")
                    }
                }
                savestname.removeAll(deletepname)
                Log.d("삭제st","${savestname}")
            } else{
                selectItem.put(position,true)
                holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#26000000"))
                savestname.add(itemList.get(position).pname)
                Log.d("추가st","${savestname}")
            }

        }

    }


}