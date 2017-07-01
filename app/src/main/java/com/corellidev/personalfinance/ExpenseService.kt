package com.corellidev.personalfinance

import android.content.Context
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.OkHttpClient



/**
 * Created by Kamil on 2017-06-28.
 */

interface  ExpenseService{
    @GET("/all")
    fun getAllExpenses(): Observable<List<ExpenseModel>>;

    @POST("/add")
    fun addExpense(@Body expenseModel: ExpenseModel): Observable<ExpenseModel>
}

class ExpenseServiceDelegate(context: Context) {

    var loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var httpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)

    val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .baseUrl(context.getString(R.string.personal_finance_backend_url))
            .build()

    val expensesService: ExpenseService = retrofit.create(ExpenseService::class.java)

    fun getAllExpenses(): Observable<List<ExpenseModel>> {
        return expensesService.getAllExpenses();
    }

    fun addExpense(expenseModel: ExpenseModel): Observable<ExpenseModel> {
        return expensesService.addExpense(expenseModel)
    }
}