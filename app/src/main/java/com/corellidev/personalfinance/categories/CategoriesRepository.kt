package com.corellidev.personalfinance.categories

import android.content.Context
import android.support.annotation.ColorInt
import com.corellidev.personalfinance.R
import com.corellidev.personalfinance.expenses.ExpenseRealmModel
import com.vicpin.krealmextensions.*
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Kamil on 2017-08-14.
 */
val NAME_FIELD_NAME = "name"
val COLOR_FIELD_NAME = "color"

@Singleton
class CategoriesRepository @Inject constructor(val context: Context){

    var modified = false

    fun addCategory(category: CategoryModel) {
        CategoryRealmModel(category.name, category.color).save()
        modified = true
    }

    //TODO could be changed to return observable (requires changes in CategoriesAdapter)
    fun getAllCategories(): List<CategoryModel> {
        var result = mutableListOf<CategoryModel>()
        for (categoryRealmModel in CategoryRealmModel().queryAll()) {
            result.add(CategoryModel(categoryRealmModel.name, categoryRealmModel.color))
        }
        return result
    }

    fun deleteCategory(categoryModel: CategoryModel) {
        ExpenseRealmModel()
                .query { it.equalTo("category", categoryModel.name) }
                .forEach{
                    it.category = context.getString(R.string.none_category_name)
                    it.save()
                }
        CategoryRealmModel().delete { query -> query.equalTo(NAME_FIELD_NAME, categoryModel.name) }
        modified = true
    }

    fun deleteAllCategoires() {
        CategoryRealmModel().deleteAll()
        modified= true
    }

    fun checkIfModified(): Boolean {
        modified = !modified
        return !modified
    }
}

open class CategoryRealmModel() : RealmObject() {

    constructor(name: String, @ColorInt color: Int) : this() {
        this.name = name
        this.color = color
    }

    @PrimaryKey
    var name: String = ""
    var color: Int = 0
}

