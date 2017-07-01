package com.corellidev.personalfinance

import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by Kamil on 2017-06-22.
 */

class ExpensesRepository {
    var expensesService: ExpenseServiceDelegate

    constructor(expensesService: ExpenseServiceDelegate) {
        this.expensesService = expensesService
    }

    //TODO local storing
    var expenses: MutableList<ExpenseModel> = mutableListOf()

    fun getAllExpenses(): Observable<List<ExpenseModel>> {
        return expensesService.getAllExpenses().flatMap({ newExpenses ->
            expenses = newExpenses.toMutableList()
            Observable.just(newExpenses)
        })
    }

    fun addExpense(expense: ExpenseModel) {
        val maxId = expenses.maxBy { exp ->  exp.id}
        val addedExpense = ExpenseModel((maxId?.id?.plus(1) as Long), expense.name,
                expense.value, expense.category, expense.time)
        expensesService.addExpense(addedExpense)
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}