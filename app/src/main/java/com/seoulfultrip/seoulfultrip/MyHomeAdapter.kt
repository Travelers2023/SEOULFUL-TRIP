package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seoulfultrip.seoulfultrip.databinding.ItemHomeBinding

class MyHomeViewHolder(val binding: ItemHomeBinding): RecyclerView.ViewHolder(binding.root)
class MyHomeAdapter(val context: Context, val itemList: MutableList<PathStorage>): RecyclerView.Adapter<MyHomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyHomeViewHolder(ItemHomeBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: MyHomeViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.binding.run{
            pathName.text = data.pathName     // 경로 이름
            pathDate.text = data.pathDate
            startPlace.text = data.pstart
            endPlace.text = data.pend

            homeCv1.setOnClickListener{

                Intent(context, HomeDetailActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}