package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoulfultrip.seoulfultrip.databinding.ItemSaveBinding

class MySaveViewHolder(val binding: ItemSaveBinding) : RecyclerView.ViewHolder(binding.root) {
    /*
    var idNameView = itemView.findViewById<TextView>(R.id.itemNameView)
    var idAddressView = itemView.findViewById<TextView>(R.id.itemAddressView)

    fun setListData(listData: PlaceStorage) {
        idNameView.text = "${listData.pname}"
        idAddressView.text = "${listData.paddress}"
    }
    */
}

class MySaveAdapter(val context: Context, val itemList: MutableList<PlaceStorage>): RecyclerView.Adapter<MySaveViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySaveViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MySaveViewHolder(ItemSaveBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MySaveViewHolder, position: Int) {
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
    }
}