package com.corellidev.personalfinance

/**
 * Created by Kamil on 2017-06-20.
 */

class ExpenseModel(val name: String, val value: Double, val category: String, val time: Long) {
    override fun toString(): String {
        return "ExpenseModel(name='$name', value=$value, category='$category', time=$time)"
    }
//    var name: String = ""
//    var value: Double = 0.0;
//    var category: String = "none"
//    var time: Long = 0
}