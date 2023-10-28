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

    fun setListData(listData: PlaceStorage) {
        //idImageView.text = "@drawable/test"
        //To 강휘 /이름 주소 PlaceStorage에서 받아와서 수정함 확인하면 주석 지워주세용 -수정-
        idNameView.text = "${listData.pname}"
        idAddressView.text = "${listData.paddress}"
    }
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

        holder.binding.run {
            holder.setListData(data)
        }
    }
}