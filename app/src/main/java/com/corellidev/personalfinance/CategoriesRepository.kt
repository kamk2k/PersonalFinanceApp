package com.corellidev.personalfinance

import android.support.annotation.ColorInt
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Kamil on 2017-08-14.
 */
@Singleton
class CategoriesRepository @Inject constructor(){

    fun addCategory(category: CategoryModel) {
        CategoryRealmModel(category.name, category.color).save()
    }

    //TODO could be changed to return observable (requires changes in CategoriesAdapter)
    fun getAllCategories(): List<CategoryModel> {
        var result = mutableListOf<CategoryModel>()
        for (categoryRealmModel in CategoryRealmModel().queryAll()) {
            result.add(CategoryModel(categoryRealmModel.name, categoryRealmModel.color))
        }
        return result
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

