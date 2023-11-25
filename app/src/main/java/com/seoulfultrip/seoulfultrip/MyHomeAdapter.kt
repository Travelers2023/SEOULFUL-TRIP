package com.seoulfultrip.seoulfultrip

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
        val user = Firebase.auth.currentUser
        val data = itemList.get(position)

        holder.binding.run{
            if(user?.email == data.email) { // 이메일로 거르기
                homeCv1.visibility = View.VISIBLE   // 경로가 있으면 visible
                pathName.text = data.pathName     // 경로 이름
                pathDate.text = data.pathDate
                startPlace.text = data.pstart
                endPlace.text = data.pend
            }

            homeCv1.setOnClickListener{

                Intent(context, HomeDetailActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }
}