package com.corellidev.personalfinance

import android.support.annotation.ColorInt

/**
 * Created by Kamil on 2017-08-14.
 */

class CategoryModel(val name: String, @ColorInt val color: Int) {
    override fun toString(): String {
        return "CategoryModel(name='$name', color=$color)"
    }
}