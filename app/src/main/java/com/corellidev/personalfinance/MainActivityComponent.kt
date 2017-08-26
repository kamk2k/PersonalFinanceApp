package com.corellidev.personalfinance

import com.corellidev.personalfinance.categories.CategoriesActivity
import com.corellidev.personalfinance.expenses.AddExpenseDialogFragment
import com.corellidev.personalfinance.expenses.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Kamil on 2017-06-22.
 */
@Singleton
@Component(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(addExpenseDialogFragment: AddExpenseDialogFragment)
    fun inject(categoriesActivity: CategoriesActivity)
}
