package com.corellidev.personalfinance

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.categories_list_item.view.*
import kotlinx.android.synthetic.main.content_categories.*
import javax.inject.Inject

/**
 * Created by Kamil on 2017-08-22.
 */

class CategoriesActivity : AppCompatActivity() {

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
            listAdapter = CategoriesActivityAdapter(this@CategoriesActivity, categoriesRepository.getAllCategories())
            adapter = listAdapter
        }

        fab.setOnClickListener({
            // todo : add category
        })
    }
}

class CategoriesActivityAdapter(val context: Context, val items: List<CategoryModel>)  : RecyclerView.Adapter<CategoriesActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.categories_list_item, null, true))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(categoryModel: CategoryModel) {
            with(categoryModel) {
                itemView.category_icon.letter = name
                itemView.category_title.setText(name)
            }
        }
    }
}