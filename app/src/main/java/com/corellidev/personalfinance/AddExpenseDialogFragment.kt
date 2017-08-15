package com.corellidev.personalfinance

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.add_expense_dialog.view.*
import kotlinx.android.synthetic.main.spinner_category_item.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Kamil on 2017-06-20.
 */

class AddExpenseDialogFragment : DialogFragment() {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
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
        MainActivity.mainActivityComponent.inject(this)
        categoryInput.adapter = CategoriesAdapter(context, categoriesRepository.getAllCategories())
        return AlertDialog.Builder(activity)
                .setTitle("Add new expense")
                .setView(view)
                .setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                    val name = nameInput.text.toString()
                    val value = valueInput.text.toString()
                    val expense = ExpenseModel(-1, name, value.toDouble(), categoryInput.selectedItem.toString(), Date().time)
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

class CategoriesAdapter(val context: Context, val list: List<CategoryModel>)
    : BaseAdapter() {
    override fun getItem(position: Int): CategoryModel {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_category_item, null, true)
        val item = getItem(position)
        view.category_icon.letter = item.name
        view.category_title.setText(item.name)
        return view
    }
}