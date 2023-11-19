package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.graphics.Color
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
            if(user?.email == data.email) {
                itemSaveLayout.visibility = View.VISIBLE
                itemImageView.visibility = View.VISIBLE
                itemView.visibility = View.VISIBLE
                itemNameView.text = "${data.pname}"
                itemAddressView.text = "${data.paddress}"
            }
        }

        holder.binding.itemSaveLayout.setOnClickListener {
            setMultipleSelection(position)
        }

        if(data.selected){
            holder.binding.itemSaveLayout.setBackgroundColor(Color.parseColor("#26000000"))
        }
    }

    private fun setMultipleSelection(position: Int) {
        itemList[position].selected = !itemList[position].selected
        notifyItemChanged(position) // 데이터 적용
    }
}