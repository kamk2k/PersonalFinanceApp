package com.corellidev.personalfinance

import android.app.Application
import android.content.Context
import com.corellidev.personalfinance.expenses.ExpenseServiceDelegate
import com.corellidev.personalfinance.expenses.ExpensesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Kamil on 2017-06-22.
 */
@Module
class MainActivityModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideExpensesService(context: Context): ExpenseServiceDelegate = ExpenseServiceDelegate(context)

    @Provides
    @Singleton
    fun provideExpensesRepository(expenseServiceDelegate: ExpenseServiceDelegate):
            ExpensesRepository = ExpensesRepository(expenseServiceDelegate)
}
