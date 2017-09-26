package com.manaspurohit.expiremenot.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.manaspurohit.expiremenot.ItemListActivity
import com.manaspurohit.expiremenot.data.Item
import com.manaspurohit.expiremenot.R
import com.manaspurohit.expiremenot.touch.ItemTouchHelperAdapter
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.item_row.view.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.collections.ArrayList
import kotlin.collections.MutableList

class ItemRecyclerAdapter(
        private val context: Context,
        private val realmItem: Realm?
) : RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private val listItem : MutableList<Item> = ArrayList()

    init {
        realmItem?.let {
            val itemResult : RealmResults<Item>? = it.where(Item::class.java)
                    .findAll().sort("expireDate", Sort.ASCENDING)
            itemResult?.forEach { listItem.add(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent?.context).inflate(R.layout.item_row, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.let {
            it.tvItemName?.text = listItem[position].name
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            it.tvItemDate?.text = sdf.format(listItem[position].expireDate)
            it.itemView.setOnClickListener {
                (context as? ItemListActivity)?.showEditItemDialog(it.tvItemName?.text.toString(),
                        it.tvItemDate?.text.toString(), position)
            }
        }
    }

    override fun onItemDismiss(position: Int?) {
        if (position == null) return

        realmItem?.beginTransaction()
        listItem[position].deleteFromRealm()
        realmItem?.commitTransaction()

        listItem.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(name: String, expireDate: Date) {
        if (realmItem == null) return

        realmItem.beginTransaction()
        val newItem = realmItem.createObject(Item::class.java, UUID.randomUUID().toString())
        newItem.name = name
        newItem.expireDate = expireDate
        realmItem.commitTransaction()

        // binary search returns index if found or twos complement of insertion point if not found
        // ..... mind = blown
        var index = listItem.binarySearch { it.expireDate.compareTo(expireDate) }
        if (index < 0) index = index.inv()

        listItem.add(index, newItem)
        notifyDataSetChanged()
    }

    fun editItem(name: String, expireDate: Date, position: Int) {
        if (realmItem == null) return

        realmItem.beginTransaction()
        listItem[position].deleteFromRealm()
        realmItem.commitTransaction()

        listItem.removeAt(position)

        addItem(name, expireDate)
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
