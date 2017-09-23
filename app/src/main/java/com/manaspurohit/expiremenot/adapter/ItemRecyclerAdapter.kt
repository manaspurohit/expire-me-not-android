package com.manaspurohit.expiremenot.adapter

import com.manaspurohit.expiremenot.data.Item

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.manaspurohit.expiremenot.R
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.RealmResults
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList

class ItemRecyclerAdapter(
        private val context: Context,
        private val realmItem: Realm?
) : RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>() {

    private val listItem : MutableList<Item> = ArrayList()

    init {
        realmItem?.let {
            val itemResult : RealmResults<Item>? = it.where(Item::class.java)
                    .findAll().sort("expireDate", Sort.ASCENDING)
            itemResult?.forEach { listItem.add(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val todoItem : View = LayoutInflater.from(parent?.context).inflate(R.layout.item_row, parent, false)

        return ViewHolder(todoItem)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {
            it.tvItemName?.text = listItem[position].name
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            it.tvItemDate?.text = sdf.format(listItem[position].expireDate)
        }
    }

    fun addItem(name: String, expireDate: Date) {
        if (realmItem == null) return

        realmItem.beginTransaction()
        val newItem = realmItem.createObject(Item::class.java, UUID.randomUUID().toString())
        newItem.name = name
        newItem.expireDate = expireDate
        realmItem.commitTransaction()

        listItem.add(Item(name, expireDate))
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var tvItemName : TextView? = null
            private set
        var tvItemDate : TextView? = null
            private set

        init {
            tvItemName = itemView?.findViewById(R.id.tvItemName) as? TextView
            tvItemDate = itemView?.findViewById(R.id.tvItemDate) as? TextView
        }
    }
}
