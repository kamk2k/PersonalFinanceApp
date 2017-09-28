package com.corellidev.personalfinance.expenses

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.corellidev.personalfinance.R
import com.corellidev.personalfinance.categories.CategoryModel
import kotlinx.android.synthetic.main.expense_list_item.view.*
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Kamil on 2017-06-20.
 */

class ExpensesListAdapter(val context: Context, categoriesMap : Map<String, CategoryModel>,
                          val callback: Callback) : Adapter<ExpensesListAdapter.ViewHolder>()  {

    var items: MutableList<ExpenseModel> = ArrayList()
    var deletedItems: HashMap<Int, ExpenseModel> = HashMap()
    var categoriesMap: Map<String, CategoryModel> = categoriesMap

    interface Callback {
        fun onExpenseClick(expenseModel: ExpenseModel)
        fun OnExpenseRemoved(expenseModel: ExpenseModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return ViewHolder(layoutInflater.inflate(R.layout.expense_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items.get(position)
        val category = categoriesMap.get(item.category)
        (holder as ViewHolder).bind(item, category, Color.GRAY,
                View.OnClickListener { view -> callback.onExpenseClick(item)})
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun onItemSwiped(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        val item = items.get(position)
        if(deletedItems.containsKey(position))
            deletedItems.put(position + 1, item)
        else
            deletedItems.put(position, item)
        removeFromDeletedItemsAfterDelay(position)
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun removeFromDeletedItemsAfterDelay(position: Int) {
        val timer = Timer("deletedItems clear", true);
        timer.schedule(object : TimerTask() {
            override fun run() {
                val expenseModel = deletedItems.remove(position)
                if(expenseModel != null) callback.OnExpenseRemoved(expenseModel)
            }
        }, 3500)
    }

    fun onUndoDeleteClicked() {
        deletedItems.forEach({
            items.add(it.key, it.value)
            notifyItemInserted(it.key)
        })
        deletedItems.clear()
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
