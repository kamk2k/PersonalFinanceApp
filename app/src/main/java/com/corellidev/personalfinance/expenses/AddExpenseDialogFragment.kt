package com.corellidev.personalfinance.expenses

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.corellidev.personalfinance.R
import com.corellidev.personalfinance.categories.CategoriesRepository
import com.corellidev.personalfinance.categories.CategoryModel
import kotlinx.android.synthetic.main.add_expense_dialog.view.*
import kotlinx.android.synthetic.main.spinner_category_item.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Kamil on 2017-06-20.
 */

val EDITED_EXPENSE_ID = "EDITED_EXPENSE_ID"

class AddExpenseDialogFragment : DialogFragment() {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var expensesRepository: ExpensesRepository

    var acceptClickListener: AcceptClickListener? = null

    val TAG = "AddExpenseDialogFragment"

    interface AcceptClickListener {
        fun onAddClick(expense: ExpenseModel)
        fun onEditAcceptClick(expense: ExpenseModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val editMode = arguments != null && arguments.keySet().contains(EDITED_EXPENSE_ID)
        val view = activity.layoutInflater.inflate(R.layout.add_expense_dialog, null)
        MainActivity.mainActivityComponent.inject(this)
        val nameInput = view.expense_name_input
        val valueInput = view.expense_value_input
        val categorySpinner = view.expense_category_input
        val newCategoryInput = view.new_category_input
        val existingCategories = categoriesRepository.getAllCategories()
        val categoriesNamesArrayList  = ArrayList<String>()
        var expenseId: String? = null
        var expenseTimestamp: Long? = null
        valueInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                val str = editable.toString()
                val posDot = str.indexOf(".")
                if (posDot <= 0) return
                if (str.length - posDot - 1 > 2) {
                    editable.delete(posDot + 3, posDot + 4)
                }
            }
            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
        })
        existingCategories.forEach({item -> categoriesNamesArrayList.add(item.name)})
        categorySpinner.adapter = CategoriesDialogAdapter(context, existingCategories)
        if(editMode) {
            expensesRepository.getExpense(arguments.getString(EDITED_EXPENSE_ID)).subscribe({ editedExpense ->
                nameInput.setText(editedExpense.name)
                valueInput.setText(editedExpense.value.toString())
                categorySpinner.setSelection(existingCategories.map { it.name }.indexOf(editedExpense.category))
                expenseId = editedExpense.id
                expenseTimestamp = editedExpense.time
            })
        }
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
        val titleText = if(editMode) "Edit expense" else "Add new expense"
        val positiveButtonText = if(editMode) "Accept" else "Add"
        val dialog = AlertDialog.Builder(activity)
                .setTitle(titleText)
                .setView(view)
                .setPositiveButton(positiveButtonText) { dialogInterface: DialogInterface, i: Int ->
                    val name = nameInput.text.toString()
                    val value = valueInput.text.toString()
                    var categoryName: String
                    if (newCategoryInput.visibility == View.VISIBLE) {
                        categoryName = newCategoryInput.text.toString()
                        categoriesRepository.addCategory(CategoryModel(categoryName,
                                CategoryModel.getNextColor(context)))
                    } else {
                        categoryName = (categorySpinner.selectedItem as CategoryModel).name
                    }
                    if(editMode && expenseId != null && expenseTimestamp != null) {
                        acceptClickListener?.onEditAcceptClick(
                                ExpenseModel(expenseId!!, name, value.toDouble(), categoryName, expenseTimestamp!!))
                    } else {
                        acceptClickListener?.onAddClick(
                                ExpenseModel(BLANK_ID, name, value.toDouble(), categoryName, Date().time))
                    }
                }
                .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                }
                .create()
        dialog.setOnShowListener({
            dialog ->
            newCategoryInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (categoriesNamesArrayList.contains(s.toString())) {
                        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    } else {
                        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
        })
        dialog.apply {setCanceledOnTouchOutside(false)}
        return dialog
    }
}

class CategoriesDialogAdapter(val context: Context, val list: List<CategoryModel>) : BaseAdapter() {

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
            view.category_icon.shapeColor = item.color
            view.category_title.setText(item.name)
            return view
        }
    }
}