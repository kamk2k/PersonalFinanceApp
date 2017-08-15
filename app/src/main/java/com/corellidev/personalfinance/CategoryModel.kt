package com.corellidev.personalfinance

import android.content.Context
import android.support.annotation.ColorInt

/**
 * Created by Kamil on 2017-08-14.
 */

class CategoryModel(val name: String, @ColorInt val color: Int) {
    companion object ColorsManager {
        var currentColorIndex = 0;

        @ColorInt fun getNextColor(context: Context): Int {
            val colorsArray = context.resources.getIntArray(R.array.categoriesColors)
            currentColorIndex = (currentColorIndex + 1) % colorsArray.size
            return colorsArray.get(currentColorIndex)
        }
    }
    override fun toString(): String {
        return "CategoryModel(name='$name', color=$color)"
    }
}