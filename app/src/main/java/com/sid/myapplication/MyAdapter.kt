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

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cycle_list, parent, false)
        return MyViewHolder(itemView, mListener)


    }

    override fun getItemCount(): Int {
        return cycleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(cycleList[position].cycleUrl).into(holder.cycleImg)
        holder.cycleName.text = cycleList[position].cycleName
        holder.cycleDesc.text = cycleList[position].cycleDesc
        holder.Url.text = cycleList[position].cycleUrl

    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val cycleName: TextView = itemView.findViewById(R.id.cycleName)
        val cycleDesc: TextView = itemView.findViewById(R.id.cycleDesc)
        val cycleImg: ImageView = itemView.findViewById(R.id.cycleImg)
        val Url: TextView = itemView.findViewById(R.id.url)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}