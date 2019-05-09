package com.packapps.videoview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.packapps.videoview.core.ContentData
import com.packapps.videoview.models.PublicationImpl
import com.packapps.videoview.utils.Utils

class PlaylistAdapter() : RecyclerView.Adapter<PlaylistAdapter.MyHolder>() {
    var list : MutableList<PublicationImpl> = mutableListOf()
    var listener : PlayListListener? = null
    val ALPHA = 0.3F

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val mView  = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)

        return MyHolder(mView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = list.get(position)
        //populate date on items
        holder.tvText.text = Utils.truncateText(item.title!!, 45)
        holder.tvTime.text =  Utils.formatDuration(item?.primaryContent?.duration)
        Glide.with(holder.itemView.context).load(item.thumbnail).apply(RequestOptions.centerCropTransform()).into(holder.ivThumbnails)
        if (item.read == true) {
            holder.ivThumbnails.alpha = ALPHA
            holder.tvText.alpha = ALPHA

        }



        //Implement clik item
        holder.itemView.setOnClickListener {
            listener?.itemClicked(item)
        }
    }


    fun updateAndNotifyDataSetChanged(list : MutableList<PublicationImpl>){
        this.list = list
        notifyDataSetChanged()
    }

    fun listenEvents(listener : PlayListListener){
        this.listener = listener
    }

    interface PlayListListener{
        fun itemClicked(item : PublicationImpl)
    }


    class MyHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tvText = view.findViewById<TextView>(R.id.tvItemDescription)
        val tvTime = view.findViewById<TextView>(R.id.tvItemTimePreview)
        var ivThumbnails = view.findViewById<ImageView>(R.id.ivItemThumbnails)

    }

}