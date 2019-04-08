package com.packapps.videoview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter() : RecyclerView.Adapter<PlaylistAdapter.MyHolder>() {
    val list : MutableList<String> = mutableListOf()
    init {
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val mView  = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)

        return MyHolder(mView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {}


    class MyHolder(view : View) : RecyclerView.ViewHolder(view) {}
}