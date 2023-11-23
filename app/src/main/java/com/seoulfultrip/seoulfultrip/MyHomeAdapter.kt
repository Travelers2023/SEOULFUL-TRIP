package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MyHomeAdapter(val context: Context, val itemList: List<PathStorage>, private val listener: OnItemClickListener): RecyclerView.Adapter<MyHomeAdapter.MyHomeViewHolder>() {

    inner class MyHomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.home_cv1)
        val itemTextView: TextView = itemView.findViewById(R.id.pathName)   // 경로 이름

        init {
            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 데이터 리스트로부터 아이템 데이터 참조
                    val clickedItem = itemList[position]
                    listener.onItemClicked(clickedItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return MyHomeViewHolder(view)

        /*val layoutInflater = LayoutInflater.from(parent.context)
        return MyHomeViewHolder(ItemHomeBinding.inflate(layoutInflater))*/
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyHomeViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemTextView.text = currentItem.pathName     // 경로 이름
    }

    interface OnItemClickListener {
        fun onItemClicked(item: PathStorage)
    }

}