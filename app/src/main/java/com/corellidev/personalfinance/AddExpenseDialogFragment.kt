package com.corellidev.personalfinance

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import kotlinx.android.synthetic.main.add_expense_dialog.view.*
import java.util.*

/**
 * Created by Kamil on 2017-06-20.
 */

class AddExpenseDialogFragment : DialogFragment() {

    var addClickListener: AddClickListener? = null

    val TAG = "AddExpenseDialogFragment"

    interface AddClickListener {
        fun onAddClick(expense: ExpenseModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.add_expense_dialog, null)
        val nameInput = view.expense_name_input
        val valueInput = view.expense_value_input
        val categoryInput = view.expense_category_input
        return AlertDialog.Builder(activity)
                .setTitle("Add new expense")
                .setView(view)
                .setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                    val name = nameInput.text.toString()
                    val value = valueInput.text.toString()
                    val category = categoryInput.text.toString()
                    val expense = ExpenseModel(-1, name, value.toDouble(), category, Date().time)
                    Log.d("MyTag", "expense = " + expense)
                    addClickListener?.onAddClick(expense)
                }
                .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                }
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                }
    }
}