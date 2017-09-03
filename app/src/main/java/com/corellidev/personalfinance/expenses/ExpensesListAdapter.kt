package com.corellidev.personalfinance.expenses

import android.content.Context
import kotlinx.android.synthetic.main.expense_list_item.view.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.corellidev.personalfinance.R
import com.corellidev.personalfinance.categories.CategoryModel
import java.util.*

/**
 * Created by Kamil on 2017-06-20.
 */

class ExpensesListAdapter(val context: Context, val categoriesMap : Map<String, CategoryModel>,
                          val onExpenseClickListener: OnExpenseClickListener) : Adapter<ExpensesListAdapter.ViewHolder>() {

    var items: MutableList<ExpenseModel> = ArrayList()

    interface OnExpenseClickListener {
        fun onExpenseClick(expenseModel: ExpenseModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return ViewHolder(layoutInflater.inflate(R.layout.expense_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items.get(position)
        val category = categoriesMap.get(item.category)
        (holder as ViewHolder).bind(item, category, context.resources.getColor(R.color.gray),
                View.OnClickListener { view -> onExpenseClickListener.onExpenseClick(item)})
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setContent(newItems: MutableList<ExpenseModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(expenseModel: ExpenseModel, categoryModel: CategoryModel?, defaultColor: Int,
                 onClickListener: View.OnClickListener) {
            itemView.setOnClickListener(onClickListener)
            itemView.expense_name.setText(expenseModel.name)
            itemView.expense_category.setText(expenseModel.category)
            itemView.icon.letter = expenseModel.category
            itemView.icon.shapeColor = if(categoryModel !=null) categoryModel.color else defaultColor
            itemView.expense_value.setText(expenseModel.value.toString())
        }
    }
}
