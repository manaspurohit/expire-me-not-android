package com.manaspurohit.expiremenot.adapter

import com.manaspurohit.expiremenot.data.Item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.manaspurohit.expiremenot.R
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList

class ItemRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>() {

    private val listItem : MutableList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val todoItem : View = LayoutInflater.from(parent?.context).inflate(R.layout.item_row, parent, false)

        return ViewHolder(todoItem)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {
            holder.tvItemName?.text = listItem[position].name
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            holder.tvItemDate?.text = sdf.format(listItem[position].expireDate)
        }
    }

    fun addItem(name: String, expireDate: Date) {
        listItem.add(Item(name, expireDate))
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var tvItemName : TextView? = null
        var tvItemDate : TextView? = null

        init {
            tvItemName = itemView?.findViewById(R.id.tvItemName) as? TextView
            tvItemDate = itemView?.findViewById(R.id.tvItemDate) as? TextView
        }
    }
}
