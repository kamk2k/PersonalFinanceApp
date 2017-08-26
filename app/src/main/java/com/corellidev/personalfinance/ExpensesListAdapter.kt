package com.corellidev.personalfinance

import android.content.Context
import kotlinx.android.synthetic.main.expense_list_item.view.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by Kamil on 2017-06-20.
 */


class ExpensesListAdapter(val context: Context, val categoriesMap : Map<String, CategoryModel>) : Adapter<ExpensesListAdapter.ViewHolder>() {

    var items: MutableList<ExpenseModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return ViewHolder(layoutInflater.inflate(R.layout.expense_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items.get(position)
        val category = categoriesMap.get(item.category)
        holder?.name?.setText(item.name)
        holder?.category?.setText(item.category)
        holder?.icon?.letter = item.category
        holder?.icon?.shapeColor = if(category !=null) category.color else context.resources.getColor(R.color.gray)
        holder?.value?.setText(item.value.toString())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setContent(newItems: MutableList<ExpenseModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.icon
        val name = itemView.expense_name
        val category = itemView.expense_category
        val value = itemView.expense_value
    }
}
