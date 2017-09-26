package com.manaspurohit.expiremenot.touch

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class ItemTouchHelperCallback(
        private val itemTouchHelperAdapter: ItemTouchHelperAdapter
) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView?,
                                  viewHolder: RecyclerView.ViewHolder?): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?,
                        viewHolder: RecyclerView.ViewHolder?,
                        target: RecyclerView.ViewHolder?): Boolean = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        itemTouchHelperAdapter.onItemDismiss(viewHolder?.adapterPosition)
    }
}