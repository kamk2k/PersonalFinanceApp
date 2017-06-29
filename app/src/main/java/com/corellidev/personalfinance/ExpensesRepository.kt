package com.corellidev.personalfinance

import io.reactivex.Observable
import java.util.*

/**
 * Created by Kamil on 2017-06-22.
 */

class ExpensesRepository {
    var expensesService: ExpenseServiceDelegate

    constructor(expensesService: ExpenseServiceDelegate) {
        this.expensesService = expensesService
    }

    //TODO local storing
    val expenses: MutableList<ExpenseModel> = mutableListOf()

    fun getAllExpenses(): Observable<List<ExpenseModel>> {
        return expensesService.getAllExpenses()
    }

    fun addExpense(expense: ExpenseModel) {
        expenses.add(expense)
    }
}