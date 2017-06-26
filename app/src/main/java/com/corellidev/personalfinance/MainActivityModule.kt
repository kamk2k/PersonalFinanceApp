package com.corellidev.personalfinance

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Kamil on 2017-06-22.
 */
@Module
class MainActivityModule() {
    @Provides
    @Singleton
    fun provideExpensesRepository(): ExpensesRepository = ExpensesRepository()
}
