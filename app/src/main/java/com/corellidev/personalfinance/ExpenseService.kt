package com.corellidev.personalfinance

import android.content.Context
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Kamil on 2017-06-28.
 */

interface  ExpenseService{
    @GET("/all")
    fun getAllExpenses(): Observable<List<ExpenseModel>>;
}

class ExpenseServiceDelegate(context: Context) {

    val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(context.getString(R.string.personal_finance_backend_url))
            .build()

    val expensesService: ExpenseService = retrofit.create(ExpenseService::class.java)

    fun getAllExpenses(): Observable<List<ExpenseModel>> {
        return expensesService.getAllExpenses();
    }
}