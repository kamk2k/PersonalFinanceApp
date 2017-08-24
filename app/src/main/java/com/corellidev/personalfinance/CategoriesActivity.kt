package com.corellidev.personalfinance

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.categories_list_item.view.*
import kotlinx.android.synthetic.main.content_categories.*
import javax.inject.Inject

/**
 * Created by Kamil on 2017-08-22.
 */

class CategoriesActivity : AppCompatActivity(), AddCategoryDialogFragment.AddClickListener,
        CategoriesActivityAdapter.OnClickListener {
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    lateinit var listAdapter: CategoriesActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        setSupportActionBar(toolbar)
        MainActivity.mainActivityComponent.inject(this)

        with (categories_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CategoriesActivity)
            listAdapter = CategoriesActivityAdapter(this@CategoriesActivity, categoriesRepository.getAllCategories().toMutableList())
            listAdapter.clickListener = this@CategoriesActivity
            adapter = listAdapter
        }

        fab.setOnClickListener({
            val addCategoryDialogFragment = AddCategoryDialogFragment()
            addCategoryDialogFragment.addClickListener = this
            addCategoryDialogFragment.show(supportFragmentManager, addCategoryDialogFragment.TAG)
        })
    }

    override fun onAddClick(categoryModel: CategoryModel) {
        categoriesRepository.addCategory(categoryModel)
        listAdapter.items.add(categoryModel)
        listAdapter.notifyItemInserted(listAdapter.itemCount)
    }

    override fun onDeleteClick(categoryModel: CategoryModel) {
        categoriesRepository.removeCategory(categoryModel)
        val indexOfRemovedItem = listAdapter.items.indexOf(categoryModel)
        listAdapter.items.remove(categoryModel)
        listAdapter.notifyItemRemoved(indexOfRemovedItem)
    }
}

class CategoriesActivityAdapter(val context: Context, val items: MutableList<CategoryModel>)  : RecyclerView.Adapter<CategoriesActivityAdapter.ViewHolder>() {

    var clickListener: OnClickListener ? = null

    interface OnClickListener {
        fun onDeleteClick(categoryModel: CategoryModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.categories_list_item, null, true), clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View, val onClickListener: OnClickListener?) : RecyclerView.ViewHolder(itemView) {
        fun bind(categoryModel: CategoryModel) {
            with(categoryModel) {
                itemView.category_icon.letter = name
                itemView.category_icon.shapeColor = color
                itemView.category_title.setText(name)
                itemView.remove_category_button.setOnClickListener({
                    onClickListener?.onDeleteClick(this)
                })
            }
        }
    }
}

class AddCategoryDialogFragment : DialogFragment() {
    val TAG = "AddCategoryDialogFragment"
    var addClickListener: AddClickListener? = null

    interface AddClickListener {
        fun onAddClick(categoryModel: CategoryModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.add_category_dialog, null)
        val nameInput = view.category_name_input
        return AlertDialog.Builder(activity)
                .setTitle("Add new category")
                .setView(view)
                .setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                    val name = nameInput.text.toString()
                    val newCategory = CategoryModel(name, CategoryModel.ColorsManager.getNextColor(context))
                    addClickListener?.onAddClick(newCategory)
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