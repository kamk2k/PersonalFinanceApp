package com.corellidev.personalfinance

import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by Kamil on 2017-06-22.
 */

class ExpensesRepository {
    var expensesService: ExpenseServiceDelegate
    var token: String? = null

    constructor(expensesService: ExpenseServiceDelegate) {
        this.expensesService = expensesService
    }

    //TODO local storing
    var expenses: MutableList<ExpenseModel> = mutableListOf()

    fun getAllExpenses(): Single<List<ExpenseModel>> {
        return Observable.fromIterable(ExpenseRealmModel().queryAll()).flatMap({
            expense -> Observable.just(ExpenseModel(0, expense.name, expense.value, expense.category, expense.time))
        }).toList()

//        return expensesService.getAllExpenses(token).flatMap({ newExpenses ->
//            expenses = newExpenses.toMutableList()
//            Observable.just(newExpenses)
//        })
    }

    fun addExpense(expense: ExpenseModel) {
        ExpenseRealmModel(expense.name, expense.value, expense.category, expense.time).save()
//        val maxId = expenses.maxBy { exp ->  exp.id}
//        val addedExpense = ExpenseModel((maxId?.id?.plus(1) as Long), expense.name,
//                expense.value, expense.category, expense.time)
//        expensesService.addExpense(token, addedExpense)
//                .subscribeOn(Schedulers.io())
//                .subscribe()
    }
}

open class ExpenseRealmModel() : RealmObject() {

    constructor(name : String, value: Double, category: String, time: Long) : this() {
        this.name = name
        this.value = value
        this.category = category
        this.time = time
    }

    @PrimaryKey
    var id = UUID.randomUUID().toString();
    var name: String = ""
    var value: Double = 0.0
    var category: String = ""
    var time: Long = 0
}