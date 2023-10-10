package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seoulfultrip.seoulfultrip.databinding.ItemSaveBinding

// 전부 레이아웃 테스트 작성용 firebase 연결하면 코드 수정해야 함

class MySaveViewHolder(val binding: ItemSaveBinding) : RecyclerView.ViewHolder(binding.root) {
    //var idImageView = itemView.findViewById<ImageView>(R.id.itemImageView)
    var idNameView = itemView.findViewById<TextView>(R.id.itemNameView)
    var idAddressView = itemView.findViewById<TextView>(R.id.itemAddressView)

    fun setListData(listData: savedata_test) {
        //idImageView.text = "@drawable/test"
        idNameView.text = "${listData.name}"
        idAddressView.text = listData.address
    }
}

class MySaveAdapter(val context: Context, val itemList: MutableList<savedata_test>): RecyclerView.Adapter<MySaveViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySaveViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MySaveViewHolder(ItemSaveBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MySaveViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            holder.setListData(data)
        }
    }
}