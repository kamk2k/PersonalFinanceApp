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

    private var items: MutableList<ExpenseModel> = ArrayList()

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
