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
import com.seoulfultrip.seoulfultrip.databinding.ItemSaveBinding

class MySelectViewHolder(val binding: ItemSaveBinding) : RecyclerView.ViewHolder(binding.root) {
}

class MySelectAdapter(val context: Context, val itemList: MutableList<PlaceStorage>): RecyclerView.Adapter<MySelectViewHolder>() {

    val selectItem = SparseBooleanArray(0)
    val savepname = mutableListOf<String?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySelectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MySelectViewHolder(ItemSaveBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MySelectViewHolder, position: Int) {
        val data = itemList.get(position)
        val user = Firebase.auth.currentUser

        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

        holder.binding.run {
            if (user?.email == data.email) {
                itemSaveLayout.visibility = View.VISIBLE
                itemImageView.visibility = View.VISIBLE
                itemView.visibility = View.VISIBLE
                itemNameView.text = "${data.pname}"
                itemAddressView.text = "${data.paddress}"
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
            //setMultipleSelection(position)
        }

        if (data.selected) {
            //holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#26000000"))
            //selectItem.put(position, true)
        }
    }

    private fun setMultipleSelection(position: Int) {
        itemList[position].selected = !itemList[position].selected
        notifyItemChanged(position) // 데이터 적용


    }

}