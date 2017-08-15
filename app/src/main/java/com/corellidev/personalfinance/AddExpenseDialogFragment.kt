package com.corellidev.personalfinance

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
        val categorySpinner = view.expense_category_input
        val newCategoryInput = view.new_category_input
        MainActivity.mainActivityComponent.inject(this)
        categorySpinner.adapter = CategoriesAdapter(context, categoriesRepository.getAllCategories())
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position >= categorySpinner.adapter.count - 1) {
                    newCategoryInput.visibility = View.VISIBLE
                } else {
                    newCategoryInput.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                newCategoryInput.visibility = View.GONE
            }
        }
        return AlertDialog.Builder(activity)
                .setTitle("Add new expense")
                .setView(view)
                .setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                    val name = nameInput.text.toString()
                    val value = valueInput.text.toString()
                    val category = if (newCategoryInput.visibility == View.VISIBLE) newCategoryInput.text else categorySpinner.selectedItem.toString()
                    val expense = ExpenseModel(-1, name, value.toDouble(), category.toString(), Date().time)
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

class CategoriesAdapter(val context: Context, val list: List<CategoryModel>) : BaseAdapter() {

    override fun getItem(position: Int): CategoryModel {
        if(position >= list.size) {
            return CategoryModel("", Color.CYAN)
        } else {
            return list.get(position)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size + 1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if(position >= list.size) {
            val view = LayoutInflater.from(context).inflate(R.layout.spinner_add_category_item, null, true)
            return view
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.spinner_category_item, null, true)
            val item = getItem(position)
            view.category_icon.letter = item.name
            view.category_title.setText(item.name)
            return view
        }
    }
}