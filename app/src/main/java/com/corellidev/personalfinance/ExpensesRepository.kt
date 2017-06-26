package com.corellidev.personalfinance

import java.util.*

/**
 * Created by Kamil on 2017-06-22.
 */

class ExpensesRepository {

    val expenses: MutableList<ExpenseModel> = mutableListOf(
            ExpenseModel("expeeeeeeeeeeeeeeeeense 1", 12.5, "food", Date().getTime() - 6000000),
            ExpenseModel("new expense :) 2", 123.5, "car", Date().getTime() - 1000000),
            ExpenseModel("expense 3", 222.5, "food", Date().getTime() - 16000000),
            ExpenseModel("expense 4", 125.0, "utilities", Date().getTime() - 63000000)
    )

    fun getAllExpenses(): List<ExpenseModel> {
        return expenses
    }

    fun addExpense(expense: ExpenseModel) {
        expenses.add(expense)
    }
}