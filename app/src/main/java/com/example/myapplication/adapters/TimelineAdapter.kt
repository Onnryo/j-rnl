package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.TimelineItem
import kotlinx.android.synthetic.main.timeline_item.view.*

class TimelineAdapter(
    private val items: ArrayList<TimelineItem>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<TimelineAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.timeline_item,
            parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.body.text = item.body
        holder.tags.text = "Tags: "
    }

    override fun getItemCount() = items.size

    inner class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView = itemView.title
        var body: TextView = itemView.body
        var tags: TextView = itemView.tags

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(v!!, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }
}