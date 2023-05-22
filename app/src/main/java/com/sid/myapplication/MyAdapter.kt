package com.sid.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter(
    private val cycleList: ArrayList<cycleData>,
    private val context: MainActivity
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cycle_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cycleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(cycleList[position].cycleUrl).into(holder.cycleImg)
        holder.cycleName.text = cycleList[position].cycleName
        holder.cycleDesc.text = cycleList[position].cycleDesc

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cycleName: TextView = itemView.findViewById(R.id.cycleName)
        val cycleDesc: TextView = itemView.findViewById(R.id.cycleDesc)
        val cycleImg: ImageView = itemView.findViewById(R.id.cycleImg)
    }
}