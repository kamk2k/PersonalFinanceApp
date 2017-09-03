package com.corellidev.personalfinance.expenses

import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.query
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
        return Observable.fromIterable(ExpenseRealmModel().queryAll().sortedByDescending {it.time}).flatMap({
            expense -> Observable.just(ExpenseModel(expense.id, expense.name, expense.value, expense.category, expense.time))
        }).toList()

//        return expensesService.getAllExpenses(token).flatMap({ newExpenses ->
//            expenses = newExpenses.toMutableList()
//            Observable.just(newExpenses)
//        })
    }

    fun getExpense(id: String): Observable<ExpenseModel>{
        return Observable.just(ExpenseRealmModel()
                .query { query -> query.equalTo("id", id) }
                .map { ExpenseModel(it.id, it.name, it.value, it.category, it.time) }
                .first())
    }

    fun addExpense(expense: ExpenseModel): String {
        if(expense.id == BLANK_ID) {
            val expenseRealmModel = ExpenseRealmModel(expense.name, expense.value, expense.category, expense.time)
            expenseRealmModel.save()
            return expenseRealmModel.id
        } else {
            ExpenseRealmModel(expense.id, expense.name, expense.value, expense.category, expense.time).save()
            return expense.id
        }
//        val maxId = expenses.maxBy { exp ->  exp.id}
//        val addedExpense = ExpenseModel((maxId?.id?.plus(1) as Long), expense.name,
//                expense.value, expense.category, expense.time)
//        expensesService.addExpense(token, addedExpense)
//                .subscribeOn(Schedulers.io())
//                .subscribe()
    }

    fun deleteAllExpenses() {
        ExpenseRealmModel().deleteAll()
    }
}

open class ExpenseRealmModel() : RealmObject() {

    constructor(name : String, value: Double, category: String, time: Long) : this() {
        this.id = UUID.randomUUID().toString();
        this.name = name
        this.value = value
        this.category = category
        this.time = time
    }

    constructor(id: String, name : String, value: Double, category: String, time: Long) : this() {
        this.id = id
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