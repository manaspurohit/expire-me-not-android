package com.manaspurohit.expiremenot

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.widget.EditText
import com.manaspurohit.expiremenot.adapter.ItemRecyclerAdapter
import com.manaspurohit.expiremenot.data.Item
import com.manaspurohit.expiremenot.touch.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.dialog_add_item.*

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ItemListActivity : AppCompatActivity() {

    private lateinit var itemRecyclerAdapter : ItemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val app = application as? ExpireMeNotApplication
        app?.openRealm()

        btnAddItem.setOnClickListener { showAddItemDialog() }

        rvItemList.setHasFixedSize(true)
        rvItemList.layoutManager = LinearLayoutManager(this)
        itemRecyclerAdapter = ItemRecyclerAdapter(this, app?.realmItem)
        rvItemList.adapter = itemRecyclerAdapter

        val callback : ItemTouchHelper.Callback = ItemTouchHelperCallback(itemRecyclerAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rvItemList)
    }

    private fun showAddItemDialog() {
        val dialog = showDialog(getString(R.string.add_perish_item))

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val item = inputToItem(dialog)
            item?.let {
                itemRecyclerAdapter.addItem(it.name, it.expireDate)
                dialog.dismiss()
            }
        }
    }

    fun showEditItemDialog(curName: String, curDate: String, position: Int) {
        val dialog = showDialog(getString(R.string.edit_perish_item), curName, curDate)

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val item = inputToItem(dialog)
            item?.let {
                itemRecyclerAdapter.editItem(it.name, it.expireDate, position)
                dialog.dismiss()
            }
        }
    }

    private fun showDialog(title: String, nameText: String = "", dateText: String = ""): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(title)

        dialogBuilder.setView(R.layout.dialog_add_item)

        dialogBuilder.setPositiveButton(getString(R.string.ok)) { _, _ -> }

        dialogBuilder.setNeutralButton(getString(R.string.take_a_pic)) { _, _ ->
            // TODO: Change to camera view
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        val dialog = dialogBuilder.create()
        dialog.show()

        dialog.etName.setText(nameText)
        dialog.etDate.setText(dateText)

        return dialog
    }

    private fun inputToItem(dialog: AlertDialog): Item? {
        val etName = dialog.findViewById(R.id.etName) as? EditText
        val etDate = dialog.findViewById(R.id.etDate) as? EditText

        if (etName == null || etDate == null) {
            return null
        }

        if (TextUtils.isEmpty(etName.text)) {
            etName.error = getString(R.string.name_empty_error)
            return null
        }

        val name: String = etName.text.toString()

        val sdf = SimpleDateFormat(getString(R.string.date_format), Locale.US)
        val expireDate: Date = try {
            val initialDate = sdf.parse(etDate.text.toString())
            val cal = Calendar.getInstance()
            cal.time = initialDate
            cal.add(Calendar.HOUR_OF_DAY, 23)
            cal.time
        }
        catch (e: ParseException) {
            etDate.error = "Please use mm/dd/yyyy format"
            return null
        }

        if (Calendar.getInstance().time.after(expireDate)) {
            etDate.error = "Please enter a valid date"
            return null
        }

        return Item(name, expireDate)
    }

    override fun onDestroy() {
        (application as? ExpireMeNotApplication)?.closeRealm()
        super.onDestroy()
    }
}
