package com.corellidev.personalfinance

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


class ExpensesListAdapter : Adapter<ExpensesListAdapter.ViewHolder>() {

    private val items: MutableList<ExpenseModel>

    init {
// TODO replace mocked data with real ones
        items = ArrayList()
        items.add(ExpenseModel("expeeeeeeeeeeeeeeeeense 1", 12.5, "food", Date().getTime() - 6000000))
        items.add(ExpenseModel("expense 2", 123.5, "car", Date().getTime() - 1000000))
        items.add(ExpenseModel("expense 3", 222.5, "food", Date().getTime() - 16000000))
        items.add(ExpenseModel("expense 4", 125.0, "utilities", Date().getTime() - 63000000))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return ViewHolder(layoutInflater.inflate(R.layout.expense_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items.get(position)
        holder?.name?.setText(item.name)
        holder?.category?.setText(item.category)
        holder?.value?.setText(item.value.toString())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.icon
        val name = itemView.expense_name
        val category = itemView.expense_category
        val value = itemView.expense_value
    }
}
