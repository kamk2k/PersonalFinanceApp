package com.corellidev.personalfinance.categories

import android.content.Context
import android.support.annotation.ColorInt
import com.corellidev.personalfinance.R

/**
 * Created by Kamil on 2017-08-14.
 */

class CategoryModel(val name: String, @ColorInt val color: Int) {
    companion object ColorsManager {
        @ColorInt fun getNextColor(context: Context): Int {
            val colorsArray = context.resources.getIntArray(R.array.categoriesColors)
            return colorsArray.get(Math.floor(Math.random() * colorsArray.size).toInt())
        }
    }
    override fun toString(): String {
        return "CategoryModel(name='$name', color=$color)"
    }
}