package com.manaspurohit.expiremenot

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_item_list.*

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ItemListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        btnAddItem.setOnClickListener { showAddItemDialog() }
    }

    private fun showAddItemDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.add_perish_item))

        dialogBuilder.setView(R.layout.dialog_add_item)

        dialogBuilder.setPositiveButton(getString(R.string.ok)) { _, _ -> }

        dialogBuilder.setNeutralButton(getString(R.string.take_a_pic)) { _, _ ->
            // TODO: Change to camera view
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        val dialog = dialogBuilder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val etName = dialog.findViewById(R.id.etName) as? EditText
            val etDate = dialog.findViewById(R.id.etDate) as? EditText

            if (etName == null || etDate == null) {
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(etName.text)) {
                etName.error = getString(R.string.name_empty_error)
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat(getString(R.string.date_format), Locale.US)

            val expireDate = try {
                val initialDate = sdf.parse(etDate.text.toString())
                val cal = Calendar.getInstance()
                cal.time = initialDate
                cal.add(Calendar.HOUR_OF_DAY, 23)
                cal.time
            }
            catch (e: ParseException) {
                etDate.error = "Please use mm/dd/yyyy format"
                return@setOnClickListener
            }

            if (Calendar.getInstance().time.after(expireDate)) {
                etDate.error = "Please enter a valid date"
                return@setOnClickListener
            }

            // TODO: Add item to recycler view

            dialog.dismiss()
        }
    }
}
